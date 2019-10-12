/*
 * Them cam bien nhiet do, do am dat va khong khi
 * 
*/
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>
#include <WiFiUdp.h>
#include <WiFiClient.h>

#include <Wire.h>
#include <BH1750FVI.h> //anh sang

#include "DHTesp.h"   // nhiet do - do am khong khi
DHTesp dht;
#define dhtpin D5

#include <SHT1x.h>    // nhiet do - do am dat
#define dataPin  D4
#define clockPin D3
SHT1x sht10(dataPin, clockPin);

uint8_t ADDRESSPIN = 13;
BH1750FVI::eDeviceAddress_t DEVICEADDRESS = BH1750FVI::k_DevAddress_H;
BH1750FVI::eDeviceMode_t DEVICEMODE = BH1750FVI::k_DevModeContHighRes;

// Create the Lightsensor instance
BH1750FVI LightSensor(ADDRESSPIN, DEVICEADDRESS, DEVICEMODE);


WiFiUDP UDPSender;
WiFiUDP UDPReceiver;

ESP8266WebServer server(80);



// ESV value
const byte SetI = 0x60; // request write value (no response required)
const byte SetC = 0x61; // request write value (response required)
const byte Get = 0x62; // request read value
const byte Set_Res = 0x71; // response write value
const byte Get_Res = 0x72; // response read value
const byte INF = 0x73;
const byte INF_REQ = 0x63;

const byte GET_echo = 0xD6;


// EPC value
//Device Object Super Class Properties
const byte EPC_airTemp = 0xF0;
const byte EPC_airHumidity = 0xF2;

const byte EPC_soilTemp = 0xF1;
const byte EPC_soilMoisture = 0xF3;

const byte EPC_lightLevel = 0xF4;


/*
   EEPROM
   0-31 : ssid
   32 - 96:pass
   quantity EOJ: 97
   EOJ code: X1-X2-X3: 98 -130
   future:131- 150
   Status: 161
   Power limit: 162- 163
   Location: 164
   Port ESP8266:
*/

const IPAddress ipMulti = {224, 0, 23, 0};
const int portMulti = 3610;

//Access point
//byte mac[6];
//WiFi.macAddress(mac);
//char ssid[17];
//sprintf(ssid, "%02X:%02X:%02X:%02X:%02X:%02X", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
//const char* passphrase = "12345678";

int sizeOfResponsePacket = 0;
String st;
String content;
int statusCode;

void setStatusDevice();
void sendEchoNode(IPAddress ipMulti, int portMulti );
void sendMessage(IPAddress ip, int port, byte* message);

union {
  float floatValue;
  unsigned long longValue;
  unsigned char bytes[4];
} thing;


