package cn.kizzzy.qqhx.vfs.converter;

import cn.kizzzy.qqhx.CseFile;
import cn.kizzzy.vfs.converter.IConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringToCseFileConverter implements IConverter<String, CseFile> {
    
    private final Charset charset;
    
    public StringToCseFileConverter() {
        this(StandardCharsets.UTF_8);
    }
    
    public StringToCseFileConverter(Charset charset) {
        this.charset = charset;
    }
    
    @Override
    public CseFile convert(String s) {
        CseFile cseFile = new CseFile();
        cseFile.data = s.getBytes(charset);
        return cseFile;
    }
}
