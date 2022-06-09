package cn.kizzzy.qqhx;

public class FspFile {
    
    public short[] magic;
    
    public int reserved_01;
    
    public int count;
    
    public int headerSize;
    
    public long indexOffset;
    
    public String path;
    
    public Idx[] idxes;
    
    public FspItem[] items;
    
    public static class Idx {
        
        public int index;
        
        public String pack;
        
        public String path;
        
        public long offset;
    }
}
