package us.sep.biz.exam.common;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final long expireTime = 10L;

    public static ThreadLocal<String> holder = new ThreadLocal<>();

    public Boolean setConcurrentLock(String key) throws InterruptedException {
        stringRedisTemplate.setEnableTransactionSupport(true);
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        while (!ops.setIfAbsent(key, JSON.toJSONString(String.valueOf(System.currentTimeMillis() + expireTime)))) {
            stringRedisTemplate.watch(key);
            Long expire = Long.parseLong(ops.get(key));
            if (expire != null && expire < System.currentTimeMillis()) {
                stringRedisTemplate.multi();
                String Expire = String.valueOf(System.currentTimeMillis() + expireTime);
                Long oldExpire = Long.parseLong(ops.getAndSet(key,Expire));
                if (stringRedisTemplate.exec() != null && oldExpire != null && oldExpire < System.currentTimeMillis()) {
                    break;
                }
            } else {
                stringRedisTemplate.unwatch();
            }
            TimeUnit.MILLISECONDS.sleep(3);
        }
        holder.set(ops.get(key));
        return true;
    }

    public void deleteConcurrentLock(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String expire = ops.get(key);
        if(expire.equals(holder.get())){
            stringRedisTemplate.delete(key);
        }
        holder.remove();
    }
}
