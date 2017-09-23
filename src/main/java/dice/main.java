package dice;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

import javax.xml.bind.DatatypeConverter;

public class main {

	public static void main(String[] args) {
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		String geofox_url = "http://api-hack.geofox.de/gti/public/init";
		// String geofox_url = "http://httpbin.org/anything";
		OkHttpClient client = new OkHttpClient();

		// the name
		JSONObject theName = new JSONObject();
		theName.put("name", "Altona");
		theName.put("type", "STATION");

		// request json
		JSONObject requestJson = new JSONObject();
		requestJson.put("coordinateType", "EPSG_4326");
		requestJson.put("maxList", 2);
		requestJson.put("theName", theName.toString());

		RequestBody body = RequestBody.create(JSON, requestJson.toString());

		// passwort hash
		String userSignature = mkSignature("H4m$urgH13okt", requestJson.toString().getBytes());
		System.out.println(userSignature);

		Request request = new Request.Builder().url(geofox_url).post(body).addHeader("Accept", "application/json")
				// .addHeader("Accept-Encoding", "gzip, deflate")
				// .addHeader("Accept-Language", "en-US,en;q=0.8,de;q=0.6")
				.addHeader("geofox-auth-type", "HmacSha1").addHeader("Content-Type", "application/json")
				.addHeader("Geofox-Auth-User", "mobi-hack").addHeader("geofox-auth-signature", userSignature).build();
		Response response;
		try {
			response = client.newCall(request).execute();
			System.out.println(new JSONObject(response.body().string()));
			System.out.println(response.body().string());

		} catch (IOException e) {
			e.printStackTrace();
		}

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

}
