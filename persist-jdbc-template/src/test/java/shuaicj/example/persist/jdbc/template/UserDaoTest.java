package shuaicj.example.persist.jdbc.template;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test UserDao.
 *
 * @author shuaicj 2017/01/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = {"h2", "mysql"})
public class UserDaoTest {

    private static final String NAME = "shuaicj";
    private static final String PASS = "pass123";
    private static final String NAME2 = "newuser";
    private static final String PASS2 = "newpass";

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserDao dao;

    @Before
    public void setUp() throws Exception {
        jdbcDelete(NAME);
        jdbcDelete(NAME2);
    }

    @After
    public void tearDown() throws Exception {
        jdbcDelete(NAME);
        jdbcDelete(NAME2);
    }

    @Test
    public void save() throws Exception {
        assertThat(jdbcCount(NAME, PASS)).isEqualTo(0);
        dao.save(new User(NAME, PASS));
        assertThat(jdbcCount(NAME, PASS)).isEqualTo(1);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveDuplicate() throws Exception {
        dao.save(new User(NAME, PASS));
        dao.save(new User(NAME, PASS));
    }

    @Test
    public void saveReturnsId() throws Exception {
        User u1 = dao.save(new User(NAME, PASS));
        User u2 = dao.save(new User(NAME2, PASS2));
        assertThat(u2.getId()).isEqualTo(u1.getId() + 1);
    }

    @Test
    public void findByUsername() throws Exception {
        jdbcInsert(NAME, PASS);
        User user = dao.findByUsername(NAME);
        assertThat(user.getPassword()).isEqualTo(PASS);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void findByUsernameNotExists() throws Exception {
        dao.findByUsername(NAME);
    }

    @Test
    public void deleteByUsername() throws Exception {
        jdbcInsert(NAME, PASS);
        assertThat(jdbcCount(NAME, PASS)).isEqualTo(1);
        int num = dao.deleteByUsername(NAME);
        assertThat(jdbcCount(NAME, PASS)).isEqualTo(0);
        assertThat(num).isEqualTo(1);
    }

    @Test
    public void deleteByUsernameNotExists() throws Exception {
        int num = dao.deleteByUsername(NAME);
        assertThat(num).isEqualTo(0);
    }

    @Test
    public void updatePasswordByUsername() throws Exception {
        jdbcInsert(NAME, PASS);
        int num = dao.updatePasswordByUsername(NAME, PASS2);
        assertThat(jdbcCount(NAME, PASS)).isEqualTo(0);
        assertThat(jdbcCount(NAME, PASS2)).isEqualTo(1);
        assertThat(num).isEqualTo(1);
    }

    @Test
    public void updatePasswordByUsernameNotExists() throws Exception {
        int num = dao.updatePasswordByUsername(NAME, PASS2);
        assertThat(num).isEqualTo(0);
    }

    private void jdbcDelete(String username) throws Exception {
        jdbc.update("delete from user where username = ?", username);
    }

    private void jdbcInsert(String username, String password) throws Exception {
        jdbc.update("insert into user(username, password) values(?, ?)", username, password);
    }

    private int jdbcCount(String username, String password) throws Exception {
        String sql = "select count(*) from user where username = ? and password = ?";
        return jdbc.queryForObject(sql, Integer.class, username, password);
    }
}