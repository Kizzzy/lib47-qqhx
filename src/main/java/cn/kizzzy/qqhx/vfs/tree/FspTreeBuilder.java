package cn.kizzzy.qqhx.vfs.tree;

import cn.kizzzy.qqhx.FspFile;
import cn.kizzzy.qqhx.FspItem;
import cn.kizzzy.vfs.ITree;
import cn.kizzzy.vfs.Separator;
import cn.kizzzy.vfs.tree.IdGenerator;
import cn.kizzzy.vfs.tree.TreeBuilderAdapter;

import java.util.Arrays;

public class FspTreeBuilder extends TreeBuilderAdapter<FspFile, FspItem> {
    
    private final FspFile idxFile;
    
    public FspTreeBuilder(FspFile idxFile) {
        this(idxFile, new IdGenerator());
    }
    
    public FspTreeBuilder(FspFile idxFile, IdGenerator idGenerator) {
        super(Separator.SLASH_SEPARATOR_LOWERCASE, idGenerator);
        this.idxFile = idxFile;
    }
    
    @Override
    public ITree build() {
        return buildImpl(idxFile, new Helper<FspFile, FspItem>() {
            
            @Override
            public String idxPath(FspFile idxFile) {
                return idxFile.path;
            }
            
            @Override
            public Iterable<FspItem> entries(FspFile idxFile) {
                return Arrays.asList(idxFile.items);
            }
            
            @Override
            public String itemPath(FspItem item) {
                return item.path;
            }
        });
    }
}
