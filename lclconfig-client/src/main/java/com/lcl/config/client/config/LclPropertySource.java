package com.lcl.config.client.config;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * @Author conglongli
 * @date 2024/5/21 23:05
 */
public class LclPropertySource extends EnumerablePropertySource<LclConfigService> {

    public LclPropertySource(String name, LclConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }

}
