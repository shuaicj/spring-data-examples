package shuaicj.example.persist.redis.serializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A java bean representing a user.
 *
 * @author shuaicj 2017/04/20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String username;
    private Address address;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static final class Address {
        private String country, city;
    }
}

