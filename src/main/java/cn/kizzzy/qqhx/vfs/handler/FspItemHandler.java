package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.helper.ByteHelper;
import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.qqhx.FspItem;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class FspItemHandler implements IFileHandler<FspItem> {
    
    private static final short[] MAGIC = new short[]{
        0x42, 0x4C, 0x43, 0x4B
    };
    
    @Override
    public FspItem load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        FspItem fspItem = new FspItem();
        fspItem.path = path;
        fspItem.magic = reader.readUnsignedBytes(4);
        
        if (!ByteHelper.equals(fspItem.magic, MAGIC)) {
            return null;
        }
        
        fspItem.reserved_01 = reader.readIntEx();
        fspItem.reserved_02 = reader.readIntEx();
        fspItem.path = reader.readString(32);
        fspItem.compressSize = reader.readIntEx();
        fspItem.originSize = reader.readIntEx();
        fspItem.dataStart = reader.readIntEx();
        fspItem.dataEnd = reader.readIntEx();
        fspItem.prevOffset = reader.readIntEx();
        fspItem.isFile = reader.readIntEx();
        fspItem.reserved_10 = reader.readIntEx();
        
        return fspItem;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, FspItem data) throws Exception {
        return false;
    }
}
