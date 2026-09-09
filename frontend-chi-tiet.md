# Chi tiết Frontend Android — App xem phim

## 1. Danh sách chức năng đầy đủ

### 1.1. Tài khoản & hồ sơ
- Đăng ký/đăng nhập bằng email, số điện thoại, hoặc Google/Facebook OAuth
- Quên mật khẩu / đặt lại mật khẩu qua OTP hoặc email
- Quản lý nhiều hồ sơ trong 1 tài khoản (tối đa N hồ sơ, có hồ sơ trẻ em giới hạn nội dung)
- Cài đặt: ngôn ngữ ứng dụng, ngôn ngữ phụ đề mặc định, chất lượng phát mặc định, quản lý thiết bị đã đăng nhập

### 1.2. Khám phá nội dung
- Trang chủ: banner nổi bật, "tiếp tục xem", các hàng theo thể loại/xu hướng/mới cập nhật
- Trang thể loại: lưới phim lọc theo thể loại, năm, quốc gia, sắp xếp (mới nhất/phổ biến/đánh giá)
- Tìm kiếm: gợi ý tức thời khi gõ, lịch sử tìm kiếm, tìm theo diễn viên/đạo diễn
- Trang chi tiết phim: poster, trailer, mô tả, diễn viên, đánh giá, phim liên quan, danh sách tập (nếu series)

### 1.3. Xem phim
- Phát trực tuyến với chất lượng thích ứng theo băng thông
- Chọn thủ công độ phân giải, ngôn ngữ âm thanh, phụ đề
- Tua nhanh/lùi, xem tiếp từ vị trí đã dừng
- Chế độ thu nhỏ (Picture-in-Picture), xoay ngang toàn màn hình
- Tải xuống xem offline, quản lý dung lượng đã tải
- Tự động phát tập tiếp theo (series)

### 1.4. Tương tác
- Thêm/xóa danh sách yêu thích ("Danh sách của tôi")
- Đánh giá sao, viết nhận xét (tuỳ chọn kiểm duyệt)
- Chia sẻ phim qua ứng dụng khác

### 1.5. Gói cước & thanh toán
- Xem, so sánh các gói cước
- Đăng ký gói, nhập phương thức thanh toán (thẻ, ví điện tử)
- Xem lịch sử giao dịch, gia hạn/hủy gói
- Thông báo trước khi gói hết hạn

### 1.6. Thông báo
- Thông báo đẩy: phim mới ra mắt, khuyến mãi, nhắc gia hạn
- Trung tâm thông báo trong app (danh sách đã đọc/chưa đọc)

---

## 2. Thiết kế giao diện chi tiết theo màn hình

### 2.1. Splash & Onboarding
- Logo hiển thị trong lúc app kiểm tra token đã lưu (`DataStore`/`EncryptedSharedPreferences`)
- Nếu chưa đăng nhập → 2-3 slide giới thiệu tính năng → màn đăng nhập
- Nếu đã đăng nhập → vào thẳng Home, không hiển thị lại onboarding

### 2.2. Đăng nhập / Đăng ký
- Layout: logo trên cùng, ô nhập email/số điện thoại, ô mật khẩu (có icon ẩn/hiện), nút "Đăng nhập" full-width
- Dưới nút chính: "Đăng nhập bằng Google" (icon + text), link "Quên mật khẩu?" và "Chưa có tài khoản? Đăng ký"
- Validate trực tiếp khi rời khỏi field (email sai định dạng, mật khẩu quá ngắn) — không đợi bấm submit mới báo lỗi toàn bộ

### 2.3. Home
Theo mockup ở trên:
- Header cố định: tiêu đề, icon tìm kiếm, thông báo, hồ sơ
- Banner carousel tự động chuyển (autoplay trailer câm tiếng sau 2-3s nếu muốn nâng cao)
- Các hàng ngang (`LazyRow` trong Compose), mỗi thẻ phim là poster + tên khi chạm giữ (long-press) hiện quick action (thêm yêu thích, xem chi tiết)
- Bottom navigation 4 tab: Trang chủ, Tìm kiếm, Tải xuống, Hồ sơ

### 2.4. Chi tiết phim
- Ảnh nền lớn phía trên (backdrop), gradient tối dần xuống để chữ dễ đọc
- Nút "Phát" lớn, nổi bật nhất màn hình
- Hàng icon phụ: Thêm vào danh sách, Đánh giá, Chia sẻ, Tải xuống
- Tab con nếu là series: "Tập phim" (danh sách theo mùa) / "Chi tiết" (mô tả, diễn viên) / "Liên quan"
- Danh sách tập dạng list dọc, mỗi item có thumbnail, số tập, thời lượng, thanh tiến độ nếu đã xem dở

