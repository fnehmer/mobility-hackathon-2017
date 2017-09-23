package dice;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;

import java.util.Base64;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class main {

	public static void main(String[] args) {
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		String geofox_url = "http://api-hack.geofox.de/gti/public/init";
		OkHttpClient client = new OkHttpClient();

		// the name
		JSONObject theName = new JSONObject();
		theName.put("name", "Altona");
		theName.put("type", "STATION");

		// request json
		JSONObject requestJson = new JSONObject();
		requestJson.put("coordinateType", "EPSG_4326");
		requestJson.put("maxList", 1);
		requestJson.put("theName", theName);

		RequestBody body = RequestBody.create(JSON, requestJson.toString());
		String userSignature = HmacUtils.hmacSha1Hex("H4m$urgH13okt", requestJson.toString());
		System.out.println(userSignature);
		byte[] userSignature_byte = HmacUtils.hmacSha1("H4m$urgH13okt", requestJson.toString());
		System.out.println(new String(userSignature_byte, 0));

		Request request = new Request.Builder().url(geofox_url).post(body).addHeader("Accept", "application/json")
				.addHeader("Content-Type", "application/json").addHeader("geofox-auth-user", "moby-hack")
				.addHeader("geofox-auth-signature", userSignature_byte).build();
		Response response;
		try {
			response = client.newCall(request).execute();
			System.out.println(response.body().string());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
