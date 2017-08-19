package shuaicj.example.persist.hive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A java bean representing a user.
 *
 * @author shuaicj 2017/04/12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotNull
    @Min(1)
    private Integer id;

    @NotNull
    private String username;
}
