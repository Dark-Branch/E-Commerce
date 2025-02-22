import com.ecom.backend.EComBackendApplication;
import com.ecom.backend.model.Product;
import com.ecom.backend.repository.CartRepository;
import com.ecom.backend.repository.ProductRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EComBackendApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EComBackendApplicationTests {
	@Test
	void contextLoads() {
	}
}
