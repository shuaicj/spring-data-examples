package shuaicj.example.persist.jpa.relationship.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * A jpa entity representing an identity card.
 *
 * @author shuaicj 2017/03/30
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class IdentityCard {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String number;

    @OneToOne(mappedBy = "identityCard")
    private Employee owner;
}
