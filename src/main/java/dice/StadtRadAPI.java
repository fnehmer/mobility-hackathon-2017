package dice;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

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
		String url = "https://api.deutschebahn.com/flinkster-api-ng/v1/bookingproposals?lat=" + lat + "&lon=" + lon
				+ "&expand=rentalobject,area&radius=100&limit=5&providernetwork=2";

		JSONObject response = makeRequest(url);
		JSONObject array = ((JSONObject) ((JSONObject) ((JSONArray) response.get("items")).get(0)).get("area"));
		String stationName = (String) array.get("name");

		return stationName;
	}

	private JSONObject makeRequest(String url) {
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

	public String getNumberOfBikesAt(String lat, String lon, String radius) {
		String url = "https://api.deutschebahn.com/flinkster-api-ng/v1/bookingproposals?lat=" + lat + "&lon=" + lon
				+ "&expand=rentalobject,area&radius=" + radius + "&limit=5&providernetwork=2";
		JSONObject response = makeRequest(url);
		String numberOfBikes = response.get("size") + "";

		return numberOfBikes;

	}

	public JSONArray getAvailableBikes(String lat, String lon, String radius) {
		String url = "https://api.deutschebahn.com/flinkster-api-ng/v1/bookingproposals?lat=" + lat + "&lon=" + lon
				+ "&expand=rentalobject,area&radius=" + radius + "&limit=5&providernetwork=2";
		JSONObject response = makeRequest(url);
		JSONArray results = (JSONArray) response.get("items");

		return results;
	}
}
