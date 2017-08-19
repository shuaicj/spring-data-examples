package shuaicj.example.persist.kv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;

/**
 * A simple user.
 *
 * @author shuaicj 2017/04/11
 */
@Getter
@AllArgsConstructor
public class User {

    @Id
    private final String username;

    private final String address;
}
