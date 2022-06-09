package cn.kizzzy.qqhx;

public class MspFile {
    
    /**
     * 4 bytes
     */
    public short[] magic;
    
    /**
     * 4 bytes
     */
    public int fileOffset;
    
    /**
     * 4 bytes
     */
    public int reserved_01;
    
    /**
     * 4 bytes
     */
    public int reserved_02;
    
    /**
     * 2 bytes
     */
    public int paletteSize;
    
    /**
     * 2 bytes
     */
    public int paletteCount;
    
    /**
     * 4 bytes
     */
    public int reserved_04;
    
    /**
     * 4 bytes
     */
    public int unitCount;
    
    /**
     * 4 bytes
     */
    public int totalCount;
    
    /**
     *
     */
    public int[] unknowns;
    
    /**
     *
     */
    public short[] palettes;
    
    /**
     *
     */
    public Frame[] frames;
    
    /**
     * pixel data
     */
    public SfpFile[] files;
    
    public static class Frame {
        
        public int reserved_01;
        
        public int reserved_02;
        
        public int reserved_03;
        
        public int reserved_04;
        
        public int offsetX;
        
        public int offsetY;
        
        public int width;
        
        public int height;
    }
}
