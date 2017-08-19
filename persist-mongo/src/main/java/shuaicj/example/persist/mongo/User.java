package shuaicj.example.persist.mongo;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A java bean representing a user.
 *
 * @author shuaicj 2017/03/04
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    private String id;

    @NonNull
    @Indexed(unique = true)
    private String username;

    @NonNull
    private String password;

    private Address address; // an object

    private List<String> phones; // a list

    private List<Address> addresses; // a list contains objects

    private Map<String, Integer> scores; // a map

    private Map<String, Address> groupAddresses; // a map with object value

    private List<List<Integer>> listListScores; // a list contains list

    private List<Map<String, Integer>> listMapScores; // a list contains map

    private Map<String, List<Integer>> mapListScores; // a map with list value

    private Map<String, Map<String, Integer>> mapMapScores; // a map with map value

    @CreatedDate
    private Date createdTime;

    @LastModifiedDate
    private Date updatedTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Address {
        private String country, city, street;
    }
}
