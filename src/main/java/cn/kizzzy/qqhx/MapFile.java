package cn.kizzzy.qqhx;

import java.util.List;

public class MapFile {
    
    public int fileSize;
    
    public int reserved_01;
    
    public int width;
    
    public int height;
    
    public int reserved_04;
    
    public int reserved_05;
    
    // 128
    public short[] bytes_01;
    
    public long[] offsets;
    
    public List<short[]> data;
}
