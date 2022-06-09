package cn.kizzzy.qqhx;

public class RcfFile {
    
    public short[] magic;
    
    public int count;
    
    public int offset;
    
    public Item1[] item1s;
    
    public static class Item1 {
        
        public long id;
        
        public long offset;
        
        public int reserved_01;
        
        public int count1;
        
        public int reserved_03;
        
        public int count2;
        
        public int count3;
        
        public long[] id2s;
        
        public short[] id1s;
        
        public int[] id3s;
    }
}
