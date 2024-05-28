package com.lcl.config.client.config;

import com.lcl.config.client.repository.LclRepository;
import com.lcl.config.client.repository.LclRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 用于封装具体的处理
 * @Author conglongli
 * @date 2024/5/21 23:42
 */
public interface LclConfigService extends LclRepositoryChangeListener {

    static LclConfigService getDefault(ApplicationContext applicationContext, ConfigMeta configMeta) {
        // 从 LclRepository 中获取最新配置
        LclRepository repository = LclRepository.getDefault(configMeta);
        Map<String, String> config = repository.getConfig();
        // 构建 LclConfigService 实现
        LclConfigServiceImpl lclConfigService = new LclConfigServiceImpl(applicationContext, config);
        // 添加监听
        repository.addListener(lclConfigService);
        return lclConfigService;
    }

    String[] getPropertyNames();

    String getProperty(String name);
}
