package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import requestBuilder.OtherExpenseBuilder;
import utils.FunctionLibrary;

public class OtherExpenseAPI extends FunctionLibrary {

	String requestPayload;
	Response response;
	Response deleteResponse;
	Scenario scenario;

	@Before
	public void setUP(Scenario scenario) {
		this.scenario = scenario;
	}
	
	@Given("User creates request data {string} , {string}, {string},{string} for OtherExpense API")
	public void user_creates_request_data_for_OtherExpense_API(String name, String amount, String expenseDateTime,
			String expenseNameID) throws JsonProcessingException {
		// Create Request data from Java Object
		OtherExpenseBuilder reqBuilder = new OtherExpenseBuilder();
		reqBuilder.setName(name);
		reqBuilder.setAmount(amount);
		reqBuilder.setExpenseDateTime(expenseDateTime);
		reqBuilder.setExpenseType("Other");
		reqBuilder.setOtherExpenseNameId(expenseNameID);
		// Convert Java Object to String
		ObjectMapper objMap = new ObjectMapper();
		requestPayload = objMap.writerWithDefaultPrettyPrinter().writeValueAsString(reqBuilder);
		System.out.println(requestPayload);
	}

	@Given("User submits POST request to {string} api")
	public void user_submits_POST_request_to_api(String url) {

		response = given().header("Authorization", "Bearer " + getToken()).body(requestPayload)
				.contentType(ContentType.JSON).when().post(property.getProperty("OtherExpense_URL"));
		response.prettyPrint();
	}
	
	@When("User validates if status code is {int}")
	public void user_validates_if_status_code_is(int expectedStatusCode) {
		int actualStatusCode = response.getStatusCode();
		assertEquals(expectedStatusCode, actualStatusCode);
		scenario.write("Status code: " + response.getStatusCode());
	}

	@Then("User validate if value of name in response is {string}")
	public void user_validate_if_value_of_name_in_response_is(String responseName) {
		String actualName = JsonPath.read(response.asString(), "$.result.name");
		System.out.println("Expected name: " + responseName + "---  Actual name: " + actualName);
		assertEquals(responseName, actualName);
	}

	@Then("User delete the expense with request to {string}")
	public void user_delete_the_expense_with_request_to(String url) {
		int expenseID = JsonPath.read(response.asString(), "$.result.id");
		deleteResponse = given().header("Authorization", "Bearer " + getToken()).when()
				.post(property.getProperty("Delete_URL") + "/" + expenseID);
		int statusCode = deleteResponse.getStatusCode();
		assertEquals(200, statusCode);

	}
	
}
