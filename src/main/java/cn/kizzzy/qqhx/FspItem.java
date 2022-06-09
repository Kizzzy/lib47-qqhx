package cn.kizzzy.qqhx;

import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.SliceFullReader;
import cn.kizzzy.vfs.IStreamable;

public class FspItem implements IStreamable {
    
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
    
    public String pack;
    private IStreamable source;
    
    @Override
    public IStreamable getSource() {
        return source;
    }
    
    @Override
    public void setSource(IStreamable source) {
        this.source = source;
    }
    
    @Override
    public IFullyReader OpenStream() throws Exception {
        if (getSource() == null) {
            throw new NullPointerException("source is null");
        }
        return new SliceFullReader(getSource().OpenStream(), dataStart, originSize);
    }
}
