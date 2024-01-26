package ca.mcgill.ecse321.rest.repositories;

import ca.mcgill.ecse321.rest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
