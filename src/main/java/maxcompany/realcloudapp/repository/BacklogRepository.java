package maxcompany.realcloudapp.repository;

import maxcompany.realcloudapp.domain.Backlog;
import org.springframework.data.repository.CrudRepository;

public interface BacklogRepository extends CrudRepository<Backlog,Long> {

    Backlog findByProjectIdentifier(String id);
}
