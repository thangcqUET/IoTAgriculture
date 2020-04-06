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
    private  String locateId="353412";
    private  ArrayList<WeatherForecastAtATime> weatherForecastAtATimes=new ArrayList<WeatherForecastAtATime>();
    private LocalTime curTime;
    public WeatherForecast() {
        locateId ="353412";
    }
    public WeatherForecast(String locateId) {
        this.locateId = locateId;
    }

    public ArrayList<WeatherForecastAtATime> getWeatherForecastAtATimes() {
        return weatherForecastAtATimes;
    }
    public  String getLocateId() {
        return locateId;
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
        if(curTime==null||(curTime.plusHours(1).compareTo(LocalTime.now())==-1&&curTime.getHour()!=23)) {
            weatherForecastAtATimes.clear();
            curTime = LocalTime.of(LocalTime.now().getHour(), 0);
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
    }
    public void updateTest(){
        if(curTime==null||(curTime.plusHours(1).compareTo(LocalTime.now())==-1&&curTime.getHour()!=23)) {
            weatherForecastAtATimes.clear();
            curTime = LocalTime.of(LocalTime.now().getHour(), 0);
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
    public class WeatherForecastAtATime{
        private Date dateTime;
        private Long epochDataTime;
        private String forecastStatus;
        private Boolean isDaylight;
        private Float temperature;
        private Float windSpeed;
        private Float relativeHumidity;
        private Byte rainProbability;
        private Byte precipitationProbability;
        private Float rainValue;
        private Byte cloudCover;

        public WeatherForecastAtATime() {
        }

        @Override
        public String toString() {
            return "dateTime: "+ dateTime+"\n"+
                    "epochDataTime: "+epochDataTime+"\n"+
                    "forecastStatus: "+forecastStatus+"\n"+
                    "isDaylight"+isDaylight+"\n"+
                    "temperature: "+temperature+"\n"+
                    "windSpeed: "+windSpeed+"\n"+
                    "relativeHumidity "+relativeHumidity+"\n"+
                    "rainProbability: "+rainProbability+"\n"+
                    "precipitationProbability: "+precipitationProbability+"\n"+
                    "rainValue: "+rainValue+"\n"+
                    "cloudCover: "+cloudCover+"\n";
        }

        public Date getDateTime() {
            return dateTime;
        }

        public Long getEpochDataTime() {
            return epochDataTime;
        }

        public String getForecastStatus() {
            return forecastStatus;
        }

        public Boolean getDaylight() {
            return isDaylight;
        }

        public Float getTemperature() {
            return temperature;
        }

        public Float getWindSpeed() {
            return windSpeed;
        }

        public Float getRelativeHumidity() {
            return relativeHumidity;
        }

        public Byte getRainProbability() {
            return rainProbability;
        }

        public Byte getPrecipitationProbability() {
            return precipitationProbability;
        }

        public Float getRainValue() {
            return rainValue;
        }

        public Byte getCloudCover() {
            return cloudCover;
        }

        private void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }
        private void setEpochDataTime(Long epochDataTime) {
            this.epochDataTime = epochDataTime;
        }
        private void setForecastStatus(String forecastStatus) {
            this.forecastStatus = forecastStatus;
        }
        private void setDaylight(Boolean daylight) {
            isDaylight = daylight;
        }
        private void setTemperature(Float temperature) {
            this.temperature = temperature;
        }
        private void setWindSpeed(Float windSpeed) {
            this.windSpeed = windSpeed;
        }
        private void setRelativeHumidity(Float relativeHumidity) {
            this.relativeHumidity = relativeHumidity;
        }
        private void setRainProbability(Byte rainProbability) {
            this.rainProbability = rainProbability;
        }
        private void setPrecipitationProbability(Byte precipitationProbability) {
            this.precipitationProbability = precipitationProbability;
        }
        private void setRainValue(Float rainValue) {
            this.rainValue = rainValue;
        }
        private void setCloudCover(Byte cloudCover) {
            this.cloudCover = cloudCover;
        }
        public WeatherForecastAtATime(Date dateTime, Long epochDataTime, String forecastStatus, Boolean isDaylight, Float temperature, Float windSpeed, Float relativeHumidity, Byte rainProbability, Byte precipitationProbability, Float rainValue, Byte cloudCover) {
            this.dateTime = dateTime;
            this.epochDataTime = epochDataTime;
            this.forecastStatus = forecastStatus;
            this.isDaylight = isDaylight;
            this.temperature = temperature;
            this.windSpeed = windSpeed;
            this.relativeHumidity = relativeHumidity;
            this.rainProbability = rainProbability;
            this.precipitationProbability = precipitationProbability;
            this.rainValue = rainValue;
            this.cloudCover = cloudCover;
        }
    }
}
