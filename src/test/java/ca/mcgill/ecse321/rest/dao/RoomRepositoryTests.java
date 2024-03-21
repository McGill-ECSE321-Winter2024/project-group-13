package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;
import ca.mcgill.ecse321.rest.models.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomRepositoryTests {
    @Autowired private RoomRepository roomRepository;
    /**
     * Sets the name of a room, saves it to the database.
     * @author Rafael Reis
     */
    public void setAttributes(Room room, String name) {
        room.setRoomName(name);
        roomRepository.save(room);
    }

    @BeforeEach
    public void makeRoom() {
        String name = "Yoga Room";
        Room room = new Room();
        setAttributes(room, name);
    }
    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
    }

    /**
     * Test successful room creation from makeRoom() and that we can read the
     * attributes in existing courses
     * @author Rafael Reis
     */
    @Test
    public void testReadWriteRoom() {
        String name = "Yoga Room";
        Room room = roomRepository.findRoomByRoomName(name);
        assertNotNull(room);
        assertEquals(name, room.getRoomName());
    }

    /**
     * Test goal: Tests the functionality of editing room name.
     * @author Rafael Reis
     */
    @Test
    public void testEditRoomAttributes() {
        Room room = roomRepository.findRoomByRoomName("Yoga Room");
        assertNotNull(room);

        // Define new attribute values
        String name = "Squash Room";
        setAttributes(room, name);

        // Ensure that the course with the original name is not found
        Room oldRoom = roomRepository.findRoomByRoomName("Yoga Room");
        assertNull(oldRoom);

        // Retrieve the modified course by its new name
        room = roomRepository.findRoomByRoomName(name);
        assertNotNull(room);
        assertEquals(name, room.getRoomName());
    }
}
