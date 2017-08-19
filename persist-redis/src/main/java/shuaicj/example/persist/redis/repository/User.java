package shuaicj.example.persist.redis.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * A java bean representing a user.
 *
 * @author shuaicj 2017/04/20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "users", timeToLive = 1)
public class User {

    @Id
    private String username;

    private Address address;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Address {
        private String country, city;
    }
}

