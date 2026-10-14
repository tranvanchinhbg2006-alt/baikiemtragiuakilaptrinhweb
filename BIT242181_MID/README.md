# BIT242181_MID — QUẢN LÝ MÓN ĂN

Mini Project giữa kỳ môn Công nghệ Lập trình Web, theo đề **"ĐỀ KIỂM TRA GIỮA KỲ CNLT WEB — QUẢN LÝ MÓN ĂN"**.

- **MSSV:** `BIT242181`
- **Stack:** Java 21, Spring Boot 3.3.5, Spring MVC, Spring Data JPA/Hibernate (ORM), Thymeleaf, PostgreSQL.
- **Database:** `BIT242181_MID` (phân biệt chữ hoa chữ thường; tạo đúng tên có dấu ngoặc kép).
- **Bảng:** `BIT242181_DishCategories`, `BIT242181_Dishes`, `BIT242181_DishImages`.
- **Model:** `DishCategory_BIT242181`, `Dish_BIT242181`, `DishImage_BIT242181`.
- **Cổng mặc định:** `http://localhost:8080/`.

## 1. Phần mềm cần có

1. **JDK 21** (mở CMD chạy `java -version`).
2. **IntelliJ IDEA** (Community dùng được) với plugin Spring/Java, Maven; hoặc Maven CLI.
3. **PostgreSQL** + **pgAdmin 4**.
4. Internet trong lần chạy đầu để Maven tự tải các thư viện Java; ảnh demo lưu bằng URL bên ngoài cũng cần Internet để hiển thị.

> Lưu ý: Không cần cài Microsoft SQL Server. Project này dùng PostgreSQL đúng như yêu cầu.

## 2. Tạo database trong pgAdmin 4

**Cách dễ nhất:**

1. Mở pgAdmin 4, đăng nhập vào PostgreSQL.
2. Nhấn phải **Databases → Create → Database...**
3. Database: nhập **`BIT242181_MID`** đúng ký tự hoa/thường.
4. Owner: `postgres` (hoặc đúng user của bạn) → **Save**.
5. **Không phải tự chạy `schema.sql`**: Spring Boot sẽ tự tạo 3 bảng, các chỉ mục và dữ liệu demo khi kết nối lần đầu.

**Cách dùng Query Tool:** Mở Query Tool **trong database `postgres`**, chạy:

```sql
CREATE DATABASE "BIT242181_MID" ENCODING 'UTF8';
```

Nếu đã có database, không chạy lại lệnh trên. Lệnh cũng nằm trong `create_database.sql`.

**Vì sao phải dùng ngoặc kép?** PostgreSQL tự chuyển định danh không đặt ngoặc kép sang chữ thường. Đề yêu cầu tiền tố **`BIT242181`** nên script dùng định danh SQL trong `"..."` để giữ đúng chữ hoa/thường.

## 3. Cấu hình mật khẩu PostgreSQL

Mở file `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/BIT242181_MID}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```

Mật khẩu mặc định trong ví dụ là `postgres`. **Bạn phải sửa phần giá trị sau dấu `:` trong `DB_PASSWORD:postgres` thành mật khẩu PostgreSQL thực tế của bạn**, hoặc đặt biến môi trường `DB_PASSWORD` (khuyên dùng hơn khi chia sẻ project). Không nộp mật khẩu thật lên EDUX.

Ví dụ nếu mật khẩu PostgreSQL thực tế của bạn là `123456`:

```properties
spring.datasource.password=${DB_PASSWORD:123456}
```

## 4. Mở và chạy bằng IntelliJ IDEA

1. Giải nén ZIP `BIT242181_MID.zip`.
2. IntelliJ IDEA → **File → Open** → chọn thư mục `BIT242181_MID` (thư mục chứa `pom.xml`).
3. Chọn **Trust Project** nếu IDE hỏi; đợi Maven tải dependencies.
4. Trong `src/main/java/vn/edu/bit242181/mid/Bit242181Application.java`, nhấn Run ▶ ở hàm `main`.
5. Khi thấy dòng `Tomcat started on port 8080`, mở: **http://localhost:8080/**.
6. Spring Boot tự tạo `BIT242181_DishCategories`, `BIT242181_Dishes`, `BIT242181_DishImages` cùng **3 loại món và 7 món ăn** (seed khi các bảng tương ứng còn rỗng; khi đã có dữ liệu, các bản ghi đã sửa/xóa không bị tự động ghi đè). Ngoài ra script tạo các ảnh demo và các ràng buộc dữ liệu.

### Chạy bằng CMD thay cho IntelliJ (sau khi đã cài Maven)

```cmd
cd /d D:\duong-dan-toi\BIT242181_MID
mvn clean spring-boot:run
```

Nếu CMD báo `'mvn' is not recognized` thì Maven chưa được thêm PATH. Bạn vẫn có thể chạy bằng IntelliJ (IDE có Maven tích hợp).

Nếu cổng 8080 đang bị dùng, đặt biến môi trường `PORT=8081` hoặc chỉnh `server.port`.

## 5. Các chức năng đã hiện thực trong mã nguồn

