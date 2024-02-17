package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.SportCenter;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
/**
 * This is the SportCenter CRUD Repository
 *
 * @Author Philippe Aprahamian
 */
public interface SportCenterRepository extends CrudRepository<SportCenter, String>{

    SportCenter findSportCenterByName(String name);
    SportCenter findSportCenterByAddress(String address);
    Integer deleteSportCenterByName(String name);


}