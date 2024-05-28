package com.lcl.config.client.config;

import com.lcl.config.client.repository.LclRepository;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 用于封装具体的处理
 * @Author conglongli
 * @date 2024/5/21 23:42
 */
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
        this.config = event.config();
        // 发送 EnvironmentChangeEvent
        if(!config.isEmpty()){
            System.out.println("[LCLCONFIG] fire an EnvironmentChangeEvent with keys：" + config.keySet());
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
