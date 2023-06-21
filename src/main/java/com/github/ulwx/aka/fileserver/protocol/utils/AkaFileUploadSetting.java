package com.github.ulwx.aka.fileserver.protocol.utils;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix =  "aka.file-server" )
public class AkaFileUploadSetting {

    private String uploadDir;
    private String ossHttpPrefix;
    private String httpPrefix;
    private String ds;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getOssHttpPrefix() {
        return ossHttpPrefix;
    }

    public void setOssHttpPrefix(String ossHttpPrefix) {
        this.ossHttpPrefix = ossHttpPrefix;
    }

    public String getHttpPrefix() {
        return httpPrefix;
    }

    public void setHttpPrefix(String httpPrefix) {
        this.httpPrefix = httpPrefix;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }
}
