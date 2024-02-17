package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.SportCenter;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void clearDatabase() {
        sportCenterRepository.deleteAll();
    }

    @Test
    public void testCRUDSportCenter() {
        // Create SportCenter.
        String name= "McGill Recreation";
        Time openingHour = new Time(8,0,0);
        Time closingHour = new Time(20,0,0);
        String address = "1234 Av Dr.Penfield";

        SportCenter center = new SportCenter();
        center.setName(name);
        center.setAddress(address);
        center.setOpeningHour(openingHour);
        center.setClosingHour(closingHour);

        // Save center
        sportCenterRepository.save(center);

        // Read center from database.
        center = sportCenterRepository.findSportCenterByName(name);

        // Assert that center is not null and has correct attributes.
        assertNotNull(center);
        assertEquals(name, center.getName());
        assertEquals(openingHour, center.getOpeningHour());
        assertEquals(closingHour, center.getClosingHour());

        // Delete center from database.
        sportCenterRepository.delete(center);
        center = sportCenterRepository.findSportCenterByName(name);

        // Assert that center is null.
        assertNull(center);
    }



}