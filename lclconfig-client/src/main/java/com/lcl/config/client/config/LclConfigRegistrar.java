package com.lcl.config.client.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author conglongli
 * @date 2024/5/22 00:12
 */
public class LclConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println("registry PropertySourcesProcessor");
        // 防止重复注册
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(x -> PropertySourcesProcessor.class.getName().equals(x)).findFirst();
        if(first.isPresent()){
            System.out.println("PropertySourcesProcessor already registered");
            return;
        }
        // 注册 PropertySourcesProcessor 的 BeanDefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition(PropertySourcesProcessor.class.getName(), beanDefinition);
    }
}
