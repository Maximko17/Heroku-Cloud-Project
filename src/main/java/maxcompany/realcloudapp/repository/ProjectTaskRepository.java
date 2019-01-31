package maxcompany.realcloudapp.repository;

import maxcompany.realcloudapp.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectTaskRepository extends CrudRepository<ProjectTask,Long> {

    List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

    ProjectTask findByProjectIdentifierAndProjectSequence(String project_id,String sequence);
}