### 2.5. Player
- Full-screen, ẩn thanh trạng thái hệ thống khi phát
- Overlay điều khiển tự ẩn sau 3 giây không tương tác: nút play/pause, thanh tua có preview thumbnail, nút chọn chất lượng/phụ đề/âm thanh (bottom sheet), nút thoát
- Góc phải trên: nút thu nhỏ (PiP)
- Khi gần hết tập (series): hiện thẻ "Phát tập tiếp theo trong 10s" có thể hủy

### 2.6. Tìm kiếm
- Ô tìm kiếm ở đầu, bàn phím hiện ngay khi vào màn hình
- Khi chưa gõ: hiển thị lịch sử tìm kiếm gần đây + gợi ý xu hướng
- Khi gõ: debounce 300ms rồi gọi API gợi ý, hiển thị kết quả dạng list có poster nhỏ + tên + năm

### 2.7. Hồ sơ / Cài đặt
- Đầu trang: avatar hồ sơ hiện tại, tên, nút chuyển hồ sơ
- Danh sách item dạng list: Lịch sử xem, Danh sách yêu thích, Quản lý tải xuống, Gói cước, Cài đặt, Đăng xuất

### 2.8. Gói cước
- Các card gói cước xếp dọc hoặc carousel ngang, card đang chọn có viền nổi bật
- Mỗi card: tên gói, giá, danh sách quyền lợi (số thiết bị, độ phân giải tối đa, có quảng cáo hay không)
- Nút "Đăng ký" cố định phía dưới màn hình

---

## 3. Cấu trúc dự án Android chi tiết (multi-module, Clean Architecture)

```
MovieApp/
├── app/
│   ├── src/main/java/com/example/movieapp/
│   │   ├── MovieApplication.kt
│   │   ├── MainActivity.kt
│   │   └── navigation/
│   │       ├── NavGraph.kt
│   │       └── Destinations.kt
│   └── build.gradle.kts
│
├── core/
│   ├── core-ui/
│   │   └── src/main/java/.../core/ui/
│   │       ├── theme/ (Color.kt, Typography.kt, Theme.kt)
│   │       ├── components/ (MovieCard.kt, LoadingIndicator.kt, ErrorView.kt, PrimaryButton.kt)
│   │       └── utils/ (ImageLoader.kt)
│   │
│   ├── core-network/
│   │   └── src/main/java/.../core/network/
│   │       ├── ApiClient.kt
│   │       ├── AuthInterceptor.kt
│   │       ├── TokenAuthenticator.kt
│   │       └── dto/ (các DTO chung: PagedResponseDto.kt, ErrorResponseDto.kt)
│   │
│   ├── core-database/
│   │   └── src/main/java/.../core/database/
│   │       ├── AppDatabase.kt
│   │       ├── dao/ (WatchHistoryDao.kt, FavoriteDao.kt, CachedMovieDao.kt)
│   │       └── entity/ (WatchHistoryEntity.kt, FavoriteEntity.kt)
│   │
│   ├── core-player/
│   │   └── src/main/java/.../core/player/
│   │       ├── PlayerManager.kt        (wrapper quanh ExoPlayer/Media3)
│   │       ├── DrmSessionManagerProvider.kt
│   │       └── DownloadTracker.kt
│   │
│   └── core-common/
│       └── src/main/java/.../core/common/
│           ├── Result.kt               (sealed class thành công/thất bại dùng chung)
│           ├── DispatcherProvider.kt
│           └── extensions/
│
├── domain/
│   └── src/main/java/.../domain/
│       ├── model/ (Movie.kt, Episode.kt, UserProfile.kt, Subscription.kt)
│       ├── repository/ (MovieRepository.kt — interface, AuthRepository.kt, ...)
│       └── usecase/
│           ├── auth/ (LoginUseCase.kt, RefreshTokenUseCase.kt)
│           ├── catalog/ (GetHomeSectionsUseCase.kt, SearchMoviesUseCase.kt)
│           ├── player/ (GetPlaybackUrlUseCase.kt, SaveProgressUseCase.kt)
│           └── subscription/ (GetPlansUseCase.kt, SubscribeUseCase.kt)
│
├── data/
│   └── src/main/java/.../data/
│       ├── remote/ (AuthApi.kt, CatalogApi.kt, StreamingApi.kt — interface Retrofit)
│       ├── local/ (mapper Entity ↔ Domain model)
│       ├── repository/ (implement các interface trong domain: MovieRepositoryImpl.kt)
│       └── mapper/ (MovieDtoMapper.kt, ...)
│
└── feature/
    ├── feature-auth/
    │   └── .../auth/
    │       ├── LoginScreen.kt
    │       ├── RegisterScreen.kt
    │       └── AuthViewModel.kt
    │
    ├── feature-home/
    │   └── .../home/
    │       ├── HomeScreen.kt
    │       ├── HomeViewModel.kt
    │       └── components/ (BannerCarousel.kt, MovieRow.kt)
    │
    ├── feature-detail/
    │   └── .../detail/
    │       ├── MovieDetailScreen.kt
    │       └── MovieDetailViewModel.kt
    │
    ├── feature-player/
    │   └── .../player/
    │       ├── PlayerScreen.kt
    │       ├── PlayerViewModel.kt
    │       └── components/ (PlayerControls.kt, QualitySheet.kt)
    │
    ├── feature-search/
    │   └── .../search/
    │       ├── SearchScreen.kt
    │       └── SearchViewModel.kt
    │
    ├── feature-profile/
    │   └── .../profile/
    │       ├── ProfileScreen.kt
    │       ├── SettingsScreen.kt
    │       └── ProfileViewModel.kt
    │
    └── feature-subscription/
        └── .../subscription/
            ├── PlansScreen.kt
            ├── PaymentScreen.kt
            └── SubscriptionViewModel.kt
```

