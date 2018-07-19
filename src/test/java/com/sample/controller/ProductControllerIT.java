package com.sample.controller;

import com.sample.repository.ProductRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {

    @Autowired
    private ProductRepository productService;

    @LocalServerPort
    private int port;

    @Test
    public void shouldCreateNewProduct() throws Exception {
        String POST_NEW_PRODUCT = String.format("http://localhost:%s/products", port);

        Map<String, String> productProperties = new HashMap<>();
        productProperties.put("name", "jaquar");
        productProperties.put("base_price", "1.0");

        given()
                .contentType("application/json")
                .body(productProperties)
                .when().post(POST_NEW_PRODUCT)
                .then()
                .statusCode(201)
                .header("Location", Matchers.is("/products/1"));

    }

    @Test
    public void shouldReturnAllProducts() throws Exception {
        String GET_ALL_PRODUCTS = String.format("http://localhost:%s/products", port);

        when()
                .get(GET_ALL_PRODUCTS)
                .then()
                .statusCode(is(200))
                .body(Matchers.containsString("[{\"id\":1,\"name\":\"jaquar\",\"basePrice\":0.0}]"));
    }

    @Test
    public void shouldReturnOnlyOneProduct() throws Exception {
        String GET_BY_PRODUCT_ID = String.format("http://localhost:%s/products/1", port);

        when()
                .get(GET_BY_PRODUCT_ID)
                .then()
                .statusCode(is(200))
                .body(Matchers.containsString("{\"id\":1,\"name\":\"jaquar\",\"basePrice\":0.0}"));
    }

    @Test
    public void shouldUpdateProduct() throws Exception {
        String UPDATE_NEW_PRODUCT = String.format("http://localhost:%s/products/1", port);

        Map<String, String> car = new HashMap<>();
        car.put("name", "Audi");
        car.put("base_price", "2.0");

        given()
                .contentType("application/json")
                .body(car)
                .when().put(UPDATE_NEW_PRODUCT)
                .then()
                .statusCode(204);

    }

    @Test
    public void shouldDeleteProductById() throws Exception {
        String DELETE_BY_PRODUCT_ID = String.format("http://localhost:%s/products/1", port);

        when()
                .delete(DELETE_BY_PRODUCT_ID)
                .then()
                .statusCode(is(200));
    }

}