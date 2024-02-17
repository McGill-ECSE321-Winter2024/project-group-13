package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.SportCenter;
import org.springframework.data.repository.CrudRepository;

public interface SportCenterRepository extends CrudRepository<SportCenter, String>{

    SportCenter findSportCenterByName(String name);
    Boolean deleteSportCenterByName(String name);



}