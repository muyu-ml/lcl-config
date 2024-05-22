package com.lcl.config.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author conglongli
 * @date 2024/5/21 22:57
 */
@Data
@ConfigurationProperties(prefix = "lcl")
public class LclDemoConfig {

    private String a;
}
