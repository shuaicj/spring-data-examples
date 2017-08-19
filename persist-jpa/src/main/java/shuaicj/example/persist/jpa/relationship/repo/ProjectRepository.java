package shuaicj.example.persist.jpa.relationship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import shuaicj.example.persist.jpa.relationship.entity.Project;

import javax.transaction.Transactional;

/**
 * The JPA repository for Project.
 *
 * @author shuaicj 2017/03/30
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByName(String name);

    @Transactional
    int deleteByName(String name);
}
