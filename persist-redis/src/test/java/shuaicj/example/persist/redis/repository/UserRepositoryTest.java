package shuaicj.example.persist.redis.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test spring redis repository.
 *
 * User("shuaicj", new User.Address("china", "cd")) will be stored as:
 *   1. k: users
 *      v: a redis set contains "shuaicj"
 *   2. k: users:shuaicj
 *      v: a redis hash contains
 *                 hk: _class            hv: "shuaicj.hello.persist.redis.repository.User"
 *                 hk: username          hv: "shuaicj"
 *                 hk: address.country   hv: "china"
 *                 hk: address.city      hv: "cd"
 *   3. k: users:shuaicj:phantom v: a copy of value of users:shuaicj
 *      The phantom is created because timeToLive is set and will be deleted
 *      in several minutes after users:shuaicj expires.
 *
 * @author shuaicj 2017/04/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "redis")
public class UserRepositoryTest {

    @Autowired
    UserRepository repo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void test() throws Exception {
        assertThat(repo.findOne("shuaicj")).isNull();
        repo.save(new User("shuaicj", new User.Address("china", "cd")));
        User u = repo.findOne("shuaicj");
        assertThat(u.getUsername()).isEqualTo("shuaicj");
        assertThat(u.getAddress().getCountry()).isEqualTo("china");
        assertThat(u.getAddress().getCity()).isEqualTo("cd");

        TimeUnit.MILLISECONDS.sleep(1100);
        assertThat(repo.findOne("shuaicj")).isNull();
    }
}
