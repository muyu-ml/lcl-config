package com.lcl.config.client.value;

import cn.kimmking.utils.FieldUtils;
import com.lcl.config.client.util.PlaceholderHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * processor spring value
 *  1、扫描所有的 Spring Value，保存起来
 *  2、在配置变更时，更新所有的 Spring Value
 * @Author conglongli
 * @date 2024/5/29 20:07
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    static final PlaceholderHelper helper = PlaceholderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();
    @Setter
    private BeanFactory beanFactory;

    /**
     * 第一步：扫描所有的 Spring Value，保存起来
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 获取所有带有 Value 注解的field
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(
            field -> {
                log.info("[LCLCONFIG] >> find spring value：{}", field);
                // 获取 Value 注解中所有的key
                Value value = field.getAnnotation(Value.class);
                helper.extractPlaceholderKeys(value.value()).forEach(key -> {
                    log.info("[LCLCONFIG] >> find spring value：{}", key);
                    // 将每一个key封装为StringValue并放入VALUE_HOLDER中保存
                    SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
                    VALUE_HOLDER.add(key, springValue);
                });
            });
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

//    /**
//     * 方式一
//     * @param event
//     */
//    @EventListener
//    public void onChange(EnvironmentChangeEvent event) {
//
//    }

    /**
     * 方式二
     * 第二步：在配置变更时，更新所有的 Spring Value
     * @param event
     */
    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info("[LCLCONFIG] >> update spring value for keys: {}", event.getKeys());
        event.getKeys().forEach(key -> {
            log.info("[LCLCONFIG] >> update spring value: {}", key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            if(springValues == null || springValues.isEmpty()){
                return;
            }
            springValues.forEach(springValue -> {
                log.info("[LCLCONFIG] >> update spring value: {} for key: {}", springValue, key);
                try {
                    Object value = helper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory, springValue.getBeanName(), springValue.getPlaceholder());
                    log.info("[LCLCONFIG] >> update spring value: {} for holder: {}", springValue, springValue.getPlaceholder());
                    springValue.getField().setAccessible(true);
                    springValue.getField().set(springValue.getBean(), value);
                } catch (Exception e) {
                    log.error("[LCLCONFIG] >> update spring value error: {}", e);
                }
            });
        });
    }
}
