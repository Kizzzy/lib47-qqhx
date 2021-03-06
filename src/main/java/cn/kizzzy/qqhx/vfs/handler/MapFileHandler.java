package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.io.SeekType;
import cn.kizzzy.qqhx.MapFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

import java.util.LinkedList;

public class MapFileHandler implements IFileHandler<MapFile> {
    
    @Override
    public MapFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        reader.setLittleEndian(true);
        
        MapFile mapFile = new MapFile();
        mapFile.fileSize = reader.readInt();
        mapFile.reserved_01 = reader.readInt();
        mapFile.width = reader.readInt();
        mapFile.height = reader.readInt();
        mapFile.bytes_01 = reader.readUnsignedBytes(128);
        
        reader.seek(144, SeekType.BEGIN);
        long index = reader.readUnsignedInt();
        
        reader.seek(144, SeekType.BEGIN);
        mapFile.offsets = reader.readUnsignedInts((int) ((index - 144) / 4));
        
        mapFile.data = new LinkedList<>();
        for (int i = 0, n = mapFile.offsets.length; i < n; ++i) {
            long start = mapFile.offsets[i];
            long end = i == n - 1 ? size : mapFile.offsets[i + 1];
            
            reader.seek(start, SeekType.BEGIN);
            short[] _d = reader.readUnsignedBytes((int) (end - start));
            mapFile.data.add(_d);
        }
        
        return mapFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, MapFile data) throws Exception {
        return false;
    }
}
