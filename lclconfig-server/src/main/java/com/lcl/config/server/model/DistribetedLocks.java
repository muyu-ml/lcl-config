package com.lcl.config.server.model;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * distributed locks
 * @Author conglongli
 * @date 2024/5/30 19:40
 */
@Component
@Slf4j
public class DistribetedLocks {

    Connection connection;

    @Getter
    private AtomicBoolean locked = new AtomicBoolean(false);
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    @PostConstruct
    public void init() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        executorService.scheduleAtFixedRate(this :: tryLock, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @Autowired
    DataSource dataSource;

    public boolean lock() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        // 设置连接的事务等待超时时间为 5 秒
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        // 设置加锁语句
        connection.createStatement().execute("select app from locks where id = 1 for update");
        if(locked.get()){
            log.info(" =====>>>> reenter this dist lock.");
        } else {
            log.info(" =====>>>> get a disk lock.");
        }
        return true;
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            log.info(" lock fail .......");
            locked.set(false);
        }
    }

    @PreDestroy
    public void close(){
        try {
            if(connection != null && !connection.isClosed()){
                connection.rollback();
                connection.close();
            }
        } catch (SQLException e) {
            log.warn(" ignore this close exception");
        }
    }
}
