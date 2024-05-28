package com.lcl.config.client.repository;

import com.lcl.config.client.config.ConfigMeta;

import java.util.Map;

/**
 * @Author conglongli
 * @date 2024/5/29 01:56
 */
public interface LclRepositoryChangeListener {
    void onChange(LclRepositoryChangeListener.ChangeEvent event);

    record ChangeEvent(ConfigMeta configMeta, Map<String, String> config) {}
}