**Nguyên tắc phụ thuộc**: `feature-*` phụ thuộc `domain` + `core-ui` + `core-common`; `data` implement interface của `domain` và phụ thuộc `core-network`/`core-database`; `domain` không phụ thuộc Android framework, chỉ chứa Kotlin thuần — giúp unit test dễ dàng và có thể tái sử dụng logic nếu sau này làm thêm bản Wear OS/TV.

Mỗi `feature-*` module áp dụng mẫu ViewModel dùng `StateFlow<UiState>`:

```kotlin
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val sections: List<HomeSection>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
```

---

## 4. Cấu trúc API chi tiết

Chuẩn chung: base URL `https://api.yourapp.com/v1`, mọi response bọc trong format thống nhất:

```json
{
  "success": true,
  "data": { },
  "error": null
}
```

Khi lỗi:

```json
{
  "success": false,
  "data": null,
  "error": { "code": "INVALID_CREDENTIALS", "message": "Email hoặc mật khẩu không đúng" }
}
```

Phân trang dùng chung một dạng:

```json
{
  "items": [ ],
  "page": 1,
  "page_size": 20,
  "total_items": 134,
  "total_pages": 7
}
```

### 4.1. Auth

**POST `/auth/register`**
```json
// Request
{ "email": "user@example.com", "password": "••••••", "full_name": "Nguyen Van A" }
// Response 201
{ "user_id": "uuid", "email": "user@example.com" }
```

**POST `/auth/login`**
```json
// Request
{ "email": "user@example.com", "password": "••••••" }
// Response 200
{
  "access_token": "eyJ...",
  "refresh_token": "eyJ...",
  "expires_in": 900,
  "user": { "id": "uuid", "email": "user@example.com" }
}
```

**POST `/auth/refresh`**
```json
// Request
{ "refresh_token": "eyJ..." }
// Response 200
{ "access_token": "eyJ...", "expires_in": 900 }
```

**POST `/auth/logout`** — thu hồi refresh token hiện tại. Body: `{ "refresh_token": "..." }` → `204 No Content`

### 4.2. Profiles

| Method | Path | Mô tả |
|---|---|---|
| GET | `/profiles` | Danh sách hồ sơ con trong tài khoản |
| POST | `/profiles` | Tạo hồ sơ mới `{ "name": "Bé An", "is_kids": true, "avatar_id": 3 }` |
| PATCH | `/profiles/:id` | Cập nhật tên/avatar |
| DELETE | `/profiles/:id` | Xóa hồ sơ |

### 4.3. Catalog

**GET `/catalog/home?profile_id=`**
```json
// Response
{
  "sections": [
    { "title": "Tiếp tục xem", "type": "continue_watching", "movies": [ {"id":"..","title":"..","poster_url":"..","progress_percent":42} ] },
    { "title": "Xu hướng", "type": "trending", "movies": [ ] }
  ]
}
```

**GET `/catalog/movies?genre=&year=&sort=popular&page=1&page_size=20`** — trả về dạng phân trang ở mục 4 trên.

