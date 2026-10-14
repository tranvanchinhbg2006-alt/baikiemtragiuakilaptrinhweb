# BÁO CÁO KIỂM TRA PROJECT BIT242181

## Đã kiểm tra tĩnh

- `pom.xml` đọc được theo chuẩn XML.
- Các mẫu Thymeleaf đã kiểm tra cú pháp HTML cơ bản.
- Tất cả khóa dịch `#{...}` trong template đều có ở tiếng Anh và tiếng Việt.
- Entity JPA có 3 model, ánh xạ tên bảng và cột theo tiền tố MSSV `BIT242181_`.
- `schema.sql` có 3 bảng, khóa ngoại, unique index chống trùng tên món theo loại và unique partial index đảm bảo mỗi món tối đa một ảnh đại diện.
- Dữ liệu demo được thêm khi các bảng tương ứng rỗng; không ghi đè bản ghi hiện có. Không dùng khối PL/pgSQL DO để tránh lỗi Spring SQL ScriptUtils.

## Chưa thể xác minh trong môi trường này

- Chưa chạy `mvn test`/`mvn package`: môi trường đóng gói không có Maven và không thể tải dependencies từ Internet.
- Chưa kết nối được PostgreSQL để chạy CRUD thực tế.
- Chưa mở ứng dụng trong trình duyệt để kiểm tra kết quả hiển thị, ảnh URL từ Internet.

**Bạn cần mở bằng IntelliJ trên máy có Maven/JDK21/PostgreSQL rồi chạy `mvn test`, `mvn package` và demo theo `HUONG_DAN_DEMO.md` trước khi nộp.**
