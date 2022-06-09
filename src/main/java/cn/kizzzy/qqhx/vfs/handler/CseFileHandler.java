package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.qqhx.CseFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class CseFileHandler implements IFileHandler<CseFile> {
    
    private static final String KEYS = "0E77FB88250C5C35A2FA7699E7BC23D9396E010C28ECE2B2500AFBFDB988E040D";
    
    @Override
    public CseFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        CseFile cseFile = new CseFile();
        cseFile.data = reader.readBytes((int) size);
        for (int i = 0, n = cseFile.data.length, key = getXorKey((int) size); i < n; ++i) {
            cseFile.data[i] ^= key;
        }
        
        return cseFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, CseFile data) throws Exception {
        return false;
    }
    
    private int getXorKey(int size) {
        int index = (size + 3) & 0x1F;
        index = KEYS.length() - (index + 1) * 2;
        String keyStr = KEYS.substring(index, index + 2);
        return Integer.parseInt(keyStr, 16);
    }
}
