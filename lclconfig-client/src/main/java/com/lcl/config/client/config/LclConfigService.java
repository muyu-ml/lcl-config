package com.lcl.config.client.config;

import com.lcl.config.client.repository.LclRepository;

/**
 * 用于封装具体的处理
 * @Author conglongli
 * @date 2024/5/21 23:42
 */
public interface LclConfigService {

    static LclConfigService getDefault(ConfigMeta configMeta) {
        LclRepository repository = LclRepository.getDefault(configMeta);
        return new LclConfigServiceImpl(repository.getConfig());
    }

    String[] getPropertyNames();

    String getProperty(String name);
}
