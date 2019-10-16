package Utilities;

import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonParser;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Date;
import java.util.regex.Pattern;

public class Helper {
    public static String md5(String str){
        String result = "";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            BigInteger bigInteger = new BigInteger(1,digest.digest());
            result = bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Integer mqttMessageToInteger(MqttMessage mqttMessage){
        byte[] payload = mqttMessage.getPayload();
        Integer farmId =
                ((payload[0]|0x00000000)<<24) |((payload[1]|0x00000000)<<16) |((payload[2]|0x00000000)<<8)|((payload[3]|0x00000000));
        return farmId;
    }
    public static JSONObject mqttMessageToJsonObject(MqttMessage mqttMessage){
        JSONObject jsonObject = new JSONObject();
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(mqttMessage.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray mqttMessageToJsonArray(MqttMessage mqttMessage){
        JSONArray jsonArray = new JSONArray();
        JSONParser jsonParser = new JSONParser();
        try {
            jsonArray = (JSONArray) jsonParser.parse(mqttMessage.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static Integer convertDeviceIdToFarmId(Long deviceId){
        return (int)(deviceId&0x00000000FFFFFFFF);
    }
    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public static Timestamp getNow(){
        Date now = new Date();
        return new Timestamp(now.getTime());
    }
}

