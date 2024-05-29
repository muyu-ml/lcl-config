package com.lcl.config.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @Author conglongli
 * @date 2024/5/29 21:55
 */
@Data
@AllArgsConstructor
public class SpringValue {
    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field field;
}
