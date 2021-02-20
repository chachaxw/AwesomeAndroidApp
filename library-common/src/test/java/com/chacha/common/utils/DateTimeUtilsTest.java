package com.chacha.common.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.utils
 * @date 2/19/21
 * @time 11:12 AM
 *
 * <p>
 *     DateTimeUtils测试用例
 * </p>
 *
 */
public class DateTimeUtilsTest {
    @Test
    public void getYear_isCorrect() {
        assertEquals("2021", DateTimeUtils.getYear(new Date()));
    }
}
