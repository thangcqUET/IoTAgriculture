package model;

import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class WeatherForecast{
    private Integer weatherForecastId;
    private String locateId;
    private ArrayList<WeatherForecastAtATime> weatherForecastAtATimes=new ArrayList<WeatherForecastAtATime>();
    private LocalTime currentTime;
    public WeatherForecast() {
        locateId ="353412";
    }
    public WeatherForecast(String locateId) {
        this.locateId = locateId;
    }

    public WeatherForecast(Integer weatherForecastId, String locateId, LocalTime currentTime) {
        this.weatherForecastId = weatherForecastId;
        this.locateId = locateId;
        this.currentTime = currentTime;
    }

    public Integer getWeatherForecastId() {
        return weatherForecastId;
    }

    public LocalTime getCurTime() {
        return currentTime;
    }

    public  String getLocateId() {
        return locateId;
    }

    public ArrayList<WeatherForecastAtATime> getWeatherForecastAtATimes() {
        return weatherForecastAtATimes;
    }

    public void setLocateId(String locateId) {
        this.locateId = locateId;
    }

    public void setCurTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }

    public void setWeatherForecastAtATimes(ArrayList<WeatherForecastAtATime> weatherForecastAtATimes) {
        this.weatherForecastAtATimes = weatherForecastAtATimes;
    }

    private void analyse(JSONArray jsonArray) throws java.text.ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        for(Object object:jsonArray){
            JSONObject jsonObject = (JSONObject)object;
            WeatherForecastAtATime weatherForecastAtATime = new WeatherForecastAtATime();

            Date date = simpleDateFormat.parse((String) jsonObject.get("DateTime"));
            weatherForecastAtATime.setDateTime(date);

            weatherForecastAtATime.setEpochDataTime((Long)jsonObject.get("EpochDateTime"));

            weatherForecastAtATime.setForecastStatus((String)jsonObject.get("IconPhrase"));

            weatherForecastAtATime.setDaylight((Boolean)jsonObject.get("IsDaylight"));

            JSONObject temper = (JSONObject) jsonObject.get("Temperature");
            weatherForecastAtATime.setTemperature(((Double)temper.get("Value")).floatValue());

            weatherForecastAtATime.setRelativeHumidity(((Long)jsonObject.get("RelativeHumidity")).floatValue());

            weatherForecastAtATime.setRainProbability(((Long)jsonObject.get("RainProbability")).byteValue());

            weatherForecastAtATime.setPrecipitationProbability(((Long)jsonObject.get("PrecipitationProbability")).byteValue());
            JSONObject rain = (JSONObject) jsonObject.get("Rain");

            weatherForecastAtATime.setRainValue(((Double)rain.get("Value")).floatValue());

            weatherForecastAtATime.setCloudCover(((Long)jsonObject.get("CloudCover")).byteValue());

            JSONObject wind = (JSONObject) jsonObject.get("Wind");
            JSONObject windSpeed = (JSONObject) wind.get("Speed");
            Double windSpeedValue = (Double) windSpeed.get("Value");
            weatherForecastAtATime.setWindSpeed((windSpeedValue).floatValue());
            weatherForecastAtATimes.add(weatherForecastAtATime);
        }
    }
    private void analyse(String json){
        JSONParser decodeJson = new JSONParser();
        Object obj_json = null;
        try {
            obj_json = decodeJson.parse(json);
            JSONArray jsonArray = (JSONArray)obj_json;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            for(Object object:jsonArray){
                JSONObject jsonObject = (JSONObject)object;
                WeatherForecastAtATime weatherForecastAtATime = new WeatherForecastAtATime();

                Date date = simpleDateFormat.parse((String) jsonObject.get("DateTime"));
                weatherForecastAtATime.setDateTime(date);

                weatherForecastAtATime.setEpochDataTime((Long)jsonObject.get("EpochDateTime"));

                weatherForecastAtATime.setForecastStatus((String)jsonObject.get("IconPhrase"));

                weatherForecastAtATime.setDaylight((Boolean)jsonObject.get("IsDaylight"));

                JSONObject temper = (JSONObject) jsonObject.get("Temperature");
                weatherForecastAtATime.setTemperature(((Double)temper.get("Value")).floatValue());

                weatherForecastAtATime.setRelativeHumidity(((Long)jsonObject.get("RelativeHumidity")).floatValue());

                weatherForecastAtATime.setRainProbability(((Long)jsonObject.get("RainProbability")).byteValue());

                weatherForecastAtATime.setPrecipitationProbability(((Long)jsonObject.get("PrecipitationProbability")).byteValue());
                JSONObject rain = (JSONObject) jsonObject.get("Rain");

                weatherForecastAtATime.setRainValue(((Double)rain.get("Value")).floatValue());

                weatherForecastAtATime.setCloudCover(((Long)jsonObject.get("CloudCover")).byteValue());

                JSONObject wind = (JSONObject) jsonObject.get("Wind");
                JSONObject windSpeed = (JSONObject) wind.get("Speed");
                Double windSpeedValue = (Double) windSpeed.get("Value");
                weatherForecastAtATime.setWindSpeed((windSpeedValue).floatValue());
                weatherForecastAtATimes.add(weatherForecastAtATime);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    }
    private String getApiURL(String locateId){
        String api_key="LNrGryrjcdTBVNpPvJLCPWdg0lcZCQ4D";
        String api_url = "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/" + locateId +"?apikey=" + api_key + "&language=en-us&details=true&metric=true";
        return api_url;
    }
    public void update(){
        if(currentTime==null||(currentTime.plusHours(1).compareTo(LocalTime.now())==-1&&currentTime.getHour()!=23)) {
            weatherForecastAtATimes.clear();
            currentTime = LocalTime.of(LocalTime.now().getHour(), 0);
            String api_url = getApiURL(locateId);

            try {
                System.out.println(api_url);
                URL obj = new URL(api_url);
                HttpURLConnection url_connect = (HttpURLConnection) obj.openConnection();
                url_connect.setRequestMethod("GET");
                int responseCode = url_connect.getResponseCode();
                System.out.println(responseCode);
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    analyse(response.toString());
                }
                //Thread.sleep(60*60*1000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            return;
        }
    }//REAL
    public void update_(){//TEST
        if(currentTime==null||(currentTime.plusHours(1).compareTo(LocalTime.now())==-1&&currentTime.getHour()!=23)) {
            weatherForecastAtATimes.clear();
            currentTime = LocalTime.of(LocalTime.now().getHour(), 0);
            System.out.println("WeatherForeecast: update...");
            Object obj = null;
            try {
                obj = new JSONParser().parse(new FileReader("353412.json"));
                JSONArray jsonArray = (JSONArray) obj;
                analyse(jsonArray);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "\nweatherForecastId: "+weatherForecastId+
                "\nlocateId: "+locateId+
                "\ncurrentTime: "+currentTime+
                "\nweatherForecastAtATimes: \n"+
                weatherForecastAtATimes.toString();
    }
}
