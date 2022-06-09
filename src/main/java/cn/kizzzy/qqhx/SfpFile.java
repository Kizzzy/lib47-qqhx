package cn.kizzzy.qqhx;

public class SfpFile {
    
    /**
     * 2 bytes
     */
    public short[] magic;
    
    /**
     * 4 bytes
     */
    public int fileSize;
    
    /**
     * 4 bytes
     */
    public int width;
    
    /**
     * 4 bytes
     */
    public int height;
    
    /**
     * 2 bytes
     */
    public int reserved_01;
    
    /**
     * 4 bytes
     */
    public int dataOffset;
    
    /**
     * palette data
     */
    public short[] palettes;
    
    /**
     * pixel data
     */
    public int[] pixelData;
}
