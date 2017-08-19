package shuaicj.example.persist.redis.repository;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for user.
 *
 * @author shuaicj 2017/04/20
 */
public interface UserRepository extends CrudRepository<User, String> {
}
