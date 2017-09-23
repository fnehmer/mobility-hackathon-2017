package dice;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("restriction")
public class GeofoxTest {

	private static final String GEOFOX_API_BASE_URL = "http://api-hack.geofox.de/gti/public/";

	@SuppressWarnings("unused")
	private static final String HTTPBIN_ANYTHING = "http://httpbin.org/anything";

	public static void main(String[] args) {
		getRouteTest();
		checkNameTest();
	}

	public static void checkNameTest() {
		String geofox_url = GEOFOX_API_BASE_URL + "checkName";

		// the name
		JSONObject theName = new JSONObject();
		theName.put("name", "Barmbek");
		theName.put("type", "UNKNOWN");

		// request json
		JSONObject requestJson = new JSONObject();
		requestJson.put("coordinateType", "EPSG_4326");
		requestJson.put("maxList", 1);
		requestJson.put("version", 31);
		requestJson.put("theName", theName);

		Request request = buildRequest(geofox_url, requestJson);
		executeRequestAndPrintResponse(request);
	}

	public static void getRouteTest() {
		String geofox_url = GEOFOX_API_BASE_URL + "getRoute";

		JSONObject requestJson = new JSONObject();
		requestJson.put("language", "de");
		requestJson.put("version", 27);
		requestJson.put("start", createHauptbahnhofJSON());
		requestJson.put("dest", createBarmbekJSON());
		requestJson.put("time", createTimeJSON());
		requestJson.put("timeIsDeparture", true);
		requestJson.put("numberOfSchedules", 1);

		Request request = buildRequest(geofox_url, requestJson);
		executeRequestAndPrintResponse(request);
	}

	public static String mkSignature(String password, byte[] requestBody) {
		final Charset passwordEncoding = Charset.forName("UTF-8");
		final String algorithm = "HmacSHA1";

		byte[] key = password.getBytes(passwordEncoding);
		SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
		Mac mac = null;
		try {
			mac = Mac.getInstance(algorithm);
			mac.init(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] signature = mac.doFinal(requestBody);

		return DatatypeConverter.printBase64Binary(signature);
	}
	
	public static Request buildRequest(String api_endpoint, JSONObject requestJson) {
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, requestJson.toString());

		// passwort hash
		String userSignature = mkSignature("H4m$urgH13okt", requestJson.toString().getBytes());
		System.out.println(userSignature);

		return new Request.Builder().url(api_endpoint).post(body).addHeader("Accept", "application/json")
				.addHeader("geofox-auth-type", "HmacSha1").addHeader("Content-Type", "application/json")
				.addHeader("Geofox-Auth-User", "mobi-hack").addHeader("geofox-auth-signature", userSignature).build();
	}

	public static void executeRequestAndPrintResponse(Request request) {
		try {
			OkHttpClient client = new OkHttpClient();
			Response response;
			response = client.newCall(request).execute();
			JSONObject responseJson = new JSONObject(response.body().string());
			System.out.println(responseJson.toString(2));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JSONObject createHauptbahnhofJSON() {
		return new JSONObject().put("id", "Master:9910910").put("type", "STATION");
	}

	public static JSONObject createBarmbekJSON() {
		return new JSONObject().put("id", "Master:70950").put("type", "STATION");
	}

	public static JSONObject createTimeJSON() {
		return new JSONObject().put("date", "23.09.2017").put("time", "15:00");
	}
}
