package shuaicj.example.persist.kv;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * A simple user repository.
 *
 * @author shuaicj 2017/04/11
 */
public interface UserRepository extends CrudRepository<User, String> {

    List<User> findByAddressStartsWith(String prefix);
}
