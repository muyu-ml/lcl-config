package com.lcl.config.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author conglongli
 * @date 2024/5/27 23:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigMeta {
    private String app;
    private String env;
    private String ns;
    private String configServer;

    public String genkey(){
        return this.getApp() + "_" + this.getEnv() + "_" + this.getNs();
    }

    public String listPath(){
        return path("list");
    }

    public String versionPath(){
        return path("version");
    }

    private String path(String context){
        return this.getConfigServer() + "/" + context + "?app=" + this.getApp()
                + "&env=" + this.getEnv() + "&ns="+this.getNs();
    }
}
