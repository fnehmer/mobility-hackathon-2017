package dice;

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

	public String getNumberOfBikesAt(String name) {
		String stationName = "";
		JSONObject json = _geofoxapi.checkStation(name);
		JSONArray array = (JSONArray) json.get("results");
		JSONObject coordinate_json = (JSONObject) (JSONObject) ((JSONObject) array.get(0)).get("coordinate");

		String lon = String.valueOf(coordinate_json.get("x"));
		String lat = String.valueOf(coordinate_json.get("y"));

		stationName = _stadtradapi.getNumberOfBikesAt(lat, lon);
		return stationName;
	}
}
