# Ghi chú

## Hướng đi
- Thu thập dữ liệu: nhiệt độ, độ ẩm, ánh sáng
- -> Tìm mối liên hệ giữa độ ẩm đất và các yếu tố khác
- Dựa vào dự báo thời tiết để dự báo độ ẩm đất
- Điều khiển để độ ẩm đất tối ưu nhất trong hiện tại và tương lai thông qua mô hình điều khiển dự báo

## Chức năng cho người dùng thực hiện
### Trên ứng dụng
- đăng nhập, đăng ký tài khoản
- tạo Farm, Plot
- xem danh sách thiết bị có trong mạng
- thiết lập thiết bị vào Farm, Plot, vị trí thiết bị
- nhận dữ liệu từ thiết bị
- điều khiển thiết bị
### Trên Gateway (Đại diện cho Farm)
- cấu hình mạng (đăng nhập wifi)
- thiết lập tài khoản sở hữu
### Trên thiết bị
- cấu hình mạng (đăng nhập wifi)
- cấu hình chức năng, địa chỉ thiết bị

## Các vấn đề cần giải quyết cho Gateway
- Quản lý các thiết bị trong mạng Echonet Lite
  - Phát hiện thiết bị mới tham gia mạng
  - Phát hiện thiết bị rời khỏi mạng
  - Định danh thiết bị thuộc mạng Echonet Lite: sinh địa chỉ tự động hoặc bằng tay
- Trao đổi gói tin
  - gửi gói tin điều khiển
  - gửi gói tin Get dữ liệu
  - nhận gói tin phản hồi
    - phân tích gói tin phản hồi
    - gửi lên cho Server thông qua mqtt
- Mở chương trình cấu hình dễ dàng
  - cấu hình ID
  - cấu hình username, password để biết gateway thuộc user nào
  - Chương trình cấu hình có thể là 1 trang web nội bộ
- Tạo mới và thay thế gateway 
  (Gateway được định danh bởi FarmID lưu trữ trên nội bộ gateway và quản lý bởi server thông qua FarmID lưu trên Database)
  - Tạo mới
    - Xin FarmID
    - Đăng nhập, sau khi đăng nhập, Gateway sẽ được nhìn thấy bởi User sở hữu
    - Cấu hình cho gateway trên ứng dụng người dùng
  - Thay thế gateway cũ bằng gateway mới (chưa có FarmID)
    - Xin FarmID cho gateway mới
    - Đăng nhập, sau khi đăng nhập gateway mới sẽ được nhìn thấy bởi user sở hữu
    - Người dùng sẽ chọn thay thế gateway nay thay cho 1 gateway cũ
    - server xóa gateway mới, và gửi FarmID của gateway cũ cho gateway mới để gateway mới lưu lại
    - Cách 2: Cấu  hình trực tiếp farmID trên gateway: nếu farmID được set không có trong db, hoặc có trong db nhưng đang có 1 gateway khác dùng farmID đó thì sẽ lỗi
  - Thay thế Gateway cũ bằng gateway mới (đã có farmID từ trước)
    - xóa farmID ở gateway mới và làm như chưa có FarmID
    - How to xóa: xóa file config, trang web trên gateway
  - Những chức năng Web trên gateway:
    - Cấu hình FarmID, xóa FarmID
    - nhập Username, password để xác định user

  - Truyền tin tin cậy trên Gateway:
    - Khi nhận được 1 gói tin điều khiển từ server, gateway sẽ thực hiện gửi gói tin điều khiển tới thiết bị.
    - Hệ thống giải quyết được các vấn đề: Nếu gói tin bị mất mát, thì sau một thời gian sẽ gửi lại; Nếu gửi lại một số lần vẫn không phản hồi thì sẽ dừng và trả về false;

- Thực hiện cấu hình:
  - ping ip của gateway để có thể thực hiện cấu hình
## Vấn đề với Plot
- Plot Id không được tạo dựa trên các thiết bị vật lý như Farm và device mà sẽ tạo dựa trên logic
- PlotId sẽ có Id toàn cục
- Khi thêm 1 thiết bị vào thì bắt buộc phải set PlotId thiết bị mới được lưu vào DB. nếu chưa set thì sẽ set 1 PlotId mặc định
- Khi thêm 1 Farm vào thì mặc định tạo 1 plot

