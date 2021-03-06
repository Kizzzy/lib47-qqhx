package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.helper.ByteHelper;
import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.io.SeekType;
import cn.kizzzy.qqhx.RcfFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class RcfFileHandler implements IFileHandler<RcfFile> {
    
    private static final short[] MAGIC = new short[]{
        0x42, 0x4F, 0x48, 0x44
    };
    
    @Override
    public RcfFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        reader.setLittleEndian(true);
        
        RcfFile rcfFile = new RcfFile();
        rcfFile.magic = reader.readUnsignedBytes(4);
        if (!ByteHelper.equals(rcfFile.magic, MAGIC)) {
            return null;
        }
        
        rcfFile.count = reader.readInt();
        rcfFile.offset = reader.readInt();
        
        reader.seek(rcfFile.offset, SeekType.BEGIN);
        
        rcfFile.item1s = new RcfFile.Item1[rcfFile.count];
        for (int i = 0, n = rcfFile.item1s.length; i < n; ++i) {
            RcfFile.Item1 item = new RcfFile.Item1();
            item.id = reader.readUnsignedInt();
            item.offset = reader.readUnsignedInt();
            rcfFile.item1s[i] = item;
        }
        
        for (RcfFile.Item1 item1 : rcfFile.item1s) {
            reader.seek(item1.offset, SeekType.BEGIN);
            
            item1.reserved_01 = reader.readUnsignedShort();
            item1.count1 = reader.readUnsignedShort();
            item1.reserved_03 = reader.readUnsignedShort();
            item1.count2 = reader.readUnsignedShort();
            item1.count3 = reader.readUnsignedShort();
            item1.id2s = reader.readUnsignedInts(item1.count2);
            item1.id1s = reader.readUnsignedBytes(item1.count1);
            item1.id3s = reader.readUnsignedShorts(item1.count3);
        }
        
        return rcfFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, RcfFile data) throws Exception {
        return false;
    }
}
