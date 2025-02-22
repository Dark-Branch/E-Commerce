import com.ecom.backend.EComBackendApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EComBackendApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EComBackendApplicationTests {
	@Test
	void contextLoads() {
	}
}
