package shuaicj.example.persist.mongo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Test UserRepository.
 *
 * @author shuaicj 2017/03/04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = {"mongodb", "mongoembed"})
public class UserRepositoryTest {

    private static final String NAME = "shuaicj";
    private static final String PASS = "pass123";
    private static final String PHONE1 = "phone1";
    private static final String PHONE2 = "phone2";

    @Autowired
    private UserRepository repo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void delete() throws Exception {
        repo.save(new User(NAME, PASS));
        assertThat(repo.findByUsername(NAME)).isNotNull();
        repo.deleteByUsername(NAME);
        assertThat(repo.findByUsername(NAME)).isNull();
    }

    @Test
    public void save() throws Exception {
        repo.save(new User(NAME, PASS));
        User u = repo.findByUsername(NAME);
        assertThat(u).isNotNull();
        assertThat(u.getUsername()).isEqualTo(NAME);
        assertThat(u.getPassword()).isEqualTo(PASS);
        assertThat(u.getCreatedTime()).isNotNull();
        assertThat(u.getUpdatedTime()).isEqualTo(u.getCreatedTime());
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicate() throws Exception {
        repo.save(new User(NAME, PASS));
        repo.save(new User(NAME, PASS));
    }

    @Test
    public void updateTime() throws Exception {
        repo.save(new User(NAME, PASS));
        User u = repo.findByUsername(NAME);
        assertThat(u.getCreatedTime()).isEqualTo(u.getUpdatedTime());

        Thread.sleep(2000);

        u.setPassword("newpass");
        User u2 = repo.save(u);
        assertThat(u2.getId()).isEqualTo(u.getId());
        assertThat(u2.getUsername()).isEqualTo(u.getUsername());
        assertThat(u2.getPassword()).isEqualTo("newpass");
        assertThat(u2.getCreatedTime()).isEqualTo(u.getCreatedTime());
        assertThat(u2.getUpdatedTime()).isAfter(u.getCreatedTime());
    }

    @Test
    public void fieldIsObject() throws Exception {
        User u = new User(NAME, PASS);
        u.setAddress(new User.Address("china", "cd", "st"));
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getAddress().getCountry()).isEqualTo("china");
        assertThat(u.getAddress().getCity()).isEqualTo("cd");
        assertThat(u.getAddress().getStreet()).isEqualTo("st");
    }

