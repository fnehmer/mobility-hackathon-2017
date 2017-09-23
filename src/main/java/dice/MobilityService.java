package dice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class MobilityService {
	private GeofoxAPI _geofoxapi;
	private StadtRadAPI _stadtradapi;

	public MobilityService() {
		_geofoxapi = new GeofoxAPI();
		_stadtradapi = new StadtRadAPI();
	}

	public String getStationName(String name) {
		String stationName = "";
		JSONObject json = _geofoxapi.checkStation(name);
		JSONArray array = (JSONArray) json.get("results");
		JSONObject coordinate_json = (JSONObject) (JSONObject) ((JSONObject) array.get(0)).get("coordinate");

		String lon = String.valueOf(coordinate_json.get("x"));
		String lat = String.valueOf(coordinate_json.get("y"));

		stationName = _stadtradapi.getStationName(lat, lon);
		return stationName;
	}

	public String getNumberOfBikesAt(String address, String radius) {

		JSONObject json = _geofoxapi.checkStation(address);
		JSONArray array = (JSONArray) json.get("results");
		JSONObject coordinate_json = (JSONObject) (JSONObject) ((JSONObject) array.get(0)).get("coordinate");

		String lon = String.valueOf(coordinate_json.get("x"));
		String lat = String.valueOf(coordinate_json.get("y"));

		String numberOfBikes = _stadtradapi.getNumberOfBikesAt(lat, lon, radius);
		// System.out.println("In " + address + " befinden sich derzeit " +
		// numberOfBikes + " Bikes.");
		return numberOfBikes;
	}

	public HashMap<String, String> lookForBikesAtRadius(String address, String radius) {
		JSONObject json = _geofoxapi.checkStation(address);
		JSONArray array = (JSONArray) json.get("results");
		JSONObject coordinate_json = (JSONObject) (JSONObject) ((JSONObject) array.get(0)).get("coordinate");

		String lon = String.valueOf(coordinate_json.get("x"));
		String lat = String.valueOf(coordinate_json.get("y"));

		HashMap<String, String> set = new HashMap<>();
		JSONArray bikes = _stadtradapi.getAvailableBikes(lat, lon, radius);
		HashMap<String, String> availableStations = new HashMap<>();
		for (int i = 0; i < bikes.length(); i++) {
			JSONArray coordinates = (JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) bikes.get(i))
					.get("area")).get("geometry")).get("position")).get("coordinates");

			String lon_current = Double.toString((double) coordinates.get(0));
			String lat_current = Double.toString((double) coordinates.get(1));

			String stationName = ((JSONObject) ((JSONObject) bikes.get(i)).get("area")).getString("name");
			if (!availableStations.containsKey(stationName)) {
				availableStations.put(stationName, _stadtradapi.getNumberOfBikesAt(lat_current, lon_current, "100"));
			}
		}

		return availableStations;
	}
}
