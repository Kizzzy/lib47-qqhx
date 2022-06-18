package cn.kizzzy.qqhx;

import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.SliceFullReader;
import cn.kizzzy.vfs.stream.HolderInputStreamGetter;

public class FspFile {
    
    public short[] magic;
    
    public int reserved_01;
    
    public int count;
    
    public int headerSize;
    
    public long indexOffset;
    
    public Idx[] idxes;
    
    public Entry[] items;
    
    // -------------------- extra field --------------------
    
    public String path;
    
    public FspFile(String path) {
        this.path = path;
    }
    
    public static class Idx {
        
        public int index;
        
        public String pack;
        
        public String path;
        
        public long offset;
    }
    
    public static class Entry extends HolderInputStreamGetter {
        
        public short[] magic;
        
        public int reserved_01;
        
        public int reserved_02;
        
        public String path;
        
        public int compressSize;
        
        public int originSize;
        
        public int dataStart;
        
        public int dataEnd;
        
        public int prevOffset;
        
        public int isFile;
        
        public int reserved_10;
        
        // -------------------- extra field --------------------
        
        public String pack;
        
        public Entry(String pack) {
            this.pack = pack;
        }
        
        @Override
        public IFullyReader getInput() throws Exception {
            if (getSource() == null) {
                throw new NullPointerException("source is null");
            }
            return new SliceFullReader(getSource().getInput(), dataStart, originSize);
        }
    }
}
