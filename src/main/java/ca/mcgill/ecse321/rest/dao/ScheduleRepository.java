package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Schedule;
import org.springframework.data.repository.CrudRepository;

/** This is the Schedule CRUD Repository
 * @Author Mohamed Abdelrahman **/
public interface ScheduleRepository extends CrudRepository<Schedule, String> {
    Schedule findScheduleById(String id);
}
