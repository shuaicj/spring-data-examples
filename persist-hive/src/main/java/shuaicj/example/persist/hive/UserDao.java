package shuaicj.example.persist.hive;

import java.util.List;

/**
 * An interface represents user dao.
 *
 * @author shuaicj 2017/04/12
 */
public interface UserDao {

    // return all users
    List<User> findAll();

    // return user found
    User findOne(int id);

    // return saved user
    boolean insert(User user);

    // return if update ok
    boolean update(User user);

    // return if delete ok
    boolean delete(int id);
}
