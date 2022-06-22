package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.helper.ByteHelper;
import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.io.SliceFullReader;
import cn.kizzzy.qqhx.MfpFile;
import cn.kizzzy.qqhx.SfpFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class MfpFileHandler implements IFileHandler<MfpFile> {
    
    private static final short[] MAGIC = new short[]{
        0x50, 0x45, 0x41, 0x4B
    };
    
    @Override
    public MfpFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        reader.setLittleEndian(true);
        
        MfpFile mfpFile = new MfpFile();
        mfpFile.magic = reader.readUnsignedBytes(4);
        
        if (!ByteHelper.equals(mfpFile.magic, MAGIC)) {
            return null;
        }
        
        mfpFile.fileOffset = reader.readInt();
        mfpFile.reserved_01 = reader.readInt();
        mfpFile.reserved_02 = reader.readInt();
        mfpFile.paletteSize = reader.readUnsignedShort();
        mfpFile.paletteCount = reader.readUnsignedShort();
        mfpFile.reserved_04 = reader.readInt();
        mfpFile.fileCount = reader.readInt();
        
        mfpFile.palettes = new short[mfpFile.paletteCount];
        for (int i = 0, n = mfpFile.paletteCount; i < n; ++i) {
            mfpFile.palettes[i] = reader.readShort();
        }
        
        mfpFile.frames = new MfpFile.Frame[mfpFile.fileCount];
        for (int i = 0, n = mfpFile.fileCount; i < n; ++i) {
            MfpFile.Frame frame = new MfpFile.Frame();
            frame.reserved_01 = reader.readShort();
            frame.reserved_02 = reader.readShort();
            frame.reserved_03 = reader.readShort();
            frame.reserved_04 = reader.readShort();
            frame.offsetX = reader.readShort();
            frame.offsetY = reader.readShort();
            frame.width = reader.readUnsignedShort();
            frame.height = reader.readUnsignedShort();
            
            mfpFile.frames[i] = frame;
        }
        
        IFileHandler<SfpFile> handler = new SfpFileHandler(mfpFile.palettes);
        
        mfpFile.files = new SfpFile[mfpFile.fileCount];
        for (int i = 0, n = mfpFile.fileCount; i < n; ++i) {
            IFullyReader reader1 = new SliceFullReader(reader, reader.position(), size);
            SfpFile sfpFile = handler.load(vfs, path, reader1, size);
            mfpFile.files[i] = sfpFile;
        }
        
        return mfpFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, MfpFile data) throws Exception {
        return false;
    }
}