**GET `/catalog/movies/:id`**
```json
{
  "id": "uuid", "title": "..", "description": "..", "backdrop_url": "..",
  "poster_url": "..", "release_year": 2026, "duration_minutes": 118,
  "genres": ["Hành động", "Khoa học viễn tưởng"],
  "type": "movie",  // hoặc "series"
  "seasons": [ { "season_number": 1, "episodes": [ {"id":"..","title":"Tập 1","duration_minutes":45} ] } ],
  "is_favorite": false,
  "average_rating": 4.3
}
```

**GET `/catalog/search?q=&page=`** — trả list rút gọn (id, title, poster_url, year) dùng cho gợi ý tức thời.

### 4.4. Streaming

**GET `/streaming/:movieId/play-url?episode_id=&quality=auto`**
```json
{
  "manifest_url": "https://cdn.../master.m3u8",
  "drm": { "license_url": "https://cdn.../license", "type": "widevine" },
  "resume_position_seconds": 372,
  "available_subtitles": [ {"lang":"vi","url":".."}, {"lang":"en","url":".."} ]
}
```
Trả về signed URL có hạn dùng (ví dụ 4 giờ) — client cần gọi lại API này nếu phiên phát quá lâu.

**POST `/streaming/:movieId/progress`**
```json
{ "episode_id": null, "position_seconds": 405, "duration_seconds": 7080 }
```
→ `204 No Content`, gọi định kỳ mỗi 10-15 giây và khi thoát player.

### 4.5. Favorites & Watch history

| Method | Path | Mô tả |
|---|---|---|
| GET | `/profiles/:id/favorites` | Danh sách yêu thích (phân trang) |
| POST | `/profiles/:id/favorites` | `{ "movie_id": "uuid" }` |
| DELETE | `/profiles/:id/favorites/:movieId` | Bỏ yêu thích |
| GET | `/profiles/:id/watch-history` | Lịch sử xem, sắp xếp theo `updated_at` giảm dần |

### 4.6. Subscription & Payment

**GET `/subscriptions/plans`**
```json
{ "plans": [ { "id": "p_basic", "name": "Cơ bản", "price": 79000, "currency": "VND", "duration_days": 30, "max_resolution": "720p", "max_devices": 1 } ] }
```

**POST `/subscriptions/subscribe`**
```json
// Request
{ "plan_id": "p_premium", "payment_method": "momo" }
// Response — trả link thanh toán để mở WebView hoặc app đối tác
{ "payment_url": "https://payment-gateway.../pay/xyz", "order_id": "uuid" }
```

**POST `/payments/webhook`** — endpoint nội bộ cho cổng thanh toán gọi về khi giao dịch hoàn tất, không dùng từ app.

**GET `/subscriptions/current`** — trả trạng thái gói hiện tại của user để app kiểm tra quyền xem nội dung premium.

### 4.7. Mã lỗi chuẩn hoá

| Code | HTTP status | Ý nghĩa |
|---|---|---|
| `INVALID_CREDENTIALS` | 401 | Sai email/mật khẩu |
| `TOKEN_EXPIRED` | 401 | Access token hết hạn, client cần gọi refresh |
| `FORBIDDEN_CONTENT` | 403 | Nội dung yêu cầu gói cước cao hơn |
| `RESOURCE_NOT_FOUND` | 404 | Phim/hồ sơ không tồn tại |
| `RATE_LIMITED` | 429 | Gọi API quá tần suất cho phép |
| `VALIDATION_ERROR` | 422 | Dữ liệu gửi lên không hợp lệ |

Client (Retrofit + OkHttp) nên có 1 lớp `ApiErrorMapper` để chuyển các code này thành thông điệp tiếng Việt hiển thị cho người dùng, và 1 `Authenticator` riêng để tự động gọi `/auth/refresh` khi gặp `TOKEN_EXPIRED`.

---

## 5. Trạng thái UI & xử lý lỗi mạng trong app

- Mỗi ViewModel expose `StateFlow<UiState>` gồm tối thiểu 3 trạng thái: `Loading`, `Success(data)`, `Error(message, canRetry)`.
- Composable quan sát state qua `collectAsStateWithLifecycle()`, hiển thị `CircularProgressIndicator`, nội dung, hoặc `ErrorView` có nút "Thử lại".
- Với danh sách phân trang: dùng Paging 3 (`PagingSource` gọi API theo `page`/`page_size`), tự động load thêm khi cuộn gần cuối.
- Toàn bộ lỗi mạng (timeout, không có kết nối) map về cùng một `NetworkException` để hiển thị thông điệp nhất quán, tách biệt với lỗi nghiệp vụ trả về từ server (dựa vào field `error.code`).