## Vấn đề với Device
- DeviceId sẽ dựa trên EOJ của thiết bị
- DeviceId cần không bị lặp: cấu hình bằng tay có thể bị lặp, cấu hình tự động (sẽ phát triển sau)
- 

## Vấn đề thiết kế DB
- Các ID của Farm, Plot, Device sẽ không dùng đến bộ khóa chính, mà dùng ID riêng
  - Ex: Sẽ không dùng khóa chính ntn: Farm: FarmId , Plot: PlotId+FarmId, Device: DeviceId+PlotId+FarmId mà là ntn: Farm:  FarmId, Plot: PlotId, Device: DeviceId. 
- DeviceId có thể ánh xạ sang EOJ+FarmId. Để 
## Vấn đề khác
- server chỉ cần biết Plot này có trạng thái ra sao -> không cần biết đó là thiết bị nào, chỉ cần biết Có một thiết bị cảm biến trên Plot đó và được định danh đúng vị trí (là Plot đó). Để định danh đúng vị trí cho thiết bị, Người dùng phải tự cấu hình cho thiết bị thông qua ứng dụng và sau đó vị trí sẽ được lưu lại trên thiết bị (PlotID).
- PlotID sẽ được sinh tự động trên Server
- 
## Thêm một thiết bị vào thư viện
1. Thêm một class thiết bị và cho các thuộc tính của thiết bị đó vào class
2. Thêm EOJ của thiết bị vào hàm newOtherDevice trong class EchoNode      
    ```java
   private static DeviceObject newOtherDevice(short echoClassCode, byte instanceCode)
   ```
   * EchoNode chứa các DeviceObject. Khi các DeviceObject được xác định, trong hàm trên EchoNode sẽ phân biệt DeviceObject này là Device nào.


## Cần làm
- [ ] Lấy dữ liệu từ cảm biến thông qua UDP
- [ ] Điều khiển máy bơm thông qua TCP
- [ ] Lấy dữ liệu thời tiết 


## Quy định cho gói tin json lưu trữ dư liệu cảm biến trên đường truyền MQTT
- airTemperature
- airHumidity
- soilTemperature
- soilMoisture

## Topic trên MQTT
- Các hoạt động trao đổi gói tin
  - Trao đổi thông tin định danh: FarmID, DeviceID, username, pasword
  - Trao đổi dữ liệu cảm biến: nhiệt độ, độ ẩm, ánh sáng
  - Trao đổi dữ liệu điều khiển: bật tắt bơm, thời gian bơm,...
  - Trao đổi dữ liệu thông tin thiết bị: trạng thái thiết bị, trạng thái gateway online hay offline
- Topic: /iot_agriculture
  - Trao đổi thông tin định danh: /identify
    - FarmID: /farm_id
      - lấy id từ server: /get
        - content: randomNumber, username, password
        - pub: gateway - sub: server
      - gửi id cho gateway: /supply/<"randomNumber">
        - content: farmID (-1 nếu sai tài khoản)
        - pub: server - sub: gateway
      - xin đổi farmID: /change
        - content: farmID mới, randomNumber, username, password 
        - pub: gateway - sub: server
      - phản hồi gói tin xin đổi farmID: /response_change/<"random number">
        - content: result
        - pub: server - sub: gateway
    - DeviceID: /device_id
      <!-- - gửi id cho server: /set
        - content: deviceID (global)
        - pub: gateway - sub: server -->
    <!-- - Username, password: /user_id
      - gán gateway cho người dùng: /verify
        - content: username, password, farmId
        - pub: gateway - sub: server
      - phản hồi việc xác minh: /response_verify/<"farmID">
        - content: result
        - pub: server - sub: gateway -->
  - Trao đổi trạng thái thiết bị: /status
    - của gateway: /gateway
      - online: /online
        - content: farmID
        - pub: gateway - sub: server
      - offline: /offline
        - content: LWT
        - pub: broker - sub: server
    - của device: /device[s]
      - online: /online
        - content: deviceID
        - pub: gateway - sub: server
      - offline: /offline
        - content: deviceID hoac [deviceIDs]
        - pub: gateway - sub: server
  - Trao đổi dữ liệu cảm biến: /monitoring
    - content: deviceID, data
    - pub: gateway - sub: server
  - Trao đổi dữ liệu điều khiển: /controlling/<"farmID">
    - content: deviceID, status
    - pub: server - sub: gateway

