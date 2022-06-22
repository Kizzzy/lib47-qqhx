package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.helper.ByteHelper;
import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.io.SliceFullReader;
import cn.kizzzy.qqhx.MspFile;
import cn.kizzzy.qqhx.SfpFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class MspFileHandler implements IFileHandler<MspFile> {
    
    private static final short[] MAGIC = new short[]{
        0x50, 0x45, 0x41, 0x4B
    };
    
    @Override
    public MspFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        reader.setLittleEndian(true);
        
        MspFile mspFile = new MspFile();
        mspFile.magic = reader.readUnsignedBytes(4);
        
        if (!ByteHelper.equals(mspFile.magic, MAGIC)) {
            return null;
        }
        
        mspFile.fileOffset = reader.readInt();
        mspFile.reserved_01 = reader.readInt();
        mspFile.reserved_02 = reader.readInt();
        mspFile.paletteSize = reader.readUnsignedShort();
        mspFile.paletteCount = reader.readUnsignedShort();
        mspFile.reserved_04 = reader.readInt();
        mspFile.unitCount = reader.readInt();
        mspFile.totalCount = reader.readInt();
        
        mspFile.palettes = new short[mspFile.paletteCount];
        for (int i = 0, n = mspFile.paletteCount; i < n; ++i) {
            mspFile.palettes[i] = reader.readShort();
        }
        
        mspFile.unknowns = new int[96];
        for (int i = 0, n = mspFile.unknowns.length; i < n; ++i) {
            mspFile.unknowns[i] = reader.readInt();
        }
        
        mspFile.frames = new MspFile.Frame[mspFile.totalCount];
        for (int i = 0, n = mspFile.totalCount; i < n; ++i) {
            MspFile.Frame frame = new MspFile.Frame();
            frame.reserved_01 = reader.readShort();
            frame.reserved_02 = reader.readShort();
            frame.reserved_03 = reader.readShort();
            frame.reserved_04 = reader.readShort();
            frame.offsetX = reader.readShort();
            frame.offsetY = reader.readShort();
            frame.width = reader.readShort();
            frame.height = reader.readShort();
            
            mspFile.frames[i] = frame;
        }
        
        IFileHandler<SfpFile> handler = new SfpFileHandler(mspFile.palettes);
        
        mspFile.files = new SfpFile[mspFile.totalCount];
        for (int i = 0, n = mspFile.totalCount; i < n; ++i) {
            IFullyReader reader1 = new SliceFullReader(reader, reader.position(), size);
            SfpFile sfpFile = handler.load(vfs, path, reader1, size);
            mspFile.files[i] = sfpFile;
        }
        
        return mspFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, MspFile data) throws Exception {
        return false;
    }
}