- **Dish List:** `/dishes` — tìm một phần tên (không phân biệt hoa/thường), lọc đồng thời loại món, available/unavailable, giá min/max, sắp xếp giá tăng/giảm và thời gian chế biến tăng. Bộ lọc giữ lại sau submit nhờ GET + form binding; truy vấn WHERE/ORDER BY chạy trong DB bằng Spring Data JPA Specification.
- **Dish Create:** `/dishes/new` — bắt buộc tên, giá > 0, thời gian > 0, loại có thật, không trùng tên trong cùng loại.
- **Dish Detail:** `/dishes/{id}` — thông tin, Quick/Regular tính từ PreparationTime, xem **nhiều ảnh**.
- **Dish Edit:** `/dishes/{id}/edit` — sửa đầy đủ, chọn loại từ PostgreSQL, validation.
- **Dish Delete:** POST `/dishes/{id}/delete` — xóa món và toàn bộ ảnh của món.
- **Category List/Create/Edit/Delete:** `/categories` — KHÔNG xóa loại món đang được sử dụng.
- **Image Add:** nhập URL `http(s)`, đánh dấu đại diện; ảnh đầu tiên tự là ảnh đại diện.
- **Image Set Cover:** mỗi món tối đa một ảnh `IsThumbnail = true` (chặn cả ở PostgreSQL bằng unique partial index).
- **Image Delete:** tự chọn ảnh khác làm ảnh đại diện nếu xóa ảnh đại diện cũ và còn ảnh.
- **Error:** 404 với ID không tồn tại, lỗi giá và validation; danh sách trống có thông báo.
- **Language:** mặc định English, nút **EN/VI** ở thanh đầu trang, lưu ngôn ngữ bằng Cookie 30 ngày. Giữ nguyên các tham số GET khi chuyển ngôn ngữ.

### Ghi chú vận hành

- Các ảnh demo lấy URL Unsplash, nên cần Internet. Có thể dùng URL ảnh khác trực tiếp trên trang Detail.
- Validation phía server vẫn hoạt động khi trình duyệt bỏ qua kiểm tra HTML.
- Các trang sửa đổi dùng phương thức POST. Ứng dụng phục vụ bài thực hành, chưa tích hợp đăng nhập/ủy quyền để triển khai Internet công khai.
- Tạo 3 bảng/cột luôn dùng tiền tố MSSV; cột phụ tính Quick/Regular **không được tạo trong DB**.

## 6. Kiểm tra database trong pgAdmin

Trong pgAdmin → chọn `BIT242181_MID` → Query Tool, chạy:

```sql
-- Tên 3 bảng vật lý
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public' AND table_name LIKE 'BIT242181_%'
ORDER BY table_name;

-- Tên tất cả cột vật lý
SELECT table_name, column_name FROM information_schema.columns
WHERE table_schema='public' AND table_name LIKE 'BIT242181_%'
ORDER BY table_name, ordinal_position;

-- Dữ liệu mẫu
SELECT * FROM "BIT242181_DishCategories";
SELECT * FROM "BIT242181_Dishes";
SELECT * FROM "BIT242181_DishImages";
```

**Kết quả mong đợi:** 3 bảng, 14 cột (3 + 7 + 4), 3 loại món, 7 món, 7 ảnh (trong đó món Phở có 2 ảnh).

## 7. Kiểm tra tự động

Nếu đã cài Maven:

```cmd
mvn test
mvn package
```

Các test JUnit/Mockito trong `src/test/...` kiểm tra giá không hợp lệ, dùng truy vấn ở DB, chống trùng tên, ID loại không tồn tại và chặn xóa loại đang sử dụng.

**Không có server PostgreSQL trong môi trường đóng gói này nên hãy chạy thực tế trên máy bạn trước khi nộp.** Xem `CHECKLIST_10_DIEM.md` và `HUONG_DAN_DEMO.md`.

## 8. Cấu trúc thư mục

```text
BIT242181_MID/
├── pom.xml
├── create_database.sql
├── src/main/java/vn/edu/bit242181/mid/
│   ├── Bit242181Application.java
│   ├── model/           # 3 Entity theo MSSV
│   ├── repository/      # JpaRepository + JpaSpecificationExecutor
│   ├── service/         # nghiệp vụ, lọc và quản lý ảnh
│   ├── form/            # DTO cho validation và filter
│   ├── controller/      # MVC / xử lý lỗi / chuyển ngôn ngữ
│   ├── exception/
│   └── config/          # Cookie Locale, tiện ích View
├── src/main/resources/
│   ├── application.properties
│   ├── schema.sql      # tables + indices + sample data
│   ├── messages*.properties
│   ├── ValidationMessages*.properties
│   ├── templates/       # Thymeleaf English / Vietnamese
│   └── static/          # CSS + JS
└── src/test/            # Unit test
```

## 9. Luồng CRUD để giải thích cho giảng viên

`Browser (Thymeleaf View) → Controller → Service → Repository (Spring Data JPA ORM) → PostgreSQL → Repository → Controller(Model) → Thymeleaf View`.

- **List:** `DishController.list()` tạo `DishFilter`, gọi `DishService.search()`. Tầng service kết hợp `Specification` (WHERE) và `Sort` (ORDER BY), repository thực thi SQL thông qua ORM. Controller truyền `dishes` và `filter` vào `Model`. Thymeleaf duyệt `dishes` để tạo bảng và điền lại điều kiện lọc.
- **Create:** Form HTML POST tới `/dishes`. Spring MVC tự bind dữ liệu vào `DishForm`, Bean Validation kiểm tra, service kiểm tra category & tên trùng, JPA `save()` ghi DB, sau đó redirect sang trang Detail.
- **Edit:** GET `/dishes/{id}/edit` đọc entity, chuyển sang form. POST cập nhật entity gốc và lưu bằng JPA, không viết câu SQL thủ công trong Controller.
- **Images:** POST ảnh qua `ImageController`, `ImageService` kiểm tra URL, đổi ảnh đại diện trong một transaction. DB có partial unique index phòng trường hợp hai ảnh cùng được đặt đại diện.

**Đừng quên quay video đúng các bước trong `HUONG_DAN_DEMO.md`.**
