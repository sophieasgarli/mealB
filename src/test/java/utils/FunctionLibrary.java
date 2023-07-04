package utils;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import requestBuilder.TokenRequestBuilder;

public class FunctionLibrary extends Base {

	private final String USERNAME = property.getProperty("username");
	private final String PASSWORD = property.getProperty("password");

	public String createTokenRequestData() {
		TokenRequestBuilder tokenObj = new TokenRequestBuilder();
		tokenObj.setUsernameOrEmailAddress(USERNAME);
		tokenObj.setPassword(PASSWORD);
		String payload = convertObjectToString(tokenObj);
		System.out.println(payload);
		return payload;
	}

	// convert request data to string
	public static String convertObjectToString(Object reqObject) {
		String strReqData = null;
		try {
			ObjectMapper objMap = new ObjectMapper();
			strReqData = objMap.writerWithDefaultPrettyPrinter().writeValueAsString(reqObject);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return strReqData;
	}

	public String getToken() {
		Response tokenResp = given().contentType(ContentType.JSON).body(createTokenRequestData()).when()
				.post(property.getProperty("TOKEN_URL"));

		tokenResp.prettyPrint();
		String token = JsonPath.read(tokenResp.asString(), "$.result.accessToken");
		return token;
	}

	public String generateToken(String requestData) {
		Response response = given().contentType(ContentType.JSON).body(requestData).when()
				.post(property.getProperty("TOKEN_URL"));
		return response.prettyPrint();
	}

}
