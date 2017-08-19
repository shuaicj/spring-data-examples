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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * A jpa entity representing an employee.
 *
 * OneToOne:   Employee <--> IdentityCard
 * OneToMany:  Employee <--> Phone
 * ManyToOne:     Phone <--> Employee
 * ManyToMany: Employee <--> Project
 *
 * @author shuaicj 2017/03/30
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @OneToOne
    @JoinColumn // @JoinColumn(name = "identity_card_id")
    private IdentityCard identityCard;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Phone> phones;

    @ManyToMany(mappedBy = "employees")
    private List<Project> projects;
}
