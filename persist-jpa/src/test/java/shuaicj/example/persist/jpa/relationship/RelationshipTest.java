package shuaicj.example.persist.jpa.relationship;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import shuaicj.example.persist.jpa.relationship.entity.Employee;
import shuaicj.example.persist.jpa.relationship.entity.IdentityCard;
import shuaicj.example.persist.jpa.relationship.entity.Phone;
import shuaicj.example.persist.jpa.relationship.entity.Project;
import shuaicj.example.persist.jpa.relationship.repo.EmployeeRepository;
import shuaicj.example.persist.jpa.relationship.repo.IdentityCardRepository;
import shuaicj.example.persist.jpa.relationship.repo.PhoneRepository;
import shuaicj.example.persist.jpa.relationship.repo.ProjectRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA relationship test.
 *
 * @author shuaicj 2017/03/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = {"h2", "mysql"})
public class RelationshipTest {

    private static final String EMPLOYEE_NAME = "shuaicj";
    private static final String EMPLOYEE_NAME_2 = "lkjkcd";
    private static final String IDENTITY_CARD_NUMBER = "12345";
    private static final String IDENTITY_CARD_NUMBER_2 = "67890";
    private static final String PHONE_NUMBER = "+86 180-8888-9999";
    private static final String PHONE_NUMBER_2 = "+86 180-9999-8888";
    private static final String PROJECT_NAME = "projecta";
    private static final String PROJECT_NAME_2 = "projectb";

    @Autowired
    EmployeeRepository employeeRepo;
    @Autowired
    IdentityCardRepository identityCardRepo;
    @Autowired
    PhoneRepository phoneRepo;
    @Autowired
    ProjectRepository projectRepo;

    @Before
    public void setUp() throws Exception {
        projectRepo.deleteAll();
        phoneRepo.deleteAll();
        employeeRepo.deleteAll();
        identityCardRepo.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void testOneToOne() throws Exception {
        employeeRepo.save(new Employee(EMPLOYEE_NAME,
                identityCardRepo.save(new IdentityCard(IDENTITY_CARD_NUMBER))));

        Employee employee = employeeRepo.findByName(EMPLOYEE_NAME);
        assertThat(employee.getName()).isEqualTo(EMPLOYEE_NAME);
        assertThat(employee.getIdentityCard().getNumber()).isEqualTo(IDENTITY_CARD_NUMBER);
        assertThat(employee.getIdentityCard().getOwner()).isEqualTo(employee);

        IdentityCard identityCard = identityCardRepo.findByNumber(IDENTITY_CARD_NUMBER);
        assertThat(identityCard.getNumber()).isEqualTo(IDENTITY_CARD_NUMBER);
        assertThat(identityCard.getOwner().getName()).isEqualTo(EMPLOYEE_NAME);
        assertThat(identityCard.getOwner().getIdentityCard()).isEqualTo(identityCard);
    }

    @Test
    public void testOneToManyAndManyToOne() throws Exception {
        Employee owner = employeeRepo.save(new Employee(EMPLOYEE_NAME,
                identityCardRepo.save(new IdentityCard(IDENTITY_CARD_NUMBER))));
        phoneRepo.save(new Phone(PHONE_NUMBER, owner));
        phoneRepo.save(new Phone(PHONE_NUMBER_2, owner));

        Employee employee = employeeRepo.findByName(EMPLOYEE_NAME);
        assertThat(employee.getName()).isEqualTo(EMPLOYEE_NAME);
        assertThat(employee.getPhones()).hasSize(2);
        assertThat(employee.getPhones().get(0).getNumber()).isIn(PHONE_NUMBER, PHONE_NUMBER_2);
        assertThat(employee.getPhones().get(1).getNumber()).isIn(PHONE_NUMBER, PHONE_NUMBER_2);
        assertThat(employee.getPhones().get(0).getOwner()).isEqualTo(employee);
        assertThat(employee.getPhones().get(1).getOwner()).isEqualTo(employee);

        for (String number : Arrays.asList(PHONE_NUMBER, PHONE_NUMBER_2)) {
            Phone phone = phoneRepo.findByNumber(number);
            assertThat(phone.getNumber()).isEqualTo(number);
            assertThat(phone.getOwner().getName()).isEqualTo(EMPLOYEE_NAME);
            assertThat(phone.getOwner().getPhones()).hasSize(2);
            assertThat(phone.getOwner().getPhones()).contains(phone);
        }
    }

    @Test
    public void testManyToMany() throws Exception {
        List<Employee> employees = Arrays.asList(
                employeeRepo.save(new Employee(EMPLOYEE_NAME,
                        identityCardRepo.save(new IdentityCard(IDENTITY_CARD_NUMBER)))),
                employeeRepo.save(new Employee(EMPLOYEE_NAME_2,
                        identityCardRepo.save(new IdentityCard(IDENTITY_CARD_NUMBER_2)))));
        projectRepo.save(new Project(PROJECT_NAME, employees));
        projectRepo.save(new Project(PROJECT_NAME_2, employees));

        for (String employeeName : Arrays.asList(EMPLOYEE_NAME, EMPLOYEE_NAME_2)) {
            Employee employee = employeeRepo.findByName(employeeName);
            assertThat(employee.getName()).isEqualTo(employeeName);
            // Commented because FetchType = LAZY
            // assertThat(employee.getProjects()).hasSize(2);
            // assertThat(employee.getProjects().get(0).getName()).isIn(PROJECT_NAME, PROJECT_NAME_2);
            // assertThat(employee.getProjects().get(1).getName()).isIn(PROJECT_NAME, PROJECT_NAME_2);
        }
        for (String projectName : Arrays.asList(PROJECT_NAME, PROJECT_NAME_2)) {
            Project project = projectRepo.findByName(projectName);
            assertThat(project.getName()).isEqualTo(projectName);
            assertThat(project.getEmployees()).hasSize(2);
            assertThat(project.getEmployees().get(0).getName()).isIn(EMPLOYEE_NAME, EMPLOYEE_NAME_2);
            assertThat(project.getEmployees().get(1).getName()).isIn(EMPLOYEE_NAME, EMPLOYEE_NAME_2);
        }
    }
}