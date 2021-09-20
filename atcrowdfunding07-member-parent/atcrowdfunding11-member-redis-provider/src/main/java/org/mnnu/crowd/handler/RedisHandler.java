package org.mnnu.crowd.handler;

import com.mnnu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author qiaoh
 */
@RestController
public class RedisHandler {
    @Autowired
    StringRedisTemplate redisTemplate;

    @RequestMapping("/redis/set/kv/remote")
    public ResultEntity<?> setKeyValueRemote(@RequestParam("key") String key,
                                             @RequestParam("value") String value) {
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(key, value);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/redis/set/kv/remote/timeout")
    public ResultEntity<?> setKeyValueRemoteWithTimeOut(@RequestParam("key") String key,
                                                        @RequestParam("value") String value,
                                                        @RequestParam("time") long time,
                                                        @RequestParam("timeUnit") TimeUnit timeUnit) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        try {
            ops.set(key, value, time, timeUnit);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/redis/get/value")
    public ResultEntity<?> getValueByKey(@RequestParam("key") String key) {
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            String val = ops.get(key);
            return ResultEntity.successWithData(val);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/redis/remove/key")
    public ResultEntity<?> removeKey(@RequestParam("key") String key) {
        try {
            redisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
