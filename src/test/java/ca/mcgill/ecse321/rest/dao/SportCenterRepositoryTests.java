package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.models.Schedule;
import ca.mcgill.ecse321.rest.models.SportCenter;
import java.sql.Time;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SportCenterRepositoryTests {
  @Autowired private SportCenterRepository sportCenterRepository;

  @Autowired private ScheduleRepository scheduleRepository;

  private Schedule schedule;

  /**
   * This method executes before each test. This is done by the "@BeforeEach" JPA annotation The
   * method is used to fill the database with a sport center before each of the tests. @Author
   * Philippe Aprahamian
   */
  @BeforeEach
  public void CreateSportCenter() {
    // Create SportCenter.
    String name = "McGill Recreation";
    Time openingHour = new Time(8, 0, 0);
    Time closingHour = new Time(20, 30, 0);
    schedule = new Schedule(openingHour, closingHour);
    scheduleRepository.save(schedule);
    String address = "1234 Av Dr.Penfield";
    SportCenter center = new SportCenter();
    center.setName(name);
    center.setAddress(address);
    center.setSchedule(schedule);
    // Save center
    sportCenterRepository.save(center);
  }

  /**
   * This method executes after each test. This is done by the "@AfterEach" JPA annotation The
   * method is used to clear the database after each of the tests, so that we don't fill up our
   * database tables with unwanted data from tests. @Author Philippe Aprahamian
   */
  @AfterEach
  public void clearDatabase() {
    sportCenterRepository.deleteAll();
  }

  /** This method tests the save and the findSportCenterByName method @Author Philippe Aprahamian */
  @Test
  public void testCreateAndFindSportCenterByName() {
    // Read center from database.
    SportCenter center = sportCenterRepository.findSportCenterByName("McGill Recreation");

    // Assert that center is not null and has correct attributes.
    assertNotNull(center);
    assertEquals("McGill Recreation", center.getName());
    assertEquals("1234 Av Dr.Penfield", center.getAddress());
    assertEquals(schedule.toString(), center.getSchedule().toString());
  }

  /**
   * This method tests the save and the findSportCenterByAddress method @Author Philippe Aprahamian
   */
  @Test
  public void testFindSportCenterByAddress() {
    // Read center from database.
    SportCenter center = sportCenterRepository.findSportCenterByAddress("1234 Av Dr.Penfield");

    // Assert that center is not null and has correct attributes.
    assertNotNull(center);
    assertEquals("McGill Recreation", center.getName());
  }

  /**
   * This method tests that when we update attributes of the local object it also updates the
   * database @Author Philippe Aprahamian
   */
  @Test
  public void testUpdateAndReadSportCenter() {
    SportCenter center = sportCenterRepository.findSportCenterByName("McGill Recreation");
    center.setAddress("4321 Av Dr.Penfield");

    Time openingHour = new Time(8, 0, 0);
    Time closingHour = new Time(16, 0, 0);
    schedule = new Schedule(openingHour, closingHour);
    scheduleRepository.save(schedule);
    assertNotEquals(schedule, center.getSchedule());
    center.setSchedule(schedule);
    // Save center
    sportCenterRepository.save(center);
    center = sportCenterRepository.findSportCenterByName("McGill Recreation");
    assertEquals("4321 Av Dr.Penfield", center.getAddress());
    assertEquals(schedule.toString(), center.getSchedule().toString());
  }

  /**
   * This method tests the save and the deleteSportCenterByName method @Author Philippe Aprahamian
   */
  @Test
  public void testDeleteSportCenterByName() {
    // Delete center from database.
    SportCenter center = sportCenterRepository.findSportCenterByName("McGill Recreation");
    sportCenterRepository.delete(center);

    // Assert that center is null.
    assertNull(sportCenterRepository.findSportCenterByName("McGill Recreation"));
  }
}
