package shuaicj.example.persist.jpa.relationship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import shuaicj.example.persist.jpa.relationship.entity.Phone;

import javax.transaction.Transactional;

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
