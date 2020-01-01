package com.taoing.specialroutes.hystrix;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 自定义装配HystrixPlugins, 加入自定义并发策略
 */
@Configuration
public class ThreadLocalConfiguration {

    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    @Autowired(required = false)
    public void setExistingConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    @PostConstruct
    public void init() {
        // Keeps references of existing Hystrix plugins.
        HystrixPlugins hystrixPlugins = HystrixPlugins.getInstance();
        HystrixEventNotifier eventNotifier = hystrixPlugins.getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = hystrixPlugins.getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = hystrixPlugins.getPropertiesStrategy();
        HystrixCommandExecutionHook commandExecutionHook = hystrixPlugins.getCommandExecutionHook();

        HystrixPlugins.reset();

        hystrixPlugins = HystrixPlugins.getInstance();
        hystrixPlugins.registerConcurrencyStrategy(new ThreadLocalAwareStrategy(existingConcurrencyStrategy));
        hystrixPlugins.registerEventNotifier(eventNotifier);
        hystrixPlugins.registerMetricsPublisher(metricsPublisher);
        hystrixPlugins.registerPropertiesStrategy(propertiesStrategy);
        hystrixPlugins.registerCommandExecutionHook(commandExecutionHook);
    }
}
