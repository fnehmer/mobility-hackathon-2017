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
	private OkHttpClient _client;
	private final MediaType mediatype_json;

	public GeofoxAPI() {
		_client = new OkHttpClient();
		mediatype_json = MediaType.parse("application/json; charset=utf-8");
	}

	public JSONObject checkStation(String stationName) {
		String geofox_url = "http://api-hack.geofox.de/gti/public/checkName";
		// String geofox_url = "http://httpbin.org/anything";

		// the name
		JSONObject theName = new JSONObject();
		theName.put("name", stationName);
		theName.put("type", "STATION");

		// request json
		JSONObject requestJson = new JSONObject();
		requestJson.put("coordinateType", "EPSG_4326");
		requestJson.put("maxList", 1);
		requestJson.put("version", 16);
		requestJson.put("theName", theName);

		RequestBody body = RequestBody.create(mediatype_json, requestJson.toString());

		// passwort hash
		String userSignature = mkSignature("H4m$urgH13okt", requestJson.toString().getBytes());
		System.out.println(userSignature);

		Request request = new Request.Builder().url(geofox_url).post(body).addHeader("Accept", "application/json")
				.addHeader("geofox-auth-type", "HmacSha1").addHeader("Content-Type", "application/json")
				.addHeader("Geofox-Auth-User", "mobi-hack").addHeader("geofox-auth-signature", userSignature).build();
		Response response;

		JSONObject responseJson = null;
		try {
			response = _client.newCall(request).execute();
			responseJson = new JSONObject(response.body().string());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	public String mkSignature(String password, byte[] requestBody) {
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
}
