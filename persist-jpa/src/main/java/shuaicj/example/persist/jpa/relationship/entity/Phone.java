package shuaicj.example.persist.jpa.relationship.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A jpa entity representing a phone.
 *
 * @author shuaicj 2017/03/30
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Phone {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String number;

    @NonNull
    @ManyToOne
    @JoinColumn
    private Employee owner;
}
