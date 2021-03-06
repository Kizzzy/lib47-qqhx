package cn.kizzzy.qqhx.vfs.pack;

import cn.kizzzy.qqhx.CseFile;
import cn.kizzzy.qqhx.FspFile;
import cn.kizzzy.qqhx.MaiFile;
import cn.kizzzy.qqhx.MapFile;
import cn.kizzzy.qqhx.MfpFile;
import cn.kizzzy.qqhx.MspFile;
import cn.kizzzy.qqhx.ResFile;
import cn.kizzzy.qqhx.SfpFile;
import cn.kizzzy.qqhx.vfs.handler.CseFileHandler;
import cn.kizzzy.qqhx.vfs.handler.FspFileHandler;
import cn.kizzzy.qqhx.vfs.handler.MaiFileHandler;
import cn.kizzzy.qqhx.vfs.handler.MapFileHandler;
import cn.kizzzy.qqhx.vfs.handler.MfpFileHandler;
import cn.kizzzy.qqhx.vfs.handler.MspFileHandler;
import cn.kizzzy.qqhx.vfs.handler.ResFileHandler;
import cn.kizzzy.qqhx.vfs.handler.SfpFileHandler;
import cn.kizzzy.vfs.IStreamGetterFactory;
import cn.kizzzy.vfs.ITree;
import cn.kizzzy.vfs.pack.LeafPackage;
import cn.kizzzy.vfs.stream.FileStreamGetterFactory;

public class FspPackage extends LeafPackage<FspFile.Entry> {
    
    public FspPackage(String root, ITree tree) {
        this(tree, new FileStreamGetterFactory(root));
    }
    
    public FspPackage(ITree tree, IStreamGetterFactory factory) {
        super(tree, factory, FspFile.Entry.class, entry -> entry.pack);
    }
    
    @Override
    protected void initDefaultHandler() {
        super.initDefaultHandler();
        
        addHandler(FspFile.Entry.class, new FspFileHandler());
        addHandler(CseFile.class, new CseFileHandler());
        addHandler(ResFile.class, new ResFileHandler());
        addHandler(SfpFile.class, new SfpFileHandler());
        addHandler(MfpFile.class, new MfpFileHandler());
        addHandler(MspFile.class, new MspFileHandler());
        addHandler(MapFile.class, new MapFileHandler());
        addHandler(MaiFile.class, new MaiFileHandler());
    }
}
