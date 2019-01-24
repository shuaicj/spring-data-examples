package shuaicj.example.persist.jpa.relationship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import shuaicj.example.persist.jpa.relationship.entity.Phone;

/**
 * The JPA repository for Phone.
 *
 * @author shuaicj 2017/03/30
 */
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Phone findByNumber(String number);

    @Transactional
    int deleteByNumber(String number);
}
