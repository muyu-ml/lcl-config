package com.lcl.config.client.config;

import java.util.Map;

/**
 * 用于封装具体的处理
 * @Author conglongli
 * @date 2024/5/21 23:42
 */
public class LclConfigServiceImpl implements LclConfigService {

    Map<String, String> config;

    public LclConfigServiceImpl(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }
}
