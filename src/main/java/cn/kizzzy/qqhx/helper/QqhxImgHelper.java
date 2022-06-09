package cn.kizzzy.qqhx.helper;

import cn.kizzzy.image.ImageCreator;
import cn.kizzzy.image.creator.BufferedImageCallback;
import cn.kizzzy.qqhx.image.creator.SfpImgCreator;
import cn.kizzzy.qqhx.SfpFile;

import java.awt.image.BufferedImage;

public class QqhxImgHelper {
    
    private static final ImageCreator<SfpFile, BufferedImage> creator_1
        = new SfpImgCreator();
    
    public static BufferedImage toImage(SfpFile item) {
        return creator_1.Create(item, new BufferedImageCallback());
    }
}
