package shuaicj.example.persist.jpa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test UserRepository.
 *
 * @author shuaicj 2017/01/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = {"h2", "mysql"})
public class UserRepositoryTest {

    private static final String NAME = "shuaicj";
    private static final String PASS = "pass123";
    private static final String NAME2 = "newuser";
    private static final String PASS2 = "newpass";

    @Autowired
    private UserRepository repo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void save() throws Exception {
        repo.save(new User(NAME, PASS));
        User u = repo.findByUsername(NAME);
        assertThat(u).isNotNull();
        assertThat(u.getUsername()).isEqualTo(NAME);
        assertThat(u.getPassword()).isEqualTo(PASS);
        assertThat(u.getCreatedTime()).isNotNull();
        assertThat(u.getCreatedTime()).isEqualTo(u.getUpdatedTime());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveDuplicate() throws Exception {
        repo.save(new User(NAME, PASS));
        repo.save(new User(NAME, PASS));
    }

    @Test
    public void saveReturnsId() throws Exception {
        User u1 = repo.save(new User(NAME, PASS));
        User u2 = repo.save(new User(NAME2, PASS2));
        assertThat(u2.getId()).isEqualTo(u1.getId() + 1);
    }

    @Test
    public void findByUsername() throws Exception {
        repo.save(new User(NAME, PASS));
        User user = repo.findByUsername(NAME);
        assertThat(user.getPassword()).isEqualTo(PASS);
    }

    @Test
    public void findByUsernameNotExists() throws Exception {
        assertThat(repo.findByUsername(NAME)).isNull();
    }

    @Test
    public void deleteByUsername() throws Exception {
        repo.save(new User(NAME, PASS));
        assertThat(repo.findByUsername(NAME)).isNotNull();
        assertThat(repo.deleteByUsername(NAME)).isEqualTo(1);
        assertThat(repo.findByUsername(NAME)).isNull();
    }

    @Test
    public void deleteByUsernameNotExists() throws Exception {
        assertThat(repo.findByUsername(NAME)).isNull();
        assertThat(repo.deleteByUsername(NAME)).isEqualTo(0);
        assertThat(repo.findByUsername(NAME)).isNull();
    }

    @Test
    public void time() throws Exception {
        repo.save(new User(NAME, PASS));
        User u = repo.findByUsername(NAME);
        Date createdTime = u.getCreatedTime();
        Date updatedTime = u.getUpdatedTime();
        assertThat(createdTime).isNotNull();
        assertThat(updatedTime).isEqualTo(createdTime);

        Thread.sleep(1000);

        u.setPassword(PASS2);
        u = repo.save(u);
        assertThat(u.getCreatedTime()).isEqualTo(createdTime);
        assertThat(u.getUpdatedTime()).isAfter(createdTime);
    }

    @Test
    public void pagination() throws Exception {
        int total = 5, size = 2;
        for (int i = 0; i < total; i++) {
            repo.save(new User(NAME + i, PASS));
        }

        Page<User> users = repo.findAll(new PageRequest(0, size));
        assertThat(users.getTotalElements()).isEqualTo(total);
        assertThat(users.getTotalPages()).isEqualTo(total / size + 1);
        assertThat(users.getNumberOfElements()).isEqualTo(size);
        assertThat(users.getNumber()).isEqualTo(0);
        assertThat(users.getContent().get(0).getUsername()).isEqualTo(NAME + "0");
        assertThat(users.getContent().get(1).getUsername()).isEqualTo(NAME + "1");

        users = repo.findAll(users.nextPageable());
        assertThat(users.getTotalElements()).isEqualTo(total);
        assertThat(users.getTotalPages()).isEqualTo(total / size + 1);
        assertThat(users.getNumberOfElements()).isEqualTo(size);
        assertThat(users.getNumber()).isEqualTo(1);
        assertThat(users.getContent().get(0).getUsername()).isEqualTo(NAME + "2");
        assertThat(users.getContent().get(1).getUsername()).isEqualTo(NAME + "3");

        users = repo.findAll(users.nextPageable());
        assertThat(users.getTotalElements()).isEqualTo(total);
        assertThat(users.getTotalPages()).isEqualTo(total / size + 1);
        assertThat(users.getNumberOfElements()).isEqualTo(1);
        assertThat(users.getNumber()).isEqualTo(2);
        assertThat(users.hasNext()).isFalse();
        assertThat(users.getContent().get(0).getUsername()).isEqualTo(NAME + "4");

        for (int i = 0; i < total; i++) {
            repo.deleteByUsername(NAME + i);
        }
    }

    @Test
    public void ignoreCase() throws Exception {
        repo.save(new User(NAME, PASS));
        repo.save(new User(NAME2, PASS.toUpperCase()));
        assertThat(repo.findByPasswordIgnoreCase(PASS)).hasSize(2);
    }

    @Test
    public void orderBy() throws Exception {
        int total = 3;
        for (int i = 0; i < total; i++) {
            repo.save(new User(NAME + i, PASS));
        }

        List<User> users = repo.findByPasswordOrderByUsernameDesc(PASS);
        assertThat(users).hasSize(total);
        for (int i = 0; i < total; i++) {
            assertThat(users.get(i).getUsername()).isEqualTo(NAME + (total - i - 1));
        }

        for (int i = 0; i < total; i++) {
            repo.deleteByUsername(NAME + i);
        }
    }

    @Test
    public void count() throws Exception {
        int total = 3;
        for (int i = 0; i < total; i++) {
            repo.save(new User(NAME + i, PASS));
        }
        assertThat(repo.countByPassword(PASS)).isEqualTo(total);
        for (int i = 0; i < total; i++) {
            repo.deleteByUsername(NAME + i);
        }
    }

    @Test
    public void exists() throws Exception {
        assertThat(repo.existsByUsername(NAME)).isFalse();
        repo.save(new User(NAME, PASS));
        assertThat(repo.existsByUsername(NAME)).isTrue();
    }

    @Test
    public void findViaJPQL() throws Exception {
        assertThat(repo.findByUsernameViaJPQL(NAME)).isNull();
        repo.save(new User(NAME, PASS));
        User user = repo.findByUsernameViaJPQL(NAME);
        assertThat(user.getUsername()).isEqualTo(NAME);
        assertThat(user.getPassword()).isEqualTo(PASS);
    }

    @Test
    public void findViaSQL() throws Exception {
        assertThat(repo.findByUsernameViaSQL(NAME)).isNull();
        repo.save(new User(NAME, PASS));
        User user = repo.findByUsernameViaSQL(NAME);
        assertThat(user.getUsername()).isEqualTo(NAME);
        assertThat(user.getPassword()).isEqualTo(PASS);
    }

    @Test
    public void updateViaModifying() throws Exception {
        repo.save(new User(NAME, PASS));
        assertThat(repo.updatePassword(NAME, PASS2, PASS)).isEqualTo(0);
        assertThat(repo.updatePassword(NAME, PASS, PASS2)).isEqualTo(1);
        assertThat(repo.findByUsername(NAME).getPassword()).isEqualTo(PASS2);
    }
}