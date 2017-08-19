package shuaicj.example.persist.jpa.relationship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import shuaicj.example.persist.jpa.relationship.entity.IdentityCard;

import javax.transaction.Transactional;

/**
 * The JPA repository for IdentityCard.
 *
 * @author shuaicj 2017/03/30
 */
public interface IdentityCardRepository extends JpaRepository<IdentityCard, Long> {

    IdentityCard findByNumber(String number);

    @Transactional
    int deleteByNumber(String number);
}
