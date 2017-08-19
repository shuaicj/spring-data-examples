package shuaicj.example.persist.jdbc.template;

/**
 * An interface represents user dao.
 *
 * @author shuaicj 2017/01/24
 */
public interface UserDao {

    // return saved user
    User save(User user);

    // return user found
    User findByUsername(String username);

    // return row number affected
    int deleteByUsername(String username);

    // return row number affected
    int updatePasswordByUsername(String username, String password);
}
