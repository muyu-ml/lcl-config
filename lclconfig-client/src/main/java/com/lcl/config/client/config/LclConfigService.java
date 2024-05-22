package com.lcl.config.client.config;

/**
 * 用于封装具体的处理
 * @Author conglongli
 * @date 2024/5/21 23:42
 */
public interface LclConfigService {

    String[] getPropertyNames();

    String getProperty(String name);
}
