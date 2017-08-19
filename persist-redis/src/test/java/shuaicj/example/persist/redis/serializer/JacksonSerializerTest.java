package shuaicj.example.persist.redis.serializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test spring Jackson2JsonRedisSerializer.
 *
 * User("shuaicj", new User.Address("china", "cd")) will be stored as:
 *   k - my:key:jacksonser
 *   v - {username:"shuaicj",address:{country:"china",city:"cd"}}
 *
 * @author shuaicj 2017/04/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "redis")
public class JacksonSerializerTest {

    private static final String KEY = "my:key:jacksonser";

    @Autowired
    RedisTemplate<String, User> redis;

    @Before
    public void setUp() throws Exception {
        redis.delete(KEY);
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void test() {
        redis.opsForValue().set(KEY, new User("shuaicj", new User.Address("china", "cd")));
        User u = redis.opsForValue().get(KEY);
        assertThat(u.getUsername()).isEqualTo("shuaicj");
        assertThat(u.getAddress().getCountry()).isEqualTo("china");
        assertThat(u.getAddress().getCity()).isEqualTo("cd");
    }

    @TestConfiguration
    static class Config {

        @Bean
        public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory cf) {
            RedisTemplate<String, User> redis = new RedisTemplate<>();
            redis.setConnectionFactory(cf);
            redis.setKeySerializer(new StringRedisSerializer());
            redis.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
            return redis;
        }
    }
}
