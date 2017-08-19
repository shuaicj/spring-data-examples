package shuaicj.example.persist.hive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of UserDao.
 *
 * @author shuaicj 2017/04/12
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${shuaicj.hello.persist.hive.sql.find-all}")
    private String SQL_FIND_ALL;

    @Value("${shuaicj.hello.persist.hive.sql.find-one}")
    private String SQL_FIND_ONE;

    @Value("${shuaicj.hello.persist.hive.sql.insert}")
    private String SQL_INSERT;

    @Value("${shuaicj.hello.persist.hive.sql.update}")
    private String SQL_UPDATE;

    @Value("${shuaicj.hello.persist.hive.sql.delete}")
    private String SQL_DELETE;

    @Override
    public List<User> findAll() {
        return jdbc.query(SQL_FIND_ALL, new UserMapper());
    }

    @Override
    public User findOne(int id) {
        return jdbc.queryForObject(SQL_FIND_ONE, new UserMapper(), id);
    }

    @Override
    public boolean insert(User user) {
        return jdbc.update(SQL_INSERT, user.getId(), user.getUsername()) > 0;
    }

    @Override
    public boolean update(User user) {
        return jdbc.update(SQL_UPDATE, user.getUsername(), user.getId()) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbc.update(SQL_DELETE, id) > 0;
    }

    private static class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            return user;
        }
    }
}
