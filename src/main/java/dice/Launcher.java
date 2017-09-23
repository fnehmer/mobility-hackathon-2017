package dice;

import org.json.JSONArray;
import org.json.JSONObject;

public class Launcher {
	public static void main(String[] args) {
		GeofoxAPI geofoxapi = new GeofoxAPI();
		JSONObject json = geofoxapi.checkStation("Hamburger Straße");
		// System.out.println(json.toString(4));
		JSONArray array = (JSONArray) json.get("results");
		JSONObject coordinate_json = (JSONObject) (JSONObject) ((JSONObject) array.get(0)).get("coordinate");

		String lon = String.valueOf(coordinate_json.get("x"));
		String lat = String.valueOf(coordinate_json.get("y"));

		StadtRadAPI stadtradapi = new StadtRadAPI();
		JSONObject json_pan = stadtradapi.checkBike(lat, lon);
		// System.out.println(json_pan.toString(4));

		String stationName = stadtradapi.getStationName(lat, lon);
		// System.out.println(stationName);

		MobilityService mobi = new MobilityService();
		System.out.println(mobi.getNumberOfBikesAt("Saarlandstraße"));

	}
}
