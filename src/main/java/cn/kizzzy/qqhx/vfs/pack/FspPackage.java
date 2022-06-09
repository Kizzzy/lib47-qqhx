package cn.kizzzy.qqhx.vfs.pack;

import cn.kizzzy.io.IFullyReader;
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
import cn.kizzzy.vfs.IFileLoader;
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
        
        handlerKvs.put(FspItem.class, new FspFileHandler());
        handlerKvs.put(CseFile.class, new CseFileHandler());
        handlerKvs.put(ResFile.class, new ResFileHandler());
        handlerKvs.put(SfpFile.class, new SfpFileHandler());
        handlerKvs.put(MfpFile.class, new MfpFileHandler());
        handlerKvs.put(MspFile.class, new MspFileHandler());
        handlerKvs.put(MapFile.class, new MapFileHandler());
        handlerKvs.put(MaiFile.class, new MaiFileHandler());
    }
    
    @Override
    public boolean exist(String path) {
        return tree.getLeaf(path) != null;
    }
    
    @Override
    protected Object loadImpl(String path, IFileLoader<?> loader) throws Exception {
        Leaf leaf = tree.getLeaf(path);
        if (leaf == null || !(leaf.item instanceof FspItem)) {
            return null;
        }
        
        FspItem file = (FspItem) leaf.item;
        
        String fullPath = Separator.FILE_SEPARATOR.combine(root, dealWithPkgName(file.pack));
        if (file.getSource() == null) {
            file.setSource(new FileStreamable(fullPath));
        }
        
        try (IFullyReader reader = file.OpenStream()) {
            Object obj = loader.load(this, path, reader, file.originSize);
            if (obj instanceof IStreamable) {
                ((IStreamable) obj).setSource(file);
            }
            return obj;
        }
    }
    
    @Override
    protected <T> boolean saveImpl(String path, T data, IFileSaver<T> saver) throws Exception {
        return false;
    }
    
    protected String dealWithPkgName(String pkg) {
        return pkg;
    }
}
