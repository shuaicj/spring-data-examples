package shuaicj.example.persist.jdbc.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of UserDao.
 *
 * @author shuaicj 2017/01/24
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public User save(User user) {
        String sql = "insert into user(username, password) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);

        User u = new User();
        u.setId(keyHolder.getKey().longValue());
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        return u;
    }

    @Override
    public User findByUsername(String username) {
        return jdbc.queryForObject("select * from user where username = ?", new UserMapper(), username);
    }

    @Override
    public int deleteByUsername(String username) {
        return jdbc.update("delete from user where username = ?", username);
    }

    @Override
    public int updatePasswordByUsername(String username, String password) {
        return jdbc.update("update user set password = ? where username = ?", password, username);
    }

    private static class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setCreatedTime(rs.getDate("created_time"));
            user.setUpdatedTime(rs.getDate("updated_time"));
            return user;
        }
    }
}
