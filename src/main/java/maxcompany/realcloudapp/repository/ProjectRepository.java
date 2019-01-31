package maxcompany.realcloudapp.repository;

import maxcompany.realcloudapp.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    Project findByProjectIdentifier(String Id);

    List<Project> findAllByProjectLeader(String leader);
}
