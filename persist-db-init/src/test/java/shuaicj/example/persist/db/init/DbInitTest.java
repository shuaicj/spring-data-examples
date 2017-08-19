package shuaicj.example.persist.db.init;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test spring boot db init.
 *
 * @author shuaicj 2017/01/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = {"h2", "mysql"})
public class DbInitTest {

    private static final String NAME = "shuaicj";
    private static final String PASS = "pass123";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void exists() throws Exception {
        String sql = "select count(*) from user where username = ? and password = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, NAME, PASS);
        assertThat(count).isEqualTo(1);
    }

    @Test(expected = DuplicateKeyException.class)
    public void unique() throws Exception {
        jdbcTemplate.update("insert into user(username, password) values (?, ?)", NAME, "adf");
    }

    @Test
    public void time() throws Exception {
        String sqlQuery = "select created_time, updated_time from user where username = ?";
        Map<String, Object> times1 = jdbcTemplate.queryForMap(sqlQuery, NAME);
        Date createdTime1 = (Date) times1.get("created_time");
        Date updatedTime1 = (Date) times1.get("updated_time");
        assertThat(createdTime1).isEqualTo(updatedTime1);

        Thread.sleep(1000);

        String sqlUpdate = "update user set password = ? where username = ?";
        jdbcTemplate.update(sqlUpdate, "abcd", NAME);
        Map<String, Object> times2 = jdbcTemplate.queryForMap(sqlQuery, NAME);
        Date createdTime2 = (Date) times2.get("created_time");
        Date updatedTime2 = (Date) times2.get("updated_time");
        assertThat(createdTime2).isEqualTo(createdTime1);
        assertThat(updatedTime2).isAfter(updatedTime1);

        jdbcTemplate.update(sqlUpdate, PASS, NAME);
    }
}