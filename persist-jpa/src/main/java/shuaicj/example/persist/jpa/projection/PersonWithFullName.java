package shuaicj.example.persist.jpa.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * An open projection with full name.
 *
 * @author shuaicj 2017/03/30
 */
public interface PersonWithFullName {

    String getFirstName();
    String getLastName();

    @Value("#{target.firstName} #{target.lastName}")
    String getFullName();
}