## Các lỗ hổng mạng
- Truyền Password trực tiếp trên đường truyền Internet
- Việc xác minh gateway chưa chặt chẽ: có thể bị giả mạo


## EOJ
- Switch 5 - 253
- Agriculture sensor 0 - 46

## API tương tác với server
- Bật tắt thiết bị
- Lấy dữ liệu
- Kiểu trả về: Json

## BUG
- Khi thêm ControllingDataSender vào thì DataCollector không nhận được gói tin Gateway offline khi Gateway tắt nữa
- Khi điều khiển máy bơm bật hoặc tắt thì máy bơm bị mất kết nối và rơi vào  trạng thái offline máy bơm sẽ không được bật hoặc tắt


## Dùng SSH
kết nối tới 1 server A: ssh user@address

kết nối tới 1 server B thông qua server A
- Tạo SOCKS5: ssh userA@addressA -D portSOCKS5. Lệnh này mở 1 cổng ở localhost để khi truy cập vào cổng này ta có thể truy cập đc vào máy A
- Tạo kết nối với máy B thông qua SOCKS5: ssh -o ProxyCommand="nc -x localhost:port_number %h %p" user@B. trong đó nc là một chương trình (k rõ ch/tr gì), %h là host, %p là port. Khi này ta có thể kết nối tới máy B thông qua máy A dựa vào port đã mở SOCKS5



lấy dữ liệu thời tiết
paper
các hệ thống tưới tự động ở VN
hệ thống mạng như thế nào

chuẩn bị cho phân tích dữ liệu
tìm hiểu các mô hình học máy
nối sim vs Pi
cài TCP hoặc truyền tin tin cậy  cho Gateway


# Tự khởi động service trên raspberry pi
Tạo file thực thi và cấp quyền
  - filename : gateway
  - content: 
```
cd /path/Gateway_jar/ 
java -jar Gateway.jar
```
Tạo file service trong /etc/systemd/system/
``` 
[Unit]

Description=Gateway systemd service.
After=network.target
[Service]

Type=simple

ExecStart=/bin/bash gateway

Restart=always

User=pi

[Install]

WantedBy=multi-user.target 
```
* Phần
  ```
  After = network.target
  Restart = always
  User = pi
  ```
  quan trọng để service có thể active sau khi reboot, tuy nhiên vẫn chưa rõ câu lệnh nào trong 3 câu lệnh trên có tác dụng để active.

Sau khi tạo file service thì systemctl daemon-reload để cập nhật file service. và chạy lệnh enable, start

### xem log service đang chạy: (service gateway)
```
journalctl -f -u gateway
- trong đó: -f là follow: xem theo thời gian thực, -u là unit
``` 


## bug
- mong muốn gateway gửi gói tin dò mạng ngay khi bật nhưng sau 1 thời gian nó ms gửi
- bật gateway lên trước và sau đó mới bật server thì xảy ra điều gì
- Khi bật đồng thời AP Wifi và Gateway thì Gateway không kết nối được với server

## Các câu lệnh thường dùng
- SSH:
  Truy cập vào SSH máy trong nhà trường từ bên ngoài và tạo một cầu nối để thực hiện qua máy đó với máy ảo làm server
  ```
    ssh sonnh@112.137.129.214 -D 15101
  ```
  Truy cập vào máy server thông qua cổng cầu nối 15101
  ```
    ssh -o ProxyCommand="nc -x localhost:15101 %h %p" root@10.10.1.99
  ```
  Copy file từ máy server về laptop đang sử dụng
  ```
    scp -o ProxyCommand="nc -x localhost:15101 %h %p" root@10.10.1.99:/var/lib/mysql-files/sensing.csv /home/caothang/Documents/research/Agriculture/IoTAgriculture/DB/
  ```