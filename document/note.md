# Ghi chú
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