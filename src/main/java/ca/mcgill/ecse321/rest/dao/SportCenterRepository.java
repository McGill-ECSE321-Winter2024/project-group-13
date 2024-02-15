package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.SportCenter;
import org.springframework.data.repository.CrudRepository;

public interface SportsCenterRepository extends CrudRepository<SportCenter, String>{

    SportCenter findSportCenterById(String id);

    SportCenter findSportCenterByName(String name);


}