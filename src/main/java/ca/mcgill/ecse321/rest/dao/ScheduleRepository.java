package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Schedule;
import org.springframework.data.repository.CrudRepository;

/**
 * This is the SportCenter CRUD Repository
 *
 * @Author Philippe Aprahamian
 */
public interface ScheduleRepository extends CrudRepository<Schedule, String>{




}