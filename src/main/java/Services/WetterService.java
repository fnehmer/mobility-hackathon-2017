package Services;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WetterService {
	OkHttpClient client = new OkHttpClient();

	/*
	 * Random OkHttp aus dem Weltweiten Netz
	 */
	public String run(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	/*
	 * Jonas sieht aus wie Floki aus Vikings!
	 */
	public String getWeather(int month, int day, int hour) throws JsonProcessingException, IOException, IllegalArgumentException {
		String timestamp = normalizeTimestamp(month, day, hour);
		String result;
		if(month > 12 || month < 1 || day < 1 || hour < 1 || hour > 24)
		{
			throw new IllegalArgumentException();
		}
		
		
		String jsonString = run(
				"http://api.openweathermap.org/data/2.5/forecast?zip=20537,de&APPID=8468c94533dce20446d55f2df6110159");

		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode json = mapper.readTree(jsonString);
				
		List<JsonNode> bar = json.findParents("dt_txt");
		
		for (JsonNode jn : bar)
		{
			if(jn.findValue("dt_txt").toString().equals(""+ "\""+timestamp+"\"") )
			{
				JsonNode weatherAll = jn.findValue("weather");
				String weather = weatherAll.findValue("main").toString();
				result = ""+weather.substring(1, weather.length() - 1);
				return result;
			}
		}
		return "fail";
	}
	
	/*
	 * Pan ist ein Pandaaa!!
	 */
	public String getUpcomingWeather()
	{
		Date date = new Date();
		int day = date.getDay();
		int month = date.getMonth();
		int hour = date.getHours() + 1;
		
		try {
			return getWeather(month, day, hour);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "fail";
	}

	/**
	 * Vinh ist ein monchichi!!!!!11!1
	 * 
	 * @param month
	 * @param day
	 * @param hour
	 * @return
	 */
	private String normalizeTimestamp(int month, int day, int hour) {
		int modifiedHour = (hour - (hour % 3));
		String monthString;
		String dayString;
		String hourString;

		if (month < 10) {
			monthString = "0" + month;
		} else {
			monthString = "" + month;
		}
		if (day < 10) {
			dayString = "0" + day;
		} else {
			dayString = "" + day;
		}
		if (modifiedHour < 10) {
			hourString = "0" + modifiedHour;
		} else {
			hourString = "" + modifiedHour;
		}
		return "2017-" + monthString + "-" + dayString + " " + hourString + ":00:00";
	}
}
