package Services;

import java.io.IOException;

public class StartUp {
	public static void main(String[] args) throws IOException {
		WetterService ws = new WetterService();

		//String response = ws.run("http://api.openweathermap.org/data/2.5/forecast?zip=20537,de&APPID=8468c94533dce20446d55f2df6110159");
		String result = ws.getWeather(9, 24, 23);
		
		System.out.println(result);
	}

}
