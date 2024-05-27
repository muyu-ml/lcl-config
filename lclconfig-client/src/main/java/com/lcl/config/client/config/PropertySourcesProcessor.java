package com.lcl.config.client.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author conglongli
 * @date 2024/5/21 23:51
 */
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

    private final static String LCL_PROPERTY_SOURCE = "LclPropertySource";
    private final static String LCL_PROPERTY_SOURCES = "LclPropertySources";

    Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        if(ENV.getPropertySources().contains(LCL_PROPERTY_SOURCES)){
            return;
        }

        String app = ENV.getProperty("lclconfig.app", "app1");
        String env = ENV.getProperty("lclconfig.env", "dev");
        String ns = ENV.getProperty("lclconfig.ns", "public");
        String configServer = ENV.getProperty("lclconfig.configServer", "http://localhost:9129");
        ConfigMeta configMeta = new ConfigMeta(app, env, ns, configServer);


        // 如果配置不存在，通过 http 请求，从 lclconfig-server 获取配置
//        Map<String, String> config = new HashMap<>();
//        config.put("lcl.a", "dev10000");
//        config.put("lcl.b", "b200");
//        config.put("lcl.c", "c3000");
        // 从 lclconfig-server 获取配置
        LclConfigService configService = LclConfigService.getDefault(configMeta);
        // 创建一组配置项
        LclPropertySource propertySource = new LclPropertySource(LCL_PROPERTY_SOURCE, configService);
        // 组合的PropertySource
        CompositePropertySource compositePropertySource = new CompositePropertySource(LCL_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);
        // 将 compositePropertySource 放入环境变量的第一个，compositePropertySource 中存在该变量值，就从compositePropertySource中获取
        ENV.getPropertySources().addFirst(compositePropertySource);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
