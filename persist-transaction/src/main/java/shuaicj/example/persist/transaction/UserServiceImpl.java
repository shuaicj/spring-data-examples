package shuaicj.example.persist.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shuaicj.example.persist.jpa.User;
import shuaicj.example.persist.jpa.UserRepository;

import java.util.List;

/**
 * The implementation of UserService.
 *
 * @author shuaicj 2017/02/03
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    @Transactional
    public void register(List<User> users) {
        for (User u : users) {
            repo.save(u);
        }
    }
}
