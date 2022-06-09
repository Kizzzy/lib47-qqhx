package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.helper.ByteHelper;
import cn.kizzzy.helper.LogHelper;
import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.qqhx.SfpFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class SfpFileHandler implements IFileHandler<SfpFile> {
    
    private static final short[] MAGIC = new short[]{
        0x47, 0x46
    };
    
    private static final int HEADER_SIZE = 0x14;
    
    private final short[] palettes;
    
    public SfpFileHandler() {
        this(null);
    }
    
    public SfpFileHandler(short[] palettes) {
        this.palettes = palettes;
    }
    
    @Override
    public SfpFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        SfpFile sfpFile = new SfpFile();
        try {
            sfpFile.magic = reader.readUnsignedBytes(2);
            
            if (!ByteHelper.equals(sfpFile.magic, MAGIC)) {
                return null;
            }
            
            sfpFile.fileSize = reader.readIntEx();
            sfpFile.width = reader.readIntEx();
            sfpFile.height = reader.readIntEx();
            sfpFile.reserved_01 = reader.readShortEx();
            sfpFile.dataOffset = reader.readIntEx();
            sfpFile.palettes = palettes != null ? palettes : reader.readShortExs((sfpFile.dataOffset - HEADER_SIZE) / 2);
            sfpFile.pixelData = new int[sfpFile.width * sfpFile.height];
            
            boolean hasPalettes = sfpFile.palettes != null && sfpFile.palettes.length > 0;
            
            int pixelIndex = 0;
            for (int r = 0, h = sfpFile.height; r < h; ++r) {
                int count = reader.readShortEx();
                
                for (long i = reader.position(), x = i + count - 2; i < x; i = reader.position()) {
                    int byte1 = reader.readUnsignedByte();
                    if (byte1 < 192) {
                        if (byte1 < 128) {
                            if (byte1 < 64) {
                                for (int j = 0, y = byte1 + 1; j < y; ++j) {
                                    int argb = 0xFFFFFF;
                                    sfpFile.pixelData[pixelIndex++] = argb;
                                }
                            } else {
                                if (!hasPalettes) {
                                    for (int j = 0, y = byte1 + 1; j < y; ++j) {
                                        int argb = 0xFFFFFF;
                                        sfpFile.pixelData[pixelIndex++] = argb;
                                    }
                                } else {
                                    int byte2 = reader.readUnsignedByte();
                                    for (int j = 0, y = byte1 - 63; j < y; ++j) {
                                        int argb = argb565(0, 0, byte2 * 8);
                                        sfpFile.pixelData[pixelIndex++] = argb;
                                    }
                                }
                            }
                        } else {
                            if (!hasPalettes) {
                                int c = byte1 - 127;
                                int[][] arr = new int[c][];
                                for (int j = 0, y = c; j < y; ++j) {
                                    arr[j] = new int[2];
                                    arr[j][0] = reader.readShortEx();
                                }
                                for (int j = 0, y = c; j < y; ++j) {
                                    arr[j][1] = reader.readUnsignedByte();
                                    int argb = argb565(arr[j][0], 1, arr[j][1]);
                                    sfpFile.pixelData[pixelIndex++] = argb;
                                }
                            } else {
                                for (int j = 0, y = byte1 - 127; j < y; ++j) {
                                    int byte2 = reader.readUnsignedByte();
                                    int value = sfpFile.palettes[byte2];
                                    int argb = argb565(value, 1, 255);
                                    sfpFile.pixelData[pixelIndex++] = argb;
                                }
                            }
                        }
                    } else {
                        if (!hasPalettes) {
                            int c = byte1 - 127;
                            int[][] arr = new int[c][];
                            for (int j = 0, y = c; j < y; ++j) {
                                arr[j] = new int[2];
                                arr[j][0] = reader.readShortEx();
                            }
                            for (int j = 0, y = c; j < y; ++j) {
                                arr[j][1] = reader.readUnsignedByte();
                                int argb = argb565(arr[j][0], 1, arr[j][1]);
                                sfpFile.pixelData[pixelIndex++] = argb;
                            }
                        } else {
                            for (int j = 0, y = byte1 - 191; j < y; ++j) {
                                int byte2 = reader.readUnsignedByte();
                                int value = sfpFile.palettes[byte2];
                                int byte3 = reader.readUnsignedByte();
                                int argb = argb565(value, 1, byte3 * 8);
                                sfpFile.pixelData[pixelIndex++] = argb;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.error("load sfp file error: ", e);
        }
        return sfpFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, SfpFile data) throws Exception {
        return false;
    }
    
    private int argb565(int value, int val2, int alpha) {
        int b = 0;
        int g = 0;
        int r = 0;
        int a = Math.min(alpha, 255);
        
        if (value != 0) {
            b = calc(value, 0, 5);
            g = calc(value, 5, 6);
            r = calc(value, 11, 5);
        }
        return b | (g << 8) | (r << 16) | (a << 24);
    }
    
    public static int calc(int value, int start, int n) {
        return calc(value, start, n, true);
    }
    
    public static int calc(int value, int start, int n, boolean blend) {
        value = value >> start & (1 << n) - 1;
        if (blend) {
            value = (value << (8 - n) | value >> (2 * n - 8));
        }
        return value;
    }
}
