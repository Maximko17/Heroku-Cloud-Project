package maxcompany.realcloudapp.repository;


import maxcompany.realcloudapp.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);

}