    @Test
    public void fieldIsSimpleList() throws Exception {
        User u = new User(NAME, PASS);
        u.setPhones(new ArrayList<>(Arrays.asList(PHONE1, PHONE2)));
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getPhones()).containsExactly(PHONE1, PHONE2);
        u.getPhones().add(PHONE1);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getPhones()).containsExactly(PHONE1, PHONE2, PHONE1);
        u.getPhones().remove(0);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getPhones()).containsExactly(PHONE2, PHONE1);
        u.getPhones().set(1, PHONE2);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getPhones()).containsExactly(PHONE2, PHONE2);
    }

    @Test
    public void fieldIsSimpleMap() throws Exception {
        User u = new User(NAME, PASS);
        Map<String, Integer> scores = new HashMap<>();
        scores.put("math", 80);
        scores.put("science", 90);
        u.setScores(scores);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getScores()).containsOnly(entry("math", 80), entry("science", 90));
        u.getScores().put("biology", 100);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getScores()).containsOnly(entry("math", 80), entry("science", 90), entry("biology", 100));
        u.getScores().remove("math");
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getScores()).containsOnly(entry("science", 90), entry("biology", 100));
        u.getScores().put("science", 70);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getScores()).containsOnly(entry("science", 70), entry("biology", 100));
    }

    @Test
    public void fieldIsListContainsObject() throws Exception {
        User u = new User(NAME, PASS);
        u.setAddresses(Arrays.asList(new User.Address("a1", "b1", "c1"), new User.Address("a2", "b2", "c2")));
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getAddresses()).hasSize(2);
        assertThat(u.getAddresses().get(0).getCountry().equals("a1")
                && u.getAddresses().get(0).getCity().equals("b1")
                && u.getAddresses().get(0).getStreet().equals("c1")
                && u.getAddresses().get(1).getCountry().equals("a2")
                && u.getAddresses().get(1).getCity().equals("b2")
                && u.getAddresses().get(1).getStreet().equals("c2")
                || u.getAddresses().get(0).getCountry().equals("a2")
                && u.getAddresses().get(0).getCity().equals("b2")
                && u.getAddresses().get(0).getStreet().equals("c2")
                && u.getAddresses().get(1).getCountry().equals("a1")
                && u.getAddresses().get(1).getCity().equals("b1")
                && u.getAddresses().get(1).getStreet().equals("c1")).isTrue();
    }

    @Test
    public void fieldIsListContainsList() throws Exception {
        User u = new User(NAME, PASS);
        u.setListListScores(Arrays.asList(Arrays.asList(70, 80), Arrays.asList(90, 100, 110)));
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getListListScores()).hasSize(2);
        assertThat(u.getListListScores().get(0)).containsExactly(70, 80);
        assertThat(u.getListListScores().get(1)).containsExactly(90, 100, 110);
    }

    @Test
    public void fieldIsListContainsMap() throws Exception {
        User u = new User(NAME, PASS);
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 10);
        map1.put("b", 20);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("c", 10);
        Map<String, Integer> map3 = new HashMap<>();
        map3.put("d", 30);
        map3.put("e", 20);
        map3.put("f", 10);
        List<Map<String, Integer>> listMapScores = new ArrayList<>();
        listMapScores.add(map1);
        listMapScores.add(map2);
        listMapScores.add(map3);
        u.setListMapScores(listMapScores);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getListMapScores()).hasSize(3);
        assertThat(u.getListMapScores().get(0)).containsOnly(entry("a", 10), entry("b", 20));
        assertThat(u.getListMapScores().get(1)).containsOnly(entry("c", 10));
        assertThat(u.getListMapScores().get(2)).containsOnly(entry("d", 30), entry("e", 20), entry("f", 10));
    }

    @Test
    public void fieldIsMapValuesObject() throws Exception {
        User u = new User(NAME, PASS);
        Map<String, User.Address> groupAddresses = new HashMap<>();
        groupAddresses.put("g1", new User.Address("d1", "e1", "f1"));
        groupAddresses.put("g2", new User.Address("d2", "e2", "f2"));
        u.setGroupAddresses(groupAddresses);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getGroupAddresses()).containsKeys("g1", "g2");
        assertThat(u.getGroupAddresses().get("g1").getCountry()).isEqualTo("d1");
        assertThat(u.getGroupAddresses().get("g1").getCity()).isEqualTo("e1");
        assertThat(u.getGroupAddresses().get("g1").getStreet()).isEqualTo("f1");
        assertThat(u.getGroupAddresses().get("g2").getCountry()).isEqualTo("d2");
        assertThat(u.getGroupAddresses().get("g2").getCity()).isEqualTo("e2");
        assertThat(u.getGroupAddresses().get("g2").getStreet()).isEqualTo("f2");
    }

    @Test
    public void fieldIsMapValuesList() throws Exception {
        User u = new User(NAME, PASS);
        Map<String, List<Integer>> mapListScores = new HashMap<>();
        mapListScores.put("a", Arrays.asList(10, 20));
        mapListScores.put("b", Arrays.asList(30, 60, 50));
        u.setMapListScores(mapListScores);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getMapListScores()).containsKeys("a", "b");
        assertThat(u.getMapListScores().get("a")).containsExactly(10, 20);
        assertThat(u.getMapListScores().get("b")).containsExactly(30, 60, 50);
    }

    @Test
    public void fieldIsMapValuesMap() throws Exception {
        User u = new User(NAME, PASS);
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 10);
        map1.put("b", 20);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("c", 10);
        Map<String, Integer> map3 = new HashMap<>();
        map3.put("d", 30);
        map3.put("e", 20);
        map3.put("f", 10);
        Map<String, Map<String, Integer>> mapMapScores = new HashMap<>();
        mapMapScores.put("111", map1);
        mapMapScores.put("222", map2);
        mapMapScores.put("333", map3);
        u.setMapMapScores(mapMapScores);
        repo.save(u);
        u = repo.findByUsername(NAME);
        assertThat(u.getMapMapScores()).containsKeys("111", "222", "333");
        assertThat(u.getMapMapScores().get("111")).containsOnly(entry("a", 10), entry("b", 20));
        assertThat(u.getMapMapScores().get("222")).containsOnly(entry("c", 10));
        assertThat(u.getMapMapScores().get("333")).containsOnly(entry("d", 30), entry("e", 20), entry("f", 10));
    }

    @Test
    public void customQuery() throws Exception {
        User u1 = new User(NAME + "1", PASS);
        u1.setAddresses(Arrays.asList(new User.Address("a1", "b1", "c1")));
        repo.save(u1);
        User u2 = new User(NAME + "2", PASS);
        u2.setAddresses(Arrays.asList(new User.Address("d1", "e1", "f1"), new User.Address("g2", "h2", "i2")));
        repo.save(u2);
        User u3 = new User(NAME + "3", PASS);
        u3.setAddresses(Arrays.asList(new User.Address("j1", "k1", "l1"), new User.Address("m2", "n2", "o2")));
        repo.save(u3);

        List<User> users1 = repo.findUserWithManyAddresses(1);
        assertThat(users1).hasSize(1);
        assertThat(users1.get(0).getUsername()).isEqualTo(NAME + "1");
        assertThat(users1.get(0).getAddresses()).hasSize(1);

        List<User> users2 = repo.findUserWithManyAddresses(2);
        assertThat(users2).hasSize(2);
        for (User u : users2) {
            assertThat(u.getUsername()).isIn(NAME + "2", NAME + "3");
            assertThat(u.getAddresses()).hasSize(2);
        }
    }
}