class property_content {
  public:
    byte EPC = 0, PDC = 0;
    byte EDT[80] = {0};
};
class packet {
  public:
    byte TID[2] = {0}, SEOJ[3] = {0}, DEOJ[3] = {0}, ESV = 0, OPC = 0;
    property_content properties[5];
    packet() {

    }
    //     Create response by request
    packet(packet* req) {
      //      Serial.println("Da chay ham khoi tao111111111111111111111111111111111111111111111111111111111111111111111");
      for (int i = 0; i < 2; i++) {
        this->TID[i] = req->TID[i];
      }
      for (int i = 0; i < 3; i++) {
        this->DEOJ[i] = req->SEOJ[i];
      }
    }
    // byte[] -> packet
    void readPacket(byte packet[], int n) {
      byte SEOJ_copy[] = {packet[4], packet[5], packet[6]};
      for (int i = 0; i < 3; i++) {
        SEOJ[i] = SEOJ_copy[i];
      }
      byte DEOJ_copy[] = {packet[7], packet[8], packet[9]};
      for (int i = 0; i < 3; i++) {
        DEOJ[i] = DEOJ_copy[i];
      }
      byte TID_copy[] = {packet[2], packet[3]};
      for (int i = 0; i < 3; i++) {
        TID[i] = TID_copy[i];
      }
      ESV = packet[10];
      OPC = packet[11];
      int p = 12;
      for (int i = 0; i < OPC; i++) {
        properties[i].EPC = packet[p++];
        properties[i].PDC = packet[p++];
        for (int j = 0; j < properties[i].PDC; j++) {
          properties[i].EDT[j] = packet[p++];
        }
      }
    }
    // packet -> byte[]
    byte* writePacket() {
      byte head_packet[12] = {0x10, 0x81, TID[0], TID[1], SEOJ[0], SEOJ[1], SEOJ[2], DEOJ[0], DEOJ[1], DEOJ[2], ESV, OPC};
      int n = 0;
      for (int i = 0; i < OPC; i++) {
        n += (properties[i].PDC + 2);
      }
      n += 12;
      byte *packet = (byte*)malloc(n * sizeof(byte));
      for (int i = 0; i < 12; i++) {
        packet[i] = head_packet[i];
      }
      int i = 12;
      int j = 0;
      while (1) {
        if (j >= OPC) break;
        packet[i++] = properties[j].EPC;
        packet[i++] = properties[j].PDC;
        for (int t = 0; t < properties[j].PDC; t++) {
          packet[i++] = properties[j].EDT[t];
        }
        j++;
      }
      ////      //fixbug
      //      Serial.println("Write_Packet:");
      //      for(int i=0;i<n;i++){
      //        Serial.print(packet[i]);
      //        Serial.print(" ");
      //      }
      //      Serial.println();
      ////      //
      sizeOfResponsePacket = n;
      return packet;
    }
    void printPacket() {
      Serial.print("TID: ");
      Serial.print(TID[0]);
      Serial.print(" ");
      Serial.print(TID[1]);
      Serial.print(" ");
      Serial.print("SEOJ: ");
      Serial.print(SEOJ[0]);
      Serial.print(" ");
      Serial.print(SEOJ[1]);
      Serial.print(" ");
      Serial.print(SEOJ[2]);
      Serial.print(" ");
      Serial.print("DEOJ: ");
      Serial.print(DEOJ[0]);
      Serial.print(" ");
      Serial.print(DEOJ[1]);
      Serial.print(" ");
      Serial.print(DEOJ[2]);
      Serial.print(" ");
      Serial.print("ESV: ");
      Serial.print(ESV);
      Serial.print(" ");
      Serial.print("OPC: ");
      Serial.print(OPC);
      Serial.print(" ");
      int j = 0;
      while (1) {
        if (j >= OPC) break;
        Serial.print("EPC: ");
        Serial.print(properties[j].EPC);
        Serial.print(" ");
        Serial.print("PDC: ");
        Serial.print(properties[j].PDC);
        Serial.print(" ");
        for (int t = 0; t < properties[j].PDC; t++) {
          Serial.print("EDT: ");
          Serial.print(properties[j].EDT[t]);
          Serial.print(" ");
        }
        j++;
      }
      Serial.println();
    }
};

float airTemp(){
  delay(dht.getMinimumSamplingPeriod());
  return dht.getTemperature();
}
float airHumidity(){
  delay(dht.getMinimumSamplingPeriod());
  return dht.getHumidity();
}

float soilTemp(){
  return sht10.readTemperatureC();
}
float soilMoisture(){
  return sht10.readHumidity();
}

unsigned int lightLevel(){
  return LightSensor.GetLightIntensity();
}


