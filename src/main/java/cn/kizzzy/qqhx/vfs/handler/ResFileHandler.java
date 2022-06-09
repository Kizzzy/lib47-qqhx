package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.qqhx.ResFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class ResFileHandler implements IFileHandler<ResFile> {
    
    @Override
    public ResFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        byte[] keys = getXorKeys(size);
        
        ResFile resFile = new ResFile();
        resFile.data = reader.readBytes((int) size);
        for (int i = 0, n = resFile.data.length; i < n; ++i) {
            resFile.data[i] ^= keys[i % 8];
        }
        
        return resFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, ResFile data) throws Exception {
        return false;
    }
    
    private byte[] getXorKeys(long size) {
        long keyN = 0xFFFFFFFFL - size - 3;
        keyN = keyN << 32 | keyN;
        
        byte[] keys = new byte[8];
        for (int i = 0; i < 8; ++i) {
            keys[i] = (byte) ((keyN >> (i * 8)) & 0xFF);
        }
        return keys;
    }
}
