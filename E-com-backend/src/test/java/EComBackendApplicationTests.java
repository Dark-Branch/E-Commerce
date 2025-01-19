import com.ecom.backend.EComBackendApplication;
import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Product;
import com.ecom.backend.model.User;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EComBackendApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EComBackendApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	private List<Product> testProducts;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
		productRepository.saveAll(List.of(
				new Product("Product 1", "Electronics", "Mobile", 100.0, "0001", 5),
				new Product("Product 2", "Electronics", "Laptop", 500.0, "0002", 5),
				new Product("Product 3", "Clothing", "Men", 50.0, "0003", 5)
		));
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void contextLoads() {
	}

	@Test
//	@DirtiesContext
//	@Disabled
	public void shouldCreteNewProduct() {
		Product product = new Product();
		List<String> tags = Arrays.asList("Electronics", "fan", "speed");

		product.setName("alutheka");
		product.setCategory("Electronics");
		product.setSubCategory("Fan");
		product.setPrice(100);
		product.setInventoryCount(6);
		product.setSellerId("0005");
		product.setTags(tags);


		ResponseEntity<Void> response = restTemplate.postForEntity("/products", product, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI productLocation = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate
				.getForEntity(productLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		System.out.println(getResponse.getBody());
		String id = documentContext.read("$.id");
		String name = documentContext.read("$.name");
		String category = documentContext.read("$.category");
		String subCategory = documentContext.read("$.subCategory");
		Double price = documentContext.read("$.price");
		String sellerId = documentContext.read("$.sellerId");
		int inventoryCount = documentContext.read("$.inventoryCount");
		List<String> responseTags = documentContext.read("$.tags");




		assertThat(id).isNotNull();
		assertThat(name).isEqualTo("alutheka");
		assertThat(category).isEqualTo("Electronics");
		assertThat(subCategory).isEqualTo("Fan");
		assertThat(price).isEqualTo(100.0);
		assertThat(sellerId).isEqualTo("0005");
		assertThat(inventoryCount).isEqualTo(6);
		assertThat(responseTags).containsExactly("Electronics", "Fan", "Speed");
		//These assertThat statements are part of the assertions in your test.
		// Assertions are used to verify that the actual output of your code
		// (in this case, the values returned from the API response) matches the expected values.
		// They help ensure that your code behaves correctly by checking that the data returned by the API (e.g., product details) is accurate.
	}

	@Test
	void testGetProductByCategoryWithoutSubCategory() {
		// Send GET request for "Electronics" category
		ResponseEntity<Product[]> response = restTemplate.getForEntity(
				"/products/category/Electronics?page=0&limit=10", Product[].class);

		// Verify response
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).hasSize(2);
		assertThat(response.getBody()[0].getName()).isEqualTo("Product 1");
		assertThat(response.getBody()[1].getName()).isEqualTo("Product 2");
	}

	@Test
	void testGetProductByCategoryWithSubCategory() {
		ResponseEntity<Product[]> response = restTemplate.getForEntity(
				"/products/category/Electronics?subCategory=Mobile&page=0&limit=10", Product[].class);

		// Verify response
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).hasSize(1);
		assertThat(response.getBody()[0].getName()).isEqualTo("Product 1");
	}

	@Test
	void testGetProductByCategoryInvalidPageOrLimit() {
		// Send GET request with invalid page and limit
		ResponseEntity<String> response = restTemplate.getForEntity(
				"/products/category/Electronics?page=-1&limit=0", String.class);

		// Verify response
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isEqualTo("Page must be >= 0 and limit > 0");
	}

	@Test
	public void testUpdateProductSuccess() throws Exception {
		// i know this will succeed because of previous test
		ResponseEntity<Product[]> response = restTemplate.getForEntity(
				"/products/category/Electronics?page=0&limit=10", Product[].class);
		assertThat(response.getBody()).isNotNull();

		Product product = response.getBody()[0];
		product.setPrice(63);

		HttpEntity<Product> request = new HttpEntity<>(product);

		ResponseEntity<Void> putResponse = restTemplate.exchange(
				"/products/{id}", HttpMethod.PUT, request, Void.class, product.getId());

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<Product> getResponse = restTemplate.getForEntity(
				"/products/{id}", Product.class, product.getId());
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		Product updatedProduct = getResponse.getBody();
		assertThat(updatedProduct).isNotNull();
		assertThat(updatedProduct.getId()).isEqualTo(product.getId());
		assertThat(updatedProduct.getPrice()).isEqualTo(63);
	}

	@Test
	public void testUpdateProductNotFound() throws Exception {
		Product product = new Product();
		product.setId("non-existent-id");
		product.setName("Non-Existent Product");
		product.setPrice(100.0);

		HttpEntity<Product> request = new HttpEntity<>(product);

		ResponseEntity<Void> putResponse = restTemplate.exchange(
				"/products/{id}", HttpMethod.PUT, request, Void.class, product.getId());

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}


	@Test
	@Disabled
	void shouldGetUser() {
		ResponseEntity<String> response = restTemplate.getForEntity("/users/{id}", String.class, "67762397adc0c8036d567a30");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		String id = documentContext.read("$.id");
		assertThat(id).isEqualTo("67762397adc0c8036d567a30");

		String userName = documentContext.read("$.userName");
		assertThat(userName).isEqualTo("John Doe");
	}

	@Test
	@Disabled
	void ShouldCreateNewUser() {
		User user = new User();
		user.setUserName("pakaya");
		user.setPassword("100");

		ResponseEntity<Void> response = restTemplate.postForEntity("/users", user, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI userLocation = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate
				.getForEntity(userLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		System.out.println(getResponse.getBody());
		String name = documentContext.read("$.userName");
		String password = documentContext.read("$.password");

		assertThat(name).isEqualTo("pakaya");
		assertThat(password).isEqualTo("100");
	}
}
