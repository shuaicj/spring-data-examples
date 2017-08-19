package shuaicj.example.persist.jpa.projection;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * The JPA repository for Person.
 *
 * @author shuaicj 2017/03/29
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    PersonWithoutLastName findByFirstName(String firstName);

    PersonWithFullName findByFirstNameAndLastName(String firstName, String lastName);

    @Transactional
    int deleteByFirstName(String firstName);

    @Transactional
    int deleteByLastName(String lastName);
}
