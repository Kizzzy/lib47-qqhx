package cn.kizzzy.qqhx.vfs.handler;

import cn.kizzzy.io.IFullyReader;
import cn.kizzzy.io.IFullyWriter;
import cn.kizzzy.qqhx.MaiFile;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class MaiFileHandler implements IFileHandler<MaiFile> {
    
    @Override
    public MaiFile load(IPackage vfs, String path, IFullyReader reader, long size) throws Exception {
        MaiFile maiFile = new MaiFile();
        maiFile.width = reader.readShortEx();
        maiFile.height = reader.readShortEx();
        maiFile.data = new short[maiFile.height][maiFile.width];
        for (int i = 0; i < maiFile.height; ++i) {
            for (int j = 0; j < maiFile.width; ++j) {
                maiFile.data[i][j] = reader.readShortEx();
            }
        }
        return maiFile;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, IFullyWriter writer, MaiFile data) throws Exception {
        return false;
    }
}
