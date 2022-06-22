package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.helper.ByteHelper;
import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.io.SeekType;
import cn.kizzzy.qqhx.FspFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

import java.io.IOException;

public class FspFileHandler implements IFileHandler<FspFile> {
    
    private static final short[] MAGIC_FSP = new short[]{
        0x50, 0x41, 0x43, 0x4B
    };
    
    private static final short[] MAGIC_ITEM = new short[]{
        0x42, 0x4C, 0x43, 0x4B
    };
    
    @Override
    public FspFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        reader.setLittleEndian(true);
        
        FspFile fspFile = new FspFile(path);
        fspFile.magic = reader.readUnsignedBytes(4);
        
        if (!ByteHelper.equals(fspFile.magic, MAGIC_FSP)) {
            return null;
        }
        
        fspFile.reserved_01 = reader.readInt();
        fspFile.count = reader.readInt();
        fspFile.headerSize = reader.readInt();
        fspFile.indexOffset = reader.readUnsignedInt();
        fspFile.idxes = new FspFile.Idx[fspFile.count];
        
        reader.seek(fspFile.indexOffset, SeekType.BEGIN);
        
        for (int i = 0; i < fspFile.count; ++i) {
            FspFile.Idx idx = new FspFile.Idx();
            idx.path = reader.readString(64);
            idx.offset = reader.readUnsignedInt();
            
            fspFile.idxes[i] = idx;
        }
        
        fspFile.items = new FspFile.Entry[fspFile.count];
        for (int i = 0; i < fspFile.count; ++i) {
            FspFile.Idx idx = fspFile.idxes[i];
            
            reader.seek(idx.offset, SeekType.BEGIN);
            
            FspFile.Entry item = readItem(reader, path);
            if (item != null) {
                fspFile.items[i] = item;
            }
        }
        
        return fspFile;
    }
    
    private FspFile.Entry readItem(IFullyReader reader, String pack) throws IOException {
        FspFile.Entry entry = new FspFile.Entry(pack);
        entry.magic = reader.readUnsignedBytes(4);
        
        if (!ByteHelper.equals(entry.magic, MAGIC_ITEM)) {
            return null;
        }
        
        entry.reserved_01 = reader.readInt();
        entry.reserved_02 = reader.readInt();
        entry.path = reader.readString(32);
        entry.compressSize = reader.readInt();
        entry.originSize = reader.readInt();
        entry.dataStart = reader.readInt();
        entry.dataEnd = reader.readInt();
        entry.prevOffset = reader.readInt();
        entry.isFile = reader.readInt();
        entry.reserved_10 = reader.readInt();
        return entry;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, FspFile data) throws Exception {
        return false;
    }
}
