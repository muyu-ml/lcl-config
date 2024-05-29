package com.lcl.config.client.config;

import com.lcl.config.client.repository.LclRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用于封装具体的处理
 * @Author conglongli
 * @date 2024/5/21 23:42
 */
@Slf4j
public class LclConfigServiceImpl implements LclConfigService {

    Map<String, String> config;
    ApplicationContext applicationContext;

    public LclConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.config = config;
        this.applicationContext = applicationContext;
    }

    @Override
    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }


    @Override
    public void onChange(ChangeEvent event) {
        // 对比新旧版本中变化的配置
        Set<String> keys = calcChangeKeys(this.config, event.config());
        if(keys.isEmpty()) {
            log.info("[LCLCONFIG] calcChangeKeys return empty, ignore update");
            return;
        }
        // 更新配置项
        this.config = event.config();
        // 发送 EnvironmentChangeEvent（只发布变更的配置项）
        if(!config.isEmpty()){
            log.info("[LCLCONFIG] fire an EnvironmentChangeEvent with keys：{}", keys);
            applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
        }
    }

    /**
     * 对比新旧版本中变化的配置
     * @param oldConfig
     * @param newConfig
     * @return
     */
    private Set<String> calcChangeKeys(Map<String, String> oldConfig, Map<String, String> newConfig) {
        if(oldConfig.isEmpty()){
            return newConfig.keySet();
        }
        if(newConfig.isEmpty()){
            return oldConfig.keySet();
        }
        Set<String> news = newConfig.keySet().stream().filter(key -> !newConfig.get(key).equals(oldConfig.get(key))).collect(Collectors.toSet());
        oldConfig.keySet().stream().filter(key -> !newConfig.containsKey(key)).forEach(news :: add);
        return news;
    }
}
