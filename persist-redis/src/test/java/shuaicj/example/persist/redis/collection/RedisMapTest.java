package shuaicj.example.persist.redis.collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Test spring RedisMap
 *
 * @author shuaicj 2017/04/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "redis")
public class RedisMapTest {

    private static final String KEY = "my:key:map";

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
    public void map() {
        RedisMap<String, String> map = new DefaultRedisMap<>(KEY, redis);
        map.put("abc", "def");
        map.put("hij", "klm");
        assertThat(map).containsOnly(entry("abc", "def"), entry("hij", "klm"));
        map.remove("abc");
        assertThat(map).containsOnly(entry("hij", "klm"));
    }
}
