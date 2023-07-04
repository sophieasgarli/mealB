package stepDefinitions;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import requestBuilder.TokenRequestBuilder;
import utils.FunctionLibrary;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.jayway.jsonpath.JsonPath;

public class TokenAPI extends FunctionLibrary {
	String requestData;
	Response response;
	Scenario scenario; 

	@Before
	public void setUp(Scenario scenario) {
		this.scenario=scenario;
	}
	
	@Given("User creates request data with {string} and {string}")
	public void user_creates_request_data_with_and(String username, String password) {
		// Create token request data
		TokenRequestBuilder reqBuilder = new TokenRequestBuilder();
		reqBuilder.setUsernameOrEmailAddress(username);
		reqBuilder.setPassword(password);
		// convert request data object to String
		requestData = convertObjectToString(reqBuilder);
		// write request data to report.
		scenario.write(requestData);
		// attach request data to report
		scenario.embed(requestData.getBytes(), "application/json");
	}

	@Given("User submits request to TOKEN api")
	public void user_submits_request_to_TOKEN_api() {
		response = given().contentType(ContentType.JSON).body(requestData).when()
				.post(property.getProperty("TOKEN_URL"));
		// write response data to report
		scenario.write(response.asPrettyString());
		// attach response data to report
		scenario.embed(response.asPrettyString().getBytes(), "application/json");
	}

	@When("User validates if the status code is {int}")
	public void user_validates_if_the_status_code_is(int expectedStatusCode) {
		int actualStatusCode = response.getStatusCode();
		assertEquals(expectedStatusCode, actualStatusCode);
		// write request data to report
		scenario.write("Status code: " + response.getStatusCode());
	}

	@Then("User retrieves access token from response.")
	public void user_retrieves_access_token_from_response() {
		String token = JsonPath.read(response.asString(), "$.result.accessToken");
		assertNotNull(token);
		// write response data to report
		scenario.write(token);
	}

	
	@Then("User validates response schema")
	public void user_validates_response_schema() {
		response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/TokenSchema.json"));

	}

}