void setup() {

  LightSensor.begin();

  dht.setup(dhtpin, DHTesp::DHT22);
 
  //DHT21 co cach doc tuong duowng DHT21  
  
  Serial.begin(115200);
  EEPROM.begin(512);
  Serial.println("**************************************");
  Serial.println("Startup");

  // read eeprom for ssid
  Serial.println("Reading EEPROM ssid");
  String esid = "";
  for (int i = 0; i < 32; ++i)
  {
    esid += char(EEPROM.read(i));//
  }
  Serial.print("SSID: ");
  Serial.println(esid);

  // read eeprom for pass
  Serial.println("Reading EEPROM pass");
  String epass = "";
  for (int i = 32; i < 96; ++i)
  {
    epass += char(EEPROM.read(i));
  }
  Serial.print("PASS: ");
  Serial.println(epass);


  // Turn off auto connect with wifi before
  WiFi.setAutoConnect(false);


  // Connect wifi
  if ( esid.length() > 1 ) {
    WiFi.begin(esid.c_str(), epass.c_str());
    if (testWifi()) {
      setupAP(1);
      //Node Profile Class: 0xD5 Instance list notification
      Serial.println("ECHO NODE");
      sendEchoNode(ipMulti, portMulti);
      // Open listening
      UDPReceiver.beginMulticast(WiFi.localIP(), ipMulti, portMulti);
      return;
    }
  }
  setupAP(0);
}

bool testWifi(void) {
  int c = 0;
  Serial.println("Waiting for Wifi to connect");
  while ( c < 20 ) {
    if (WiFi.status() == WL_CONNECTED) {
      return true;
    }
    delay(500);
    Serial.print('*');
    c++;
  }
  Serial.println();
  Serial.println("Connect timed out....");
  return false;
}

void launchWeb(int webtype) {
  Serial.println();
  Serial.print("Local IP: ");
  Serial.println(WiFi.localIP());
  Serial.print("SoftAP IP: ");
  Serial.println(WiFi.softAPIP());
  createWebServer(webtype);

  // Start the server
  server.begin();
  Serial.println("Server started");
}

void setupAP(int webtype) {
  byte mac[6];
  WiFi.macAddress(mac);
  char ssid[17];
  sprintf(ssid, "ESP8266_%02X:%02X:%02X:%02X", mac[0], mac[1], mac[2], mac[3]);
  const char* passphrase = "12345678";
  // Open AP
  WiFi.mode(WIFI_STA);
  //WiFi.disconnect();
  delay(100);
  WiFi.softAP(ssid, "12345678", 6);
  Serial.println("Soft AP");
  Serial.print("SSID: ");
  Serial.println(ssid);
  Serial.print("PASS: ");
  Serial.println(passphrase);
  // Open sever on AP
  Serial.println();
  Serial.print("Local IP: ");
  Serial.println(WiFi.localIP());
  Serial.print("SoftAP IP: ");
  Serial.println(WiFi.softAPIP());
  createWebServer(webtype);
  // Start the server
  server.begin();
  Serial.println("Server started");
}

