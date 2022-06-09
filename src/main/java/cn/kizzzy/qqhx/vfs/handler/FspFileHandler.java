package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.helper.ByteHelper;
import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.io.SeekType;
import cn.kizzzy.qqhx.FspFile;
import cn.kizzzy.qqhx.FspItem;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class FspFileHandler implements IFileHandler<FspFile> {
    
    private static final short[] MAGIC = new short[]{
        0x50, 0x41, 0x43, 0x4B
    };
    
    @Override
    public FspFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        FspFile fspFile = new FspFile();
        fspFile.path = path;
        fspFile.magic = reader.readUnsignedBytes(4);
        
        if (!ByteHelper.equals(fspFile.magic, MAGIC)) {
            return null;
        }
        
        fspFile.reserved_01 = reader.readIntEx();
        fspFile.count = reader.readIntEx();
        fspFile.headerSize = reader.readIntEx();
        fspFile.indexOffset = reader.readUnsignedIntEx();
        fspFile.idxes = new FspFile.Idx[fspFile.count];
        
        reader.seek(fspFile.indexOffset, SeekType.BEGIN);
        
        for (int i = 0; i < fspFile.count; ++i) {
            FspFile.Idx idx = new FspFile.Idx();
            idx.index = i;
            idx.pack = path;
            
            idx.path = reader.readString(64);
            idx.offset = reader.readUnsignedIntEx();
            
            fspFile.idxes[i] = idx;
        }
        
        IFileHandler<FspItem> handler = vfs.getHandler(FspItem.class);
        if (handler == null) {
            return fspFile;
        }
        
        fspFile.items = new FspItem[fspFile.count];
        for (int i = 0; i < fspFile.count; ++i) {
            FspFile.Idx idx = fspFile.idxes[i];
            
            reader.seek(idx.offset, SeekType.BEGIN);
            
            FspItem item = handler.load(vfs, path, reader, size);
            if (item != null) {
                item.pack = path;
                
                fspFile.items[i] = item;
            }
        }
        
        return fspFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, FspFile data) throws Exception {
        return false;
    }
}
