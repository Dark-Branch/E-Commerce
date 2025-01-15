```PHP
src/
└── main/
    ├── java/com/ecommerce/app/
    │   ├── config/              # Configuration files (e.g., CORS, security)
    │   ├── controller/          # REST API controllers
    │   ├── dto/                 # Data Transfer Objects -> for limit amount of details passed.
    │   ├── exception/           # Custom exception handling
    │   ├── model/               # Data models/entities -> object prototypes
    │   ├── repository/          # Interfaces for database operations
    │   ├── service/             # Business logic
    │   └── EcommerceApplication.java # Main Spring Boot application
    └── resources/
        ├── application.properties # App configuration
        └── data.sql               # (Optional) Pre-populate test data -> not used here

```

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.1/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.1/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.1/reference/web/servlet.html)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/3.4.1/reference/data/nosql.html#data.nosql.mongodb)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.1/reference/web/spring-security.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.1/reference/using/devtools.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

