package dice;

import java.util.HashMap;

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
		String result = mobi.getNumberOfBikesAt("Saarlandstraße", "100");
		String result1 = mobi.getNumberOfBikesAt("Stadthallenbrücke", "100");

		HashMap<String, String> set = mobi.lookForBikesAtRadius("Saarlandstraße", "1000");
		String outputtext = "";
		for (String street : set.keySet()) {
			String stock = set.get(street);

			String modifikation = "";
			if (stock.equals("1"))
				modifikation = " ein Fahrrad. \n";
			else
				modifikation += stock + " Fahrräder. \n";

			outputtext += "In der Straße " + street + " befinden sich gegenwärtig " + modifikation;

		}

		System.out.println(">>> " + outputtext);
	}
}
