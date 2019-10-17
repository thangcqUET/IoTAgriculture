import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestWeatherAPI {

    public static void GetInfoWeather() {
        System.out.println("Weather .....");
        // while (true) {
        Date datetime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = ft.format(datetime);
        String api_key = "LNrGryrjcdTBVNpPvJLCPWdg0lcZCQ4D";
        String id_city = "353412"; // Ha noi dua vao location api on https://developer.accuweather.com/accuweather-locations-api/apis
        String api_url = "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/" + id_city +"?apikey=" + api_key + "&language=en-us&details=true&metric=true";
        try{
            URL obj = new URL(api_url);
            HttpURLConnection url_connect = (HttpURLConnection) obj.openConnection();
            url_connect.setRequestMethod("GET");
            int responseCode = url_connect.getResponseCode();
            System.out.println(responseCode);
            if(responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println(response.toString());
                JSONParser decodeJson = new JSONParser();
                Object obj_json = decodeJson.parse(response.toString());
                JSONArray array = (JSONArray)obj_json;
                JSONObject my_response = (JSONObject)array.get(0);
                String weather = (String) my_response.get("IconPhrase");
                long rain_probably = (Long) my_response.get("RainProbability");
                long humidity = (Long) my_response.get("RelativeHumidity");
                JSONObject temp = (JSONObject) my_response.get("RealFeelTemperature");
                double temperature = (Double) temp.get("Value");
                String city = "Ha Noi";
                JSONObject wind = (JSONObject) my_response.get("Wind");
                JSONObject speed = (JSONObject) wind.get("Speed");
                double velocity = (Double) speed.get("Value");

                System.out.println(weather+", "+rain_probably+", "+humidity+", "+temperature);
            }
            //Thread.sleep(60*60*1000);
        } catch (Exception e) {
            System.out.println(e);
        }
        // }

    }

    public static void main(String[] args) {
        GetInfoWeather();
    }
}
