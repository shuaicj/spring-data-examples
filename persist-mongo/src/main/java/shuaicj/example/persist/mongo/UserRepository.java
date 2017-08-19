package shuaicj.example.persist.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Mongo interface for user.
 *
 * @author shuaicj 2017/03/04
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    void deleteByUsername(String username);

    @Query("{ addresses: { $size: ?0 } }")
    List<User> findUserWithManyAddresses(int addressNum);

    // NOTE:
    //   @Query the first param is ?0 in spring data mongodb, while it's ?1 in spring data jpa
    //   @Modifying is not applicable in spring data mongodb
}
