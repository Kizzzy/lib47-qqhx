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
        MspFile mspFile = new MspFile();
        mspFile.magic = reader.readUnsignedBytes(4);
        
        if (!ByteHelper.equals(mspFile.magic, MAGIC)) {
            return null;
        }
        
        mspFile.fileOffset = reader.readIntEx();
        mspFile.reserved_01 = reader.readIntEx();
        mspFile.reserved_02 = reader.readIntEx();
        mspFile.paletteSize = reader.readUnsignedShortEx();
        mspFile.paletteCount = reader.readUnsignedShortEx();
        mspFile.reserved_04 = reader.readIntEx();
        mspFile.unitCount = reader.readIntEx();
        mspFile.totalCount = reader.readIntEx();
        
        mspFile.palettes = new short[mspFile.paletteCount];
        for (int i = 0, n = mspFile.paletteCount; i < n; ++i) {
            mspFile.palettes[i] = reader.readShortEx();
        }
        
        mspFile.unknowns = new int[96];
        for (int i = 0, n = mspFile.unknowns.length; i < n; ++i) {
            mspFile.unknowns[i] = reader.readIntEx();
        }
        
        mspFile.frames = new MspFile.Frame[mspFile.totalCount];
        for (int i = 0, n = mspFile.totalCount; i < n; ++i) {
            MspFile.Frame frame = new MspFile.Frame();
            frame.reserved_01 = reader.readShortEx();
            frame.reserved_02 = reader.readShortEx();
            frame.reserved_03 = reader.readShortEx();
            frame.reserved_04 = reader.readShortEx();
            frame.offsetX = reader.readShortEx();
            frame.offsetY = reader.readShortEx();
            frame.width = reader.readShortEx();
            frame.height = reader.readShortEx();
            
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
