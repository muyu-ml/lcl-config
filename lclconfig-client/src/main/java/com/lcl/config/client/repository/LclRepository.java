package com.lcl.config.client.repository;

import com.lcl.config.client.config.ConfigMeta;

import java.util.Map;

/**
 * interface to get config from remote
 * @Author conglongli
 * @date 2024/5/27 23:26
 */
public interface LclRepository {
    static LclRepository getDefault(ConfigMeta configMeta) {
        return new LclRepositoryImpl(configMeta);
    }

    Map<String, String> getConfig();
}
