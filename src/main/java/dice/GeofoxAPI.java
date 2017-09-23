package dice;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import javax.xml.bind.DatatypeConverter;

@SuppressWarnings("restriction")
public class GeofoxAPI {
	private final OkHttpClient _client;
	private final MediaType _mediatype_json;
	private final String _geofoxApiEndpoint;

	public GeofoxAPI() {
		_client = new OkHttpClient();
		_mediatype_json = MediaType.parse("application/json; charset=utf-8");
		_geofoxApiEndpoint = "http://api-hack.geofox.de/gti/public/";
	}

	public JSONObject checkAddress(String address) {
		return checkName(address, "ADDRESS");
	}
	
	public JSONObject checkStation(String stationName) {
		return checkName(stationName, "STATION");
	}

	public JSONObject checkName(String name, String type) {
		String geofox_url = _geofoxApiEndpoint + "checkName";

		JSONObject theName = new JSONObject();
		theName.put("name", name);
		theName.put("type", type);

		JSONObject requestJson = new JSONObject();
		requestJson.put("coordinateType", "EPSG_4326");
		requestJson.put("maxList", 1);
		requestJson.put("version", 31);
		requestJson.put("theName", theName);

		Request request = buildRequest(geofox_url, requestJson);
		return executeRequestAndReturnJSONResponse(request);
	}
	
	public JSONObject getRoute() {
		String geofox_url = _geofoxApiEndpoint + "getRoute";

		JSONObject requestJson = new JSONObject();
		requestJson.put("language", "de");
		requestJson.put("version", 31);
		requestJson.put("start", createHauptbahnhofJSON());
		requestJson.put("dest", createBarmbekJSON());
		requestJson.put("time", createTimeJSON());
		requestJson.put("timeIsDeparture", true);
		requestJson.put("numberOfSchedules", 1);

		Request request = buildRequest(geofox_url, requestJson);
		return executeRequestAndReturnJSONResponse(request);
	}

	private String mkSignature(String password, byte[] requestBody) {
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

	private Request buildRequest(String api_endpoint, JSONObject requestJson) {
		RequestBody body = RequestBody.create(_mediatype_json, requestJson.toString());

		// passwort hash
		String userSignature = mkSignature("H4m$urgH13okt", requestJson.toString().getBytes());
		System.out.println(userSignature);

		return new Request.Builder().url(api_endpoint).post(body).addHeader("Accept", "application/json")
				.addHeader("geofox-auth-type", "HmacSha1").addHeader("Content-Type", "application/json")
				.addHeader("Geofox-Auth-User", "mobi-hack").addHeader("geofox-auth-signature", userSignature).build();
	}

	private JSONObject executeRequestAndReturnJSONResponse(Request request) {
		JSONObject responseJson = null;
		try {
			Response response;
			response = _client.newCall(request).execute();
			responseJson = new JSONObject(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	private JSONObject createHauptbahnhofJSON() {
		return new JSONObject().put("id", "Master:9910910").put("type", "STATION");
	}

	private JSONObject createBarmbekJSON() {
		return new JSONObject().put("id", "Master:70950").put("type", "STATION");
	}

	private JSONObject createTimeJSON() {
		return new JSONObject().put("date", "23.09.2017").put("time", "15:00");
	}
}
