import com.movie.seats.SeatAbstractDaoUnitTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
@Testcontainers
public class TestContainerTest extends SeatAbstractDaoUnitTest {

    @Test
    void canStartPostgresDB(){
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}