# CHECKLIST ĐỐI CHIẾU THANG 10 — BIT242181

Dựa trên trang 3 đề kiểm tra giữa kỳ. Cột kiểm thử là các thao tác cần tự xác nhận trên máy có PostgreSQL.

| Tiêu chí đề | Điểm | Mã nguồn / vị trí | Kiểm thử cần làm |
|---|---:|---|---|
| Database, 3 bảng, cột vật lý tiền tố MSSV; 3 tên Model | 1.5 | `schema.sql`, `model/*.java`, `create_database.sql` | Chạy truy vấn `information_schema` trong README, mở 3 entity Java |
| Model, validation, quan hệ | 1.5 | `model/`, `form/`, `service/`; FK và UNIQUE trong schema.sql | Thử name rỗng, price 0, time 0, loại không hợp lệ, trùng tên cùng loại |
| Create, Edit và dữ liệu liên kết | 1.0 | `DishController`, `DishService`, `dishes/form.html` | Thêm và sửa món, chuyển loại bằng dropdown dữ liệu DB |
| Tìm, lọc, khoảng giá, sắp xếp, giữ điều kiện | 1.5 | `DishFilter`, `DishService.search()`, `dishes/list.html` | Chọn nhiều bộ lọc cùng lúc; submit; giá tăng/giảm; thời gian tăng; giữ value |
| Quản lý ảnh và ảnh đại diện | 1.0 | `ImageService`, `ImageController`, `dishes/detail.html`, unique index | Thêm 2 ảnh, đổi ảnh đại diện, thử xóa ảnh |
| Phân loại thời gian, nghiệp vụ, lỗi | 1.0 | `ViewUtil`, `CategoryService`, `GlobalErrorHandler` | Món ≤15/ >15, xóa loại đang được dùng, sai dish/category/image ID, giá sai, không kết quả |
| Đa ngôn ngữ Anh/Việt xuyên suốt | 1.5 | `LocaleConfig`, `LocaleController`, `messages*.properties`, template | Mở incognito English, đổi sang VI, chuyển trang, thử validation, giữ filter |
| Giao diện và demo nộp bài | 1.0 | `static/css/style.css`, `HUONG_DAN_DEMO.md` | Kiểm tra desktop/mobile, quay video, nộp đúng hạn |
| **Tổng** | **10.0** | | |

**Điểm số là tiêu chí của thầy, KHÔNG phải cam kết tự động đạt 10.** Giảng viên chấm kết quả thực tế và khả năng giải thích của bạn.
