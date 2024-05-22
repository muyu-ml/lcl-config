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
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        if(env.getPropertySources().contains(LCL_PROPERTY_SOURCES)){
            return;
        }
        // 如果配置不存在，通过 http 请求，从 lclconfig-server 获取配置
        Map<String, String> config = new HashMap<>();
        config.put("lcl.a", "dev10000");
        config.put("lcl.b", "b200");
        config.put("lcl.c", "c3000");
        LclConfigService configService = new LclConfigServiceImpl(config);
        // 创建一组配置项
        LclPropertySource propertySource = new LclPropertySource(LCL_PROPERTY_SOURCE, configService);
        // 组合的PropertySource
        CompositePropertySource compositePropertySource = new CompositePropertySource(LCL_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);
        // 将 compositePropertySource 放入环境变量的第一个，compositePropertySource 中存在该变量值，就从compositePropertySource中获取
        env.getPropertySources().addFirst(compositePropertySource);
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
