package shuaicj.example.persist.redis.collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Deque;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test spring RedisList
 *
 * @author shuaicj 2017/04/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "redis")
public class RedisListTest {

    private static final String KEY = "my:key:list";

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
    public void list() {
        RedisList<String> list = new DefaultRedisList<>(KEY, redis);
        list.add("abc");
        list.add("def");
        assertThat(list).containsExactly("abc", "def");
        list.remove("abc");
        assertThat(list).containsExactly("def");
    }

    @Test
    public void queue() {
        Queue<String> q = new DefaultRedisList<>(KEY, redis);
        q.offer("abc");
        q.offer("def");
        assertThat(q.poll()).isEqualTo("abc");
        assertThat(q.poll()).isEqualTo("def");
        assertThat(q.poll()).isNull();
    }

    @Test
    public void deque() {
        Deque<String> q = new DefaultRedisList<>(KEY, redis);
        q.offerFirst("abc");
        q.offerFirst("def");
        assertThat(q.pollLast()).isEqualTo("abc");
        assertThat(q.pollLast()).isEqualTo("def");
        assertThat(q.pollLast()).isNull();
    }
}
