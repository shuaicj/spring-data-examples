package shuaicj.example.persist.jpa.relationship.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * A jpa entity representing a project.
 *
 * @author shuaicj 2017/03/30
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
    private List<Employee> employees;
}
