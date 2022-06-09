package cn.kizzzy.qqhx.image.creator;

import cn.kizzzy.image.creator.ImageCreatorAdapter;
import cn.kizzzy.qqhx.SfpFile;

import java.awt.image.BufferedImage;

public class SfpImgCreator extends ImageCreatorAdapter<SfpFile, BufferedImage> {
    
    public SfpImgCreator() {
        super(null);
    }
    
    @Override
    protected BufferedImage CreateImpl(SfpFile item, Callback<BufferedImage> callback) throws Exception {
        if (item != null) {
            return callback.invoke(item.pixelData, item.width, item.height);
        }
        return null;
    }
}
