package shuaicj.example.persist.jpa.relationship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import shuaicj.example.persist.jpa.relationship.entity.Employee;

import javax.transaction.Transactional;

/**
 * The JPA repository for Employee.
 *
 * @author shuaicj 2017/03/30
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByName(String name);

    @Transactional
    int deleteByName(String name);
}
