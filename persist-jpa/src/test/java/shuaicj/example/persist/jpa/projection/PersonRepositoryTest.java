package shuaicj.example.persist.jpa.projection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA projections test.
 *
 * @author shuaicj 2017/03/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = {"h2", "mysql"})
public class PersonRepositoryTest {

    private static final String FIRST_NAME = "Changjun";
    private static final String LAST_NAME = "Shuai";

    @Autowired
    PersonRepository repo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void closedProejction() throws Exception {
        repo.save(new Person(FIRST_NAME, LAST_NAME));
        PersonWithoutLastName person = repo.findByFirstName(FIRST_NAME);
        assertThat(person).isNotNull();
        assertThat(person.getFirstName()).isEqualTo(FIRST_NAME);
    }

    @Test
    public void openProejction() throws Exception {
        repo.save(new Person(FIRST_NAME, LAST_NAME));
        PersonWithFullName person = repo.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME);
        assertThat(person).isNotNull();
        assertThat(person.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(person.getLastName()).isEqualTo(LAST_NAME);
        assertThat(person.getFullName()).isEqualTo(FIRST_NAME + " " + LAST_NAME);
    }
}