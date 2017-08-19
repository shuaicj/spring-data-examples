package shuaicj.example.persist.transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import shuaicj.example.persist.jpa.User;
import shuaicj.example.persist.jpa.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test UserServiceImpl.
 *
 * @author shuaicj 2017/02/03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = {"h2", "mysql"})
public class UserServiceImplTest {

    private static final String NAME1 = "shuaicj1";
    private static final String NAME2 = "shuaicj2";
    private static final String PASS1 = "pass1234";
    private static final String PASS2 = "pass2345";

    @Autowired
    private UserService userService;

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
    public void registerOK() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User(NAME1, PASS1));
        users.add(new User(NAME2, PASS2));
        userService.register(users);
    }

    @Test
    public void registerDuplicate() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User(NAME1, PASS1));
        users.add(new User(NAME1, PASS1));
        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() -> userService.register(users));
        assertThat(repo.findByUsername(NAME1)).isNull();
    }
}