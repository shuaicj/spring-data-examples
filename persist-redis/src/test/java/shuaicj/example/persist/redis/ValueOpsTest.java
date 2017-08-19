package shuaicj.example.persist.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test redis value operations.
 *
 * @author shuaicj 2017/04/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "redis")
public class ValueOpsTest {

    private static final String KEY = "my:key:test";

    @Autowired
    ValueOperations<String, String> ops;

    @Before
    public void setUp() throws Exception {
        ops.getOperations().delete(KEY);
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void testGetAndSet() {
        assertThat(ops.get(KEY)).isNull();
        ops.set(KEY, "shuaicj");
        assertThat(ops.get(KEY)).isEqualTo("shuaicj");
        assertThat(ops.getAndSet(KEY, "shuaicj2")).isEqualTo("shuaicj");
        assertThat(ops.get(KEY)).isEqualTo("shuaicj2");
    }

    @Test
    public void testSetIfAbsent() {
        assertThat(ops.setIfAbsent(KEY, "shuaicj")).isTrue();
        assertThat(ops.get(KEY)).isEqualTo("shuaicj");
        assertThat(ops.setIfAbsent(KEY, "shuaicj2")).isFalse();
        assertThat(ops.get(KEY)).isEqualTo("shuaicj");
    }

    @Test
    public void testIncrement() {
        ops.set(KEY, "10");
        assertThat(ops.increment(KEY, 2)).isEqualTo(12);
        assertThat(ops.get(KEY)).isEqualTo("12");
    }

    @Test
    public void testExpire() throws Exception {
        ops.set(KEY, "shuaicj", 100, TimeUnit.MILLISECONDS);
        TimeUnit.MILLISECONDS.sleep(150);
        assertThat(ops.get(KEY)).isNull();
    }

    @TestConfiguration
    static class Config {

        @Autowired
        StringRedisTemplate redis;

        @Bean
        ValueOperations<String, String> valueOperations() {
            return redis.opsForValue();
        }
    }
}
