package org.mnnu.crowd.api;

import com.mnnu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * redis 的远程调用
 *
 * @author qiaoh
 */
@FeignClient("mnnu-crowd-redis-provider")
public interface RedisRemoteService {
    /**
     * 存放字符串键值对
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/redis/set/kv/remote")
    ResultEntity<?> setKeyValueRemote(@RequestParam("key") String key,
                                      @RequestParam("value") String value);

    /**
     * 带有时间限制的键值对
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit 时间单位枚举类
     * @return
     */
    @RequestMapping("/redis/set/kv/remote/timeout")
    ResultEntity<?> setKeyValueRemoteWithTimeOut(@RequestParam("key") String key,
                                                 @RequestParam("value") String value,
                                                 @RequestParam("time") long time,
                                                 @RequestParam("timeUnit") TimeUnit timeUnit);

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    @RequestMapping("/redis/get/value")
    ResultEntity<?> getValueByKey(@RequestParam("key") String key);

    /**
     * 删除对应的key
     *
     * @param key
     * @return
     */
    @RequestMapping("/redis/remove/key")
    ResultEntity<?> removeKey(@RequestParam("key") String key);
}
