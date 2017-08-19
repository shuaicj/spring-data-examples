package shuaicj.example.persist.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test spring RedisTemplate.
 *
 * @author shuaicj 2017/04/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "redis")
public class RedisTemplateTest {

    private static final String KEY = "my:key:test";

    @Autowired
    RedisTemplate<String, String> redis;

    @Before
    public void setUp() throws Exception {
        redis.delete(KEY);
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void testGetAndSet() {
        assertThat(redis.opsForValue().get(KEY)).isNull();
        redis.opsForValue().set(KEY, "shuaicj");
        assertThat(redis.opsForValue().get(KEY)).isEqualTo("shuaicj");
        assertThat(redis.opsForValue().getAndSet(KEY, "shuaicj2")).isEqualTo("shuaicj");
        assertThat(redis.opsForValue().get(KEY)).isEqualTo("shuaicj2");
    }
}
