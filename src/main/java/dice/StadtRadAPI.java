package dice;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StadtRadAPI {

	private OkHttpClient _client;
	private final MediaType mediatype_json;

	public StadtRadAPI() {
		_client = new OkHttpClient();
		mediatype_json = MediaType.parse("application/json; charset=utf-8");
	}

	public JSONObject checkBike(String lat, String lon) {
		String url = "https://api.deutschebahn.com/flinkster-api-ng/v1/bookingproposals?lat=" + lat + "&lon=" + lon
				+ "&expand=rentalobject,area&radius=100&limit=5&providernetwork=2";

		// String geofox_url = "http://httpbin.org/anything";

		Request request = new Request.Builder().url(url).get().addHeader("Accept", "application/json")
				.addHeader("Authorization", "Bearer fcc6a36f18ba221547c26ead7cf73fb4").build();
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

	public String getStationName(String lat, String lon) {
		String url = "https://api.deutschebahn.com/flinkster-api-ng/v1/bookingproposals?lat=53.589104&lon=10.031621&expand=rentalobject,area&radius=100&limit=5&providernetwork=2";

		// String geofox_url = "http://httpbin.org/anything";

		Request request = new Request.Builder().url(url).get().addHeader("Accept", "application/json")
				.addHeader("Authorization", "Bearer fcc6a36f18ba221547c26ead7cf73fb4").build();
		Response response;

		String stationName = "";
		JSONObject responseJson = null;
		try {
			response = _client.newCall(request).execute();

			responseJson = new JSONObject(response.body().string());
			JSONObject array = ((JSONObject) ((JSONObject) ((JSONArray) responseJson.get("items")).get(0)).get("area"));
			stationName = (String) array.get("name");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return stationName;

	}

	public String getNumberOfBikesAt(String lat, String lon) {
		String url = "https://api.deutschebahn.com/flinkster-api-ng/v1/bookingproposals?lat=53.589104&lon=10.031621&expand=rentalobject,area&radius=100&limit=5&providernetwork=2";

		// String geofox_url = "http://httpbin.org/anything";

		Request request = new Request.Builder().url(url).get().addHeader("Accept", "application/json")
				.addHeader("Authorization", "Bearer fcc6a36f18ba221547c26ead7cf73fb4").build();
		Response response;

		String numberOfBikes = "";
		JSONObject responseJson = null;
		try {
			response = _client.newCall(request).execute();

			responseJson = new JSONObject(response.body().string());
			numberOfBikes = responseJson.get("size") + "";

		} catch (IOException e) {
			e.printStackTrace();
		}
		return numberOfBikes;

	}
}
