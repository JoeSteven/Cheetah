package com.joey.cheetah.core.async.schedulers;

/**
 * Description:
 * author:Joey
 * date:2018/9/13
 */
public class SchedulersHelper {
    public static <T> SchedulersIO2Main<T> io_main() {
        return new SchedulersIO2Main<>();
    }
}
