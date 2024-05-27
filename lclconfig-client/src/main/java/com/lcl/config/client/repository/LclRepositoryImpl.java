package com.lcl.config.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.lcl.config.client.config.ConfigMeta;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author conglongli
 * @date 2024/5/27 23:27
 */
@AllArgsConstructor
public class LclRepositoryImpl implements LclRepository{
    ConfigMeta configMeta;
    @Override
    public Map<String, String> getConfig() {
        String listPath = configMeta.getConfigServer() + "/list?app=" + configMeta.getApp()
                + "&env=" + configMeta.getEnv() + "&ns="+configMeta.getNs();
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>(){});
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(c -> resultMap.put(c.getPkey(), c.getPval()));
        return resultMap;
    }
}
