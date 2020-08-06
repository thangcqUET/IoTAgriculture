import processing.serial.*; // import thư viện serial, tương tự như hàm include thôi :D
 
Serial serial; // Khởi tạo đối tượng Serial có tên là serial (phân biệt hoa thường nhé)
int portIndex = 0; // Lựa chọn vị trí cổng Serial trong danh sách
 
int[] intData={1, 1, 1, 1, 2, 2, 2, 2, 0x00, 0x00, 0x00, 0x2E, 0x01, 0x0E, 0xF0, 0x01, 0x73, 0x01, 0xD5, 0};//, 1, 0x00, 0x2E, 0x01};
byte[] byteData;
void setup() {
  size(100, 100);
  println(Serial.list());
  serial = new Serial(this, Serial.list()[Serial.list().length-1], 115200); 
  byteData = new byte[intData.length];
  for(int i=0;i<intData.length;i++){
    byteData[i]=(byte)intData[i];
    serial.write(intData[i]);
  }
//  for(int i=0;i<10;i++){
//    serial.write(256);
//  }
}
 
void draw() {
  //serial.write(1);
  if(serial.available()>0){
    int val = serial.read();
    print(byte(val));
  }
}
