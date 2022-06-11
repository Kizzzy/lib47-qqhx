package cn.kizzzy.qqhx.vfs.converter;

import cn.kizzzy.qqhx.CseFile;
import cn.kizzzy.vfs.converter.IConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CseFileToStringConverter implements IConverter<CseFile, String> {
    
    private final Charset charset;
    
    public CseFileToStringConverter() {
        this(StandardCharsets.UTF_8);
    }
    
    public CseFileToStringConverter(Charset charset) {
        this.charset = charset;
    }
    
    @Override
    public String convert(CseFile cseFile) {
        return new String(cseFile.data, charset);
    }
}
