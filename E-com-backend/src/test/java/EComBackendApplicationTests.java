import com.ecom.backend.EComBackendApplication;
import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Product;
import com.ecom.backend.model.User;
import com.ecom.backend.repository.CartRepository;
import com.ecom.backend.repository.ProductRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EComBackendApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EComBackendApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Test
	void contextLoads() {
	}

	@Test
//	@DirtiesContext
	void shouldCreteNewProduct() {
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

	@Test
	public void testAddProductToCart() {
		// Create product
		Product product = new Product();
		product.setName("Wireless Mouse");
		product.setDescription("Ergonomic wireless mouse with adjustable DPI");
		product.setPrice(29.99);
		product.setInventoryCount(100);

		// save product
		ResponseEntity<Void> saveProductResponse = restTemplate.postForEntity("/products", product, Void.class);
		assertThat(saveProductResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// get product id
		URI productLocation = saveProductResponse.getHeaders().getLocation();
		ResponseEntity<Product> productResponse = restTemplate
				.getForEntity(productLocation, Product.class);
		assertThat(productResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(productResponse.getBody()).isNotNull();

		// Create an empty cart
		Cart cart = new Cart();
		cart.setUserId("user123");
		cart.setItems(new ArrayList<>());

		// Add the product as a CartItem
		Cart.CartItem cartItem = new Cart.CartItem();
		cartItem.setProductId(productResponse.getBody().getId()); // Reference the product by its ID
		cartItem.setQuantity(2);
		cart.getItems().add(cartItem);

		// save cart
		ResponseEntity<Void> saveCartResponse = restTemplate.postForEntity("/cart", cart, Void.class);
		assertThat(saveCartResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// get cart id
		URI cartLocation = saveCartResponse.getHeaders().getLocation();
		ResponseEntity<Cart> cartResponse = restTemplate
				.getForEntity(cartLocation, Cart.class);
		assertThat(cartResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(cartResponse.getBody()).isNotNull();

		// get cart
		Cart retrievedCart = cartResponse.getBody();
		assertThat(retrievedCart.getItems().size()).isEqualTo(1);
		assertThat(retrievedCart.getItems().get(0).getProductId()).isEqualTo(productResponse.getBody().getId());
		assertThat(retrievedCart.getItems().get(0).getQuantity()).isEqualTo(2);

	}
}
