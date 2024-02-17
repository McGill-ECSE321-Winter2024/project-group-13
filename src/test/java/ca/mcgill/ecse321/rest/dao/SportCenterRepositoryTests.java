package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.SportCenter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SportCenterRepositoryTests {
    @Autowired
    private SportCenterRepository sportCenterRepository;

    @BeforeAll
    public void CreateSportCenter() {
        // Create SportCenter.
        String name= "McGill Recreation";
        //Time openingHour = new Time(8,0,0);
        //Time closingHour = new Time(20,0,0);
        String address = "1234 Av Dr.Penfield";
        SportCenter center = new SportCenter();
        center.setName(name);
        center.setAddress(address);

        // Save center
        sportCenterRepository.save(center);
    }
    /**
     * This method executes after each test. This is done by the "@AfterAll" JPA annotation
     * The method is used to clear the database after all tests, so that we don't fill up our database tables
     * with unwanted data from tests.
     *
     * @Author Philippe Aprahamian
     */
    @AfterAll
    public void clearDatabase() {
        sportCenterRepository.deleteAll();
    }

    /**
     * This method tests the creation and the del
     *
     * @Author Philippe Aprahamian
     */
    @Test
    public void testCreateAndReadSportCenter() {
        // Read center from database.
        SportCenter center = sportCenterRepository.findSportCenterByName("McGill Recreation");

        // Assert that center is not null and has correct attributes.
        assertNotNull(center);
        assertEquals("McGill Recreation", center.getName());
        assertEquals("1234 Av Dr.Penfield", center.getAddress());
    }
    @Test
    public void testUpdateAndReadSportCenter() {
        SportCenter center = sportCenterRepository.findSportCenterByName("McGill Recreation");
        center.setAddress("4321 Av Dr.Penfield");
        // Save center
        //sportCenterRepository.save(center);
        center = sportCenterRepository.findSportCenterByName("McGill Recreation");
        assertEquals("4321 Av Dr.Penfield",center.getAddress());
    }
    @Test
    public void testDeleteSportCenter() {
        // Delete center from database.
        sportCenterRepository.deleteSportCenterByName("McGill Recreation");
        // Assert that center is null.
        assertNull(sportCenterRepository.findSportCenterByName("McGill Recreation"));
    }



}