void createWebServer(int webtype)
{
  if ( webtype == 0 ) {
    server.on("/", []() {
      IPAddress ip = WiFi.softAPIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      content = "<!DOCTYPE HTML>\r\n<html><title></title><body>Hello from ESP8266 at ";
      content += ipStr;
      content += "<p>";
      content += scanWifi();
      content += "</p><form method='get' action='setting'><table><tr><td>SSID: </td><td><input name='ssid' length=32></td></tr><tr><td>PASS: </td><td><input name='pass' length=64></td></tr><tr><td></td><td><input type='submit'></td></tr></table></form></body>";
      content += "</html>";
      server.send(200, "text/html", content);
    });

    server.on("/setting", []() {
      String qsid = server.arg("ssid");
      String qpass = server.arg("pass");
      if (qsid.length() > 0 && qpass.length() > 0) {
        Serial.println("clearing eeprom");
        for (int i = 0; i < 96; ++i) {
          EEPROM.write(i, 0);
        }
        Serial.println(qsid);
        Serial.println("");
        Serial.println(qpass);
        Serial.println("");

        Serial.println("writing eeprom ssid:");
        for (int i = 0; i < qsid.length(); ++i)
        {
          EEPROM.write(i, qsid[i]);
          Serial.print("Wrote: ");
          Serial.println(qsid[i]);
        }
        Serial.println("writing eeprom pass:");
        for (int i = 0; i < qpass.length(); ++i)
        {
          EEPROM.write(32 + i, qpass[i]);
          Serial.print("Wrote: ");
          Serial.println(qpass[i]);
        }
        EEPROM.commit();
        content = "{\"Success\":\"saved to eeprom... reset to boot into new wifi\"}";
        statusCode = 200;
      } else {
        content = "{\"Error\":\"404 not found\"}";
        statusCode = 404;
        Serial.println("Sending 404");
      }
      server.send(statusCode, "application/json", content);
    });

  } else if (webtype == 1) {

    server.on("/", []() {
      IPAddress ip = WiFi.localIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      content = "<!DOCTYPE HTML>\r\n<html><title>Update My Electric Energy Sensor Infomation</title>";
      content += "<body><center><h3>My Electric Energy Sensor Infomation</h3><h4>IP Address: " ;
      content += ipStr;
      content += "</h4><hr/><form action='config' method='get'><input type='submit' value='Cau hinh'></form>";
      content += "<form action='changewifi' method='get'><input type='submit' value='Chuyen WiFi'></form>\n\n";
      content += "</center></body></html>";
      server.send(200, "text/html", content);

    });

    server.on("/config", []() {
      IPAddress ip = WiFi.localIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      //      server.send(200, "application/json", "{\"IP\":\"" + ipStr + "\"}");
      content = "<html><head><title>Update My Electric Energy Sensor Infomation</title></head><body style='font-family:Verdana'><form action='update' method='get'>";
      content += "<center><h3>My Electric Energy Sensor Infomation</h3><h4>IP Address : " + ipStr + "</h4><hr/><table><tr>";
      //
      content += "<tr>";
      content += "<td>Number of EOJs: </td>";
      content += "<td><input type='text' name='NumOfEOJs' value='" + (String)EEPROM.read(97) + "'size='3'></td>";
      content += "</tr>";

      for (int i = 0; i < 11; i++) {
        content += "<tr>";
        content += "<td>EOJ Code : </td>";
        content += "<td><input type='text' name='X" + (String)(3 * i + 0) + "' value='" + (String)EEPROM.read(98 + 3 * i + 0) + "' size='3'></td>";
        content += "<td><input type='text' name='X" + (String)(3 * i + 1) + "' value='" + (String)EEPROM.read(98 + 3 * i + 1) + "' size='3'></td>";
        content += "<td><input type='text' name='X" + (String)(3 * i + 2) + "' value='" + (String)EEPROM.read(98 + 3 * i + 2) + "' size='3'></td>";
        content += "</tr>";
      }
      content += "<tr>";
      content += "<td><p style='font-size:11'>Example: 0x00 = 0; 0x22 = 34; 0x01 = 1</p></td>";
      content += "</tr>";
      //
      content += "<td>Operation Status : </td><td><input type='radio' value='ON' name='OperationStatus'";
      EEPROM.read(161) == 0x30 ? content += "checked" : "";
      content += ">ON<input type='radio' value='OFF' name='OperationStatus'";
      EEPROM.read(161) == 0x31 ? content += "checked" : "";
      content += ">OFF</td></tr><tr><td>Power Limited   : </td><td><input type='number' name='PowerLimit' value='";
      content += int((EEPROM.read(163) & 0xff) << 8) | EEPROM.read(162);
      content += "'/>&nbsp;W</td></tr><tr><td>Location : </td><td><input type='text' name='Location' value='" + (String)EEPROM.read(164) + "'/></td></tr><tr><td></td><td><input type='submit' name='Submit'/></td></tr></table></center></form>";
      content += "<form method='get' action='changewifi'><center><p><p>WiFi: <input type='submit' value='Choose'></center></form></body></html>";
      server.send(200, "text/html", content);
    });

    server.on("/changewifi", []() {
      IPAddress ip = WiFi.localIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      content = "<!DOCTYPE HTML>\r\n<html><title></title><body>Hello from ESP8266 at ";
      content += ipStr;
      content += "<p>";
      content += scanWifi();
      content += "</p><form method='get' action='setting'><table><tr><td>SSID: </td><td><input name='ssid' length=32></td></tr><tr><td>PASS: </td><td><input name='pass' length=64></td></tr><tr><td></td><td><input type='submit'></td></tr></table></form></body>";
      content += "</html>";
      server.send(200, "text/html", content);
    });

    server.on("/setting", []() {
      String qsid = server.arg("ssid");
      String qpass = server.arg("pass");
      if (qsid.length() > 0 && qpass.length() > 0) {
        Serial.println("clearing eeprom");
        for (int i = 0; i < 96; ++i) {
          EEPROM.write(i, 0);
        }
        Serial.println(qsid);
        Serial.println("");
        Serial.println(qpass);
        Serial.println("");

        Serial.println("writing eeprom ssid:");
        for (int i = 0; i < qsid.length(); ++i)
        {
          EEPROM.write(i, qsid[i]);
          Serial.print("Wrote: ");
          Serial.println(qsid[i]);
        }
        Serial.println("writing eeprom pass:");
        for (int i = 0; i < qpass.length(); ++i)
        {
          EEPROM.write(32 + i, qpass[i]);
          Serial.print("Wrote: ");
          Serial.println(qpass[i]);
        }
        EEPROM.commit();
        content = "{\"Success\":\"saved to eeprom... reset to boot into new wifi\"}";
        statusCode = 200;
      } else {
        content = "{\"Error\":\"404 not found\"}";
        statusCode = 404;
        Serial.println("Sending 404");
      }
      server.send(statusCode, "application/json", content);
    });

    server.on("/update", []() {
      bool testX = true;
      bool isChangeEOJ = false;

      String qOstatus = server.arg("OperationStatus");
      String qPower = server.arg("PowerLimit");
      String qLocation = server.arg("Location");
      String qNumOfEOJs = server.arg("NumOfEOJs");

      if (qNumOfEOJs.length() == 0) {
        testX = false;
      }
      String qX[qNumOfEOJs.toInt() * 3];
      for (int i = 0; i < qNumOfEOJs.toInt() * 3; i++) {
        String temp = "X" + (String)(i);
        qX[i] = server.arg(temp);
        if (qX[i].length() == 0) testX = false;
      }

      if (qOstatus.length() > 0 && qPower.length() > 0 && qLocation.length() > 0 && testX) {
        Serial.println("Update status and power limit");

        if (qOstatus.equals("ON")) {
          EEPROM.write(161, 0x30);
          digitalWrite(LED_BUILTIN, HIGH);
          //digitalWrite(Relay_Pin, HIGH);
          Serial.println("Device on");
        } else if (qOstatus.equals("OFF")) {
          EEPROM.write(161, 0x31);
          digitalWrite(LED_BUILTIN, LOW);
          //digitalWrite(Relay_Pin, LOW);
          Serial.println("Device off");
        } else {
          Serial.println("Update status failed");
        }

        if (EEPROM.read(162) != (qPower.toInt() & 0xff)) {
          EEPROM.write(162, qPower.toInt() & 0xff);
        }
        if (EEPROM.read(163) != ((qPower.toInt() >> 8) & 0xff)) {
          EEPROM.write(163, (qPower.toInt() >> 8) & 0xff);
        }
        if (EEPROM.read(97) != (qNumOfEOJs.toInt())) {
          EEPROM.write(97, qNumOfEOJs.toInt());
        }
        for (int i = 0; i < qNumOfEOJs.toInt() * 3; i++) {
          if (EEPROM.read(98 + i) != qX[i].toInt()) {
            EEPROM.write(98 + i, qX[i].toInt());
            isChangeEOJ = true;
          }
        }
        if (EEPROM.read(164) != qLocation.toInt()) {
          EEPROM.write(164, qLocation.toInt());
        }
        EEPROM.commit();
        // Send echo when EOJ change
        if (isChangeEOJ) {
          Serial.println("ECHO NODE CHANGE!");
          sendEchoNode(ipMulti, portMulti);
        }
      }
      server.sendContent("HTTP/1.1 301 OK\r\nLocation: /\r\nCache-Control: no-cache\r\n\r\n");
    });
  }
}

