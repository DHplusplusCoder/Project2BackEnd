package com.revature;

import com.revature.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest
class ECommerceApplicationTests {

	private static final String API_ROOT = "https://onlycorn.azurewebsites.net/";

	private User getExistingUser() {
		// Since there is not an endpoint for getting user info. This user should already be in the database already.
		User user = new User();
		user.setPassword("user");
		user.setEmail("user@user.com");
		user.setFirstName("User");
		user.setLastName("user");
		user.setRole(User.Role.User);
		return user;
	}

	@Test
	void contextLoads() {
	}

	// AUTH TESTS =======================================================================

	@Test public void whenLogInUserExist_thenOK() {
		final User user = getExistingUser();
		Response logInResponse = logIn(user);
		String email = logInResponse.jsonPath().get("email");
		String password = logInResponse.jsonPath().get("password");

		assertEquals(HttpStatus.OK.value(), logInResponse.getStatusCode());
		// Needs to return JSON with email and password for this to work.
		assertEquals(user.getEmail(), email);
		assertEquals(user.getPassword(), password);
	}

	@Test public void whenLogInUserNotExist_thenBAD_REQUEST() { // Change bad request to something else later?
		final User user = getExistingUser();
		user.setPassword("wrongPassword");
		Response logInResponse = logIn(user);

		assertEquals(HttpStatus.BAD_REQUEST.value(), logInResponse.getStatusCode()); //400
	}

	// - Session tests? Possibly integrate into login tests.

	// - (POST) /register should create a new user if user does not already exist

	// - (POST) /register should NOT create a new user if user already exists

	// PRODUCT TESTS =====================================================================

	@Test public void whenGetAllProductsNotLoggedIn_thenUNAUTHORIZED() {
		final Response response = RestAssured.get(API_ROOT + "/api/product");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
	}

	@Test public void whenGetAllProductsLoggedIn_thenOK() {
		final User user = getExistingUser();
		Response logInResponse = logIn(user);
		final Response response = RestAssured.get(API_ROOT + "/api/product");
		assertEquals(HttpStatus.OK.value(), logInResponse.getStatusCode(), response.getStatusCode());
	}

	// - (GET) /{id} should return product with that ID

	// - (GET) /featured should return products that are featured

	// - (GET) /sale should return product that are on sale

	// - (PUT) / should update existing product with given product

	// Purchase test?

	// ===================================================================================
	private Response logIn(User user) {
		//Log in with user credentials returned by getExistingUser()
		final Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(user)
				.post(API_ROOT + "/auth/login");
		return response;

		//response.jsonPath().get("id")
	}

}
