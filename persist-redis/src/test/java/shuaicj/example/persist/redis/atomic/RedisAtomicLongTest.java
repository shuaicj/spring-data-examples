package shuaicj.example.persist.redis.atomic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test spring RedisAtomicLong
 *
 * @author shuaicj 2017/04/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "redis")
public class RedisAtomicLongTest {

    private static final String KEY = "my:key:atomic";

    @Autowired
    StringRedisTemplate redis;

    @Before
    public void setUp() throws Exception {
        redis.delete(KEY);
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void emptyInit() {
        RedisAtomicLong counter = new RedisAtomicLong(KEY, redis.getConnectionFactory());
        assertThat(counter.get()).isEqualTo(0);
        assertThat(counter.incrementAndGet()).isEqualTo(1);
        assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    public void specifiedInit() {
        RedisAtomicLong counter = new RedisAtomicLong(KEY, redis.getConnectionFactory(), 1);
        assertThat(counter.get()).isEqualTo(1);
        assertThat(counter.incrementAndGet()).isEqualTo(2);
        assertThat(counter.get()).isEqualTo(2);
    }
}
