package shuaicj.example.persist.cache;

import org.springframework.stereotype.Service;

/**
 * The implementation for CachedUserService.
 *
 * @author shuaicj 2017/02/13
 */
@Service
public class CachedUserServiceImpl implements CachedUserService {

    @Override
    public User find(String username) {
        return new User(username, "china");
    }

    @Override
    public void clearCache(String username) {
    }

    @Override
    public User update(String username, String address) {
        return new User(username, address);
    }
}
