package com.lcl.config.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.lcl.config.client.config.ConfigMeta;
import com.alibaba.fastjson.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author conglongli
 * @date 2024/5/27 23:27
 */
public class LclRepositoryImpl implements LclRepository {
    ConfigMeta configMeta;

    Map<String, Long> versionMap = new HashMap<>();
    Map<String, Map<String, String>> configMap = new HashMap<>();

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    List<LclRepositoryChangeListener> listeners = new ArrayList<>();

    public LclRepositoryImpl(ConfigMeta configMeta){
        this.configMeta = configMeta;
        executor.scheduleWithFixedDelay(this :: heartbeat, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void addListener(LclRepositoryChangeListener listener) {
        listeners.add(listener);
    }

    private void heartbeat() {
        String versionPath = configMeta.versionPath();
        Long version = HttpUtils.httpGet(versionPath, new TypeReference<Long>(){});
        String key = configMeta.genkey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        if(version > oldVersion) {
            System.out.println("[LCLCONFIG] need update new configs：current version [" + oldVersion+ "], new version [" + version + "]");
            versionMap.put(key, version);
            Map<String, String> newConfigs = findAll();
            configMap.put(key, newConfigs);
            // 发送 EnvironmentChangeEvent
            listeners.forEach(l -> l.onChange(new LclRepositoryChangeListener.ChangeEvent(configMeta, newConfigs)));
        }
    }

    @Override
    public Map<String, String> getConfig() {
        String key = configMeta.genkey();
        if(configMap.containsKey(key)) {
            return configMap.get(key);
        }
        return findAll();
    }

    @NotNull
    private Map<String, String> findAll() {
        String listPath = configMeta.listPath();
        System.out.println("[LCLCONFIG] list all configs server...");
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>(){});
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(c -> resultMap.put(c.getPkey(), c.getPval()));
        return resultMap;
    }


}
