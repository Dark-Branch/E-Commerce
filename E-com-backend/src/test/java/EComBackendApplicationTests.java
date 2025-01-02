import com.ecom.backend.EComBackendApplication;
import com.ecom.backend.model.Product;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EComBackendApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EComBackendApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
//	@DirtiesContext
	void shouldCreteNewProduct(){
		Product product = new Product();
		product.setName("alutheka");
		product.setPrice(100);

		ResponseEntity<Void> response = restTemplate.postForEntity("/products", product, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI productLocation = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate
				.getForEntity(productLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		System.out.println(getResponse.getBody());
		String id = documentContext.read("$.id");
		Double amount = documentContext.read("$.price");

		assertThat(id).isNotNull();
		assertThat(amount).isEqualTo(100);
	}

	@Test
	void shouldGetUser(){

	}

}
