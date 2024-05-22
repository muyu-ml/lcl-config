package com.lcl.config.client.annotation;

import com.lcl.config.client.config.LclConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Lcl client config entrypoint
 * @Author conglongli
 * @date 2024/5/21 22:46
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({LclConfigRegistrar.class})
public @interface EnableLclConfig {
}
