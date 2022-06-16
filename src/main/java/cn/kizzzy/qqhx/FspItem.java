package cn.kizzzy.qqhx;

import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.SliceFullReader;
import cn.kizzzy.vfs.IInputStreamGetter;

public class FspItem implements IInputStreamGetter {
    
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
    private IInputStreamGetter source;
    
    @Override
    public IInputStreamGetter getSource() {
        return source;
    }
    
    @Override
    public void setSource(IInputStreamGetter source) {
        this.source = source;
    }
    
    @Override
    public IFullyReader getInput() throws Exception {
        if (getSource() == null) {
            throw new NullPointerException("source is null");
        }
        return new SliceFullReader(getSource().getInput(), dataStart, originSize);
    }
}
