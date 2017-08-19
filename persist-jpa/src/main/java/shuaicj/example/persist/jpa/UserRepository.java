package shuaicj.example.persist.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * JPA interface for user.
 *
 * @author shuaicj 2017/01/15
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Transactional
    int deleteByUsername(String username);

    // Use this form to get the detail users affected.
    // List<User> deleteByUsername(String username);

    Page<User> findAll(Pageable pageable);

    List<User> findByPasswordIgnoreCase(String password);

    List<User> findByPasswordOrderByUsernameDesc(String password);

    int countByPassword(String password);

    boolean existsByUsername(String username);

    @Query("select u from User u where u.username = ?1")
    User findByUsernameViaJPQL(String username);

    @Query(value = "select * from user where username = ?1", nativeQuery = true)
    User findByUsernameViaSQL(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?3 where username = ?1 and password = ?2")
    int updatePassword(String username, String oldPass, String newPass); // MUST NOT return boolean
}
