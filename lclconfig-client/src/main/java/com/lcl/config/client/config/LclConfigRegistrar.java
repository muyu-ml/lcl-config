package com.lcl.config.client.config;

import com.lcl.config.client.value.SpringValueProcessor;
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
        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);
    }

    /**
     * 注册 bean
     * @param registry
     * @param aClass
     */
    private static void registerClass(BeanDefinitionRegistry registry, Class<?> aClass) {
        System.out.println("registry " + aClass.getName());
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames())
                .filter(x -> aClass.getName().equals(x)).findFirst();
        // 防止重复注册
        if(first.isPresent()){
            System.out.println("PropertySourcesProcessor already registered");
            return;
        }
        // 注册 Bean 的 BeanDefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(aClass).getBeanDefinition();
        registry.registerBeanDefinition(aClass.getName(), beanDefinition);
    }
}
