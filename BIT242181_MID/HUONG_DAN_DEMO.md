# VIDEO DEMO GIỮA KỲ — BIT242181 (khoảng 6–8 phút)

Đúng thứ tự các mục 1–6 ở trang 2 đề bài. Nên quay màn hình IntelliJ + trình duyệt + pgAdmin; giọng nói rõ ràng, không cần slide.

## 0. Chuẩn bị

- PostgreSQL đang chạy, `BIT242181_MID` đã có 3 bảng và 7 món demo.
- Trong trình duyệt dùng **cửa sổ ẩn danh** để chứng minh ngôn ngữ mặc định English (cookie cũ có thể giữ VI).
- Mở IntelliJ tại `src/main/java/...` để chỉ file Entity, `Controller`, `Service`, `Repository` khi thầy hỏi.
- Có sẵn URL ảnh thử: `https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=900`.

## 1. Tên database, 3 bảng, toàn bộ cột + Model (0:00–1:00)

1. Mở pgAdmin, chỉ Database `BIT242181_MID`.
2. Mở Query Tool và chạy 2 câu truy vấn `information_schema` trong README. Chỉ rõ tất cả `column_name` bắt đầu bằng `BIT242181_`.
3. Mở 3 file model Java: `DishCategory_BIT242181`, `Dish_BIT242181`, `DishImage_BIT242181`.
4. Nói: "Các Model đặt tên theo MSSV. Quan hệ Category một-nhiều Dish và Dish một-nhiều Image được ánh xạ bằng JPA `@ManyToOne` và `@OneToMany`."

## 2. List, lọc kết hợp và giữ điều kiện (1:00–2:20)

1. Mở `/dishes` → danh sách tối thiểu 5 món và 3 loại.
2. Gõ tên chứa `Phở` (hoặc `b`), chọn category; trạng thái Available; Min Price `30000` Max Price `100000`.
3. Sắp xếp giá giảm; nhấn Apply → chỉ kết quả phù hợp; giá, category, trạng thái, keyword vẫn giữ trên form.
4. Đổi sort thành Preparation Time ↑; nhấn Apply → thứ tự thay đổi.
5. Nói: "Dữ liệu lọc bằng `JpaSpecificationExecutor` trong PostgreSQL, không lấy tất cả về rồi lọc trong vòng lặp Java."
6. Thử sort giá tăng/giảm và lọc Unavailable để thấy món ngừng bán vẫn còn trên trang quản trị.

## 3. Phân loại chế biến + Create/Edit + Validation (2:20–3:30)

1. Chỉ món có thời gian ≤15 là Quick / Nhanh và món >15 là Regular / Thông thường.
2. Nhấn Add a dish, thử để trống Name / Price 0 / PreparationTime 0 → validation hiển thị.
3. Điền món mới, chọn category từ dropdown DB, Save → chuyển trang Detail.
4. Nhấn Edit, sửa giá, đổi sang loại khác, Save → dữ liệu cập nhật.
5. Thử trùng tên trong cùng một loại để thấy thông báo từ chối.

## 4. Quản lý nhiều ảnh và ảnh đại diện (3:30–4:25)

1. Ở Detail, dán URL ảnh, chọn Add image.
2. Thêm ảnh thứ 2; các ảnh xuất hiện đồng thời trong Gallery.
3. Chọn Make cover cho ảnh thứ 2 → ảnh lớn phía trên thay đổi.
4. Mở bảng `BIT242181_DishImages` trong pgAdmin; với một `DishId`, chỉ một `IsThumbnail = true`.
5. Nói: "Phần service chuyển ảnh đại diện trong transaction; PostgreSQL còn có unique index bảo vệ ở DB."

## 5. Ràng buộc và lỗi (4:25–5:10)

1. Trang Categories: thử xóa một loại đang có món → xuất thông báo từ chối, dữ liệu giữ nguyên.
2. Mở `/dishes/99999999` → trang lỗi 404.
3. Mở `/categories/99999999/edit` → 404.
4. Mở `/dishes?minPrice=100000&maxPrice=1000` → báo lỗi khoảng giá.
5. Tìm từ khóa không tồn tại → trạng thái danh sách trống có hướng dẫn.

## 6. Anh–Việt, mặc định English, đổi giữa các trang (5:10–6:45)

1. Mở cửa sổ ẩn danh mới → `/dishes` mặc định toàn bộ nút/nhãn trạng thái/bộ lọc bằng English.
2. Chọn EN → VI; kiểm tra menu, tiêu đề, nhãn, Quick/Nhanh, Available/Đang bán.
3. Truy cập Create, Edit, Detail và Categories → ngôn ngữ VI vẫn giữ.
4. Ở Create, thử để trống tên hoặc đặt Price = 0; kiểm tra validation tiếng Việt.
5. Vào `/dishes`, đặt nhiều filter; chọn EN/VI ngay trên kết quả → giữ nguyên bộ lọc và kết quả.
6. Chốt: "Tôi dùng CookieLocaleResolver lưu ngôn ngữ trong cookie và MessageSource cho nhãn, validation, thông báo."

## 7. Nộp

Nén mã nguồn + video theo đúng chỉ dẫn EDUX và nộp trong thời gian thầy quy định. Đề có điều khoản bài giống nhau bị hủy điểm: bạn cần hiểu, tự demo, và có thể thêm dữ liệu riêng / tùy chỉnh nội dung cho phù hợp bài của mình.
