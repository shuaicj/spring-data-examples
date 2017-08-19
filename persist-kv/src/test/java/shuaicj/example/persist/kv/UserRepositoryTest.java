package shuaicj.example.persist.kv;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test UserRepository by spring-data-keyvalue.
 *
 * @author shuaicj 2017/04/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @SpringBootConfiguration
    @EnableMapRepositories
    static class Config {}

    private static final String NAME = "shuaicj";
    private static final String ADDRESS = "china";

    @Autowired
    UserRepository repo;

    @After
    public void tearDown() throws Exception {
        repo.deleteAll();
    }

    @Test
    public void save() throws Exception {
        repo.save(new User(NAME, ADDRESS));
        User u = repo.findOne(NAME);
        assertThat(u.getUsername()).isEqualTo(NAME);
        assertThat(u.getAddress()).isEqualTo(ADDRESS);
    }

    @Test
    public void delete() throws Exception {
        repo.save(new User(NAME, ADDRESS));
        assertThat(repo.findOne(NAME)).isNotNull();
        repo.delete(NAME);
        assertThat(repo.findOne(NAME)).isNull();
    }

    @Test
    public void findByAddressStartsWith() throws Exception {
        repo.save(new User(NAME, ADDRESS));
        repo.save(new User(NAME + "2", "galaxy"));
        List<User> users = repo.findByAddressStartsWith("ch");
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo(NAME);
        assertThat(users.get(0).getAddress()).isEqualTo(ADDRESS);
    }
}