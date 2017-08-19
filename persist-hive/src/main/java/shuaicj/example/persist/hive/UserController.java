package shuaicj.example.persist.hive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Simple spring boot rest controller.
 *
 * @author shuaicj 2016/12/28
 */
@RestController
public class UserController {

    @Autowired
    UserDao dao;

    @GetMapping("/users")
    public List<User> getAll() {
        return dao.findAll();
    }

    @GetMapping("/users/{id}")
    public User getOne(@PathVariable int id) {
        return dao.findOne(id);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User post(@Valid @RequestBody User user) {
        dao.insert(user);
        return user;
    }

    @PutMapping("/users")
    public ResponseEntity<User> put(@Valid @RequestBody User user) {
        if (dao.update(user)) {
            return ResponseEntity.ok(user);
        } else {
            dao.insert(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable int id) {
        dao.delete(id);
    }
}

