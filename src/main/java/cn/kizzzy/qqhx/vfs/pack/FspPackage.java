package cn.kizzzy.qqhx.vfs.pack;

import cn.kizzzy.qqhx.CseFile;
import cn.kizzzy.qqhx.FspItem;
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
import cn.kizzzy.vfs.IFileSaver;
import cn.kizzzy.vfs.IStreamable;
import cn.kizzzy.vfs.ITree;
import cn.kizzzy.vfs.Separator;
import cn.kizzzy.vfs.pack.AbstractPackage;
import cn.kizzzy.vfs.streamable.FileStreamable;
import cn.kizzzy.vfs.tree.Leaf;

public class FspPackage extends AbstractPackage {
    
    public FspPackage(String root, ITree tree) {
        super(root, tree);
    }
    
    @Override
    protected void initDefaultHandler() {
        super.initDefaultHandler();
        
        addHandler(FspItem.class, new FspFileHandler());
        addHandler(CseFile.class, new CseFileHandler());
        addHandler(ResFile.class, new ResFileHandler());
        addHandler(SfpFile.class, new SfpFileHandler());
        addHandler(MfpFile.class, new MfpFileHandler());
        addHandler(MspFile.class, new MspFileHandler());
        addHandler(MapFile.class, new MapFileHandler());
        addHandler(MaiFile.class, new MaiFileHandler());
    }
    
    @Override
    public boolean exist(String path) {
        return tree.getLeaf(path) != null;
    }
    
    @Override
    protected IStreamable getStreamableImpl(String path) {
        Leaf leaf = tree.getLeaf(path);
        if (leaf == null || !(leaf.item instanceof FspItem)) {
            return null;
        }
        
        FspItem fspItem = (FspItem) leaf.item;
        
        String fullPath = Separator.FILE_SEPARATOR.combine(root, dealWithPkgName(fspItem.pack));
        if (fspItem.getSource() == null) {
            fspItem.setSource(new FileStreamable(fullPath));
        }
        
        return fspItem;
    }
    
    @Override
    protected <T> boolean saveImpl(String path, T data, IFileSaver<T> saver) throws Exception {
        return false;
    }
    
    protected String dealWithPkgName(String pkg) {
        return pkg;
    }
}
