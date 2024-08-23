package com.sergeineretin.weatherviewer.listener;

import com.sergeineretin.weatherviewer.service.ExpiredSessionsService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class ExpiredSessionListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(
                ExpiredSessionsService::deleteExpiredSessions,
                0, 1,
                TimeUnit.HOURS);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