String scanWifi() {
  String s = "<ol>";
  int n = WiFi.scanNetworks();
  if (n == 0) {
    s += "No networks found";
  } else {
    for (int i = 0; i < n; ++i) {
      // Print SSID and RSSI for each network found
      s += "<li>";
      s += WiFi.SSID(i);
      s += " (";
      s += WiFi.RSSI(i);
      s += ")";
      s += (WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*";
      s += "</li>";
    }
  }
  s += "</ol>";
  delay(100);
  return s;
}

int count = 0;
int statusDevice = 0;
int sent = 0;
void loop() {
  server.handleClient();

  int noBytes = UDPReceiver.parsePacket();
  if (noBytes) {
    Serial.println("Recevied a packet........");
    byte request[noBytes];
    for (int i = 0; i < noBytes; i++) {
      request[i] = UDPReceiver.read();
    }

    Serial.print("Noi dung nhan duoc: ");

    for (int i = 0; i < noBytes; i++) {
      Serial.print(request[i]);
      Serial.print(", ");
    }
    Serial.println();

    packet req;
    // send a packet    packet req;
    req.readPacket(request, noBytes);
    req.printPacket();

    //set TID, DEOJ
    packet res(&req);
    //set SEOJ
    for (int i = 0; i < 3; i++) {
      res.SEOJ[i] = req.DEOJ[i];
    }
    byte ESV = req.ESV;
    Serial.print("OPC: ");
    Serial.println(req.OPC);
    IPAddress reqIP = UDPReceiver.remoteIP();
    int reqPort = UDPReceiver.remotePort();

    if (ESV == Get) {
      res.ESV = 0x72; //Get_Res
      for (int i = 0; i < req.OPC; i++) {
        byte EPC = req.properties[i].EPC;
        Serial.println();
        Serial.println(EPC);
        switch (EPC) {

          case EPC_airTemp: {
              res.properties[res.OPC].EPC = EPC;
              res.properties[res.OPC].PDC = 4;
              thing.floatValue = airTemp();
              for (int i = 0; i < res.properties[res.OPC].PDC; i++) {
                res.properties[res.OPC].EDT[i] = thing.bytes[i];
              }
              res.OPC++;
              Serial.print("airTemp is :");
              Serial.println(thing.floatValue);
              break;
            }
          case EPC_airHumidity: {
              res.properties[res.OPC].EPC = EPC;
              res.properties[res.OPC].PDC = 4;
              thing.floatValue = airHumidity();
              for (int i = 0; i < res.properties[res.OPC].PDC; i++) {
                res.properties[res.OPC].EDT[i] = thing.bytes[i];
              }
              res.OPC++;
              Serial.print("airHumidity is :");
              Serial.println(thing.floatValue);
              break;
            }

          case EPC_soilTemp: {
              res.properties[res.OPC].EPC = EPC;
              res.properties[res.OPC].PDC = 4;
              thing.floatValue = soilTemp();
              for (int i = 0; i < res.properties[res.OPC].PDC; i++) {
                res.properties[res.OPC].EDT[i] = thing.bytes[i];
              }
              res.OPC++;
              Serial.print("soilTemp is :");
              Serial.println(thing.floatValue);
              break;
            }

          case EPC_soilMoisture: {
              res.properties[res.OPC].EPC = EPC;
              res.properties[res.OPC].PDC = 4;
              thing.floatValue = soilMoisture();
              for (int i = 0; i < res.properties[res.OPC].PDC; i++) {
                res.properties[res.OPC].EDT[i] = thing.bytes[i];
              }
              res.OPC++;
              Serial.print("soilMoisture is: ");
              Serial.println(thing.floatValue);
              break;
            }

          case EPC_lightLevel: {
              res.properties[res.OPC].EPC = EPC;
              res.properties[res.OPC].PDC = 4;
              thing.longValue = lightLevel();
              for (int i = 0; i < res.properties[res.OPC].PDC; i++) {
                res.properties[res.OPC].EDT[i] = thing.bytes[i];
              }
              res.OPC++;
              Serial.print("lightLevel is: ");
              Serial.println(thing.longValue);
              break;
            }

          case 0xD5: {
              sendEchoNode(ipMulti, portMulti);
              Serial.println("Sended echo node");
              break;
            }


          default: {
              Serial.print("EPC Code not found");
              Serial.println(EPC);
            }
        }
      }
      Serial.println("Response: ");
      res.printPacket();
      byte *a = res.writePacket();
      sendMessage(reqIP, reqPort, a);
      free(a);
    } else if(ESV==INF_REQ){
      sendEchoNode(ipMulti, portMulti);
    } else{
      Serial.print("ESV Code Not Found :");
      Serial.println(ESV);
    }
  } else {
    delay(1000);
    count++;
    if (count > 60) {
      sendEchoNode(ipMulti, portMulti);
      count = 0;
    }


  }
}


void sendMessage(IPAddress ip, int port, byte* message) {
  //  //fixbug
  //  Serial.println("Before send:");
  //      for(int i=0;i<sizeOfResponsePacket;i++){
  //        Serial.print(message[i]);
  //        Serial.print(" ");
  //      }
  //  Serial.println();
  //  //
  UDPSender.beginPacket(ip, port);
  for (int i = 0; i < sizeOfResponsePacket; i++) {
    UDPSender.write(message[i]);
    Serial.print(message[i]);
    Serial.print(" ");
  }
  Serial.println();
  UDPSender.endPacket();
}

void sendEchoNode(IPAddress ipMulti, int portMulti ) {
  //Get MAC address
  byte mac[6];
  WiFi.macAddress(mac);
  //Node Profile Class: 0xD5 Instance list notification
  int lengthPacket = 15;
  int qualityEOJs = EEPROM.read(97);
  int PDC = qualityEOJs * 3 + 1;
  lengthPacket += (PDC - 1);
  byte echoMyNode[lengthPacket];
  byte echoMyNode_head[] = {0x10, 0x81, 0x00, 0x00, EEPROM.read(98), EEPROM.read(99), EEPROM.read(100), 0x0E, 0xF0, 0x01, 0x73, 0x02, 0xD5, PDC, qualityEOJs};
  for (int i = 0; i < sizeof(echoMyNode_head); i++) {
    echoMyNode[i] = echoMyNode_head[i];
  }

  for (int i = 0; i < lengthPacket - 15; i++) {
    echoMyNode[15 + i] = EEPROM.read(98 + i);
  }
  UDPSender.beginPacketMulticast(ipMulti, portMulti, WiFi.localIP());
  for (int i = 0; i < sizeof(echoMyNode); i++) {
    UDPSender.write(echoMyNode[i]);
    Serial.print(echoMyNode[i]);
    Serial.print(" ");
  }

  //send mac address
  UDPSender.write(0xD8); //EPC mac address
  UDPSender.write(0x06); //PDC
  for ( int i = 0; i < 6; i++ ) {
    UDPSender.write(mac[i]);
  }
  UDPSender.endPacket();
  Serial.print("MAC address: ");
  Serial.println(WiFi.macAddress());
  Serial.println("Send Echo Node and MAC address");
}
