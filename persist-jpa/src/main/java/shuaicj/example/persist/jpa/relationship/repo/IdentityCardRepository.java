package shuaicj.example.persist.jpa.relationship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import shuaicj.example.persist.jpa.relationship.entity.IdentityCard;

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
