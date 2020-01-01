package com.taoing.authentication.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 一般工具类
 */
@Slf4j
public class GeneralUtils {

    /**
     * 模拟随机长时间调用
     */
    public static void randomlyRunLong(int timeout) {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum == 3) {
            sleep(timeout);
        }
    }

    public static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
