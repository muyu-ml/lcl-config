package com.lcl.config.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author conglongli
 * @date 2024/4/28 14:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configs {
    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pval;
}
