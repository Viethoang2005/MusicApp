# 🎵 MusicApp - Modern Android Music Player

MusicApp là một ứng dụng nghe nhạc hiện đại trên nền tảng Android, được xây dựng hoàn toàn bằng **Jetpack Compose** và tuân thủ các tiêu chuẩn phát triển Android mới nhất (Modern Android Development). Ứng dụng hỗ trợ phát nhạc trực tuyến, phát nhạc dưới nền (Background Audio) với thông báo điều khiển trên màn hình khóa, và giao diện Material 3 bắt mắt.

---

## ✨ Các Tính Năng Chính

1. **Phát Nhạc Dưới Nền (Background Playback)**:
   - Sử dụng **AndroidX Media3 (`MediaSessionService` & `ExoPlayer`)**.
   - Cho phép nhạc tiếp tục phát ngay cả khi người dùng thoát ứng dụng ra màn hình chính (Home) hoặc tắt màn hình khóa.
   - Hiển thị Media Notification trên thanh trạng thái và màn hình khóa với các nút điều khiển (Play, Pause, Next, Previous).

2. **Giao Diện Hiện Đại (Jetpack Compose & Material 3)**:
   - **HomeScreen**: Hiển thị danh sách bài hát được tải từ Firestore / Repository.
   - **MiniPlayer**: Thanh phát nhạc thu nhỏ xuất hiện ở cuối màn hình giúp điều khiển nhanh khi đang duyệt danh sách.
   - **PlayerScreen**: Màn hình phát nhạc chi tiết với hình ảnh album (Artwork), tiêu đề bài hát, tên nghệ sĩ, thanh tua nhạc (SeekBar), và các nút điều khiển trực quan.

3. **Kiến Trúc & Công Nghệ Chuẩn Clean / MVVM**:
   - **Dependency Injection**: Quản lý phụ thuộc bằng **Dagger Hilt**.
   - **State Management**: Sử dụng `StateFlow` và `Jetpack Compose State`.
   - **Image Loading**: Tải ảnh bìa bài hát mượt mà bằng **Coil Compose**.

---

## 🛠 Công Nghệ Sử Dụng (Tech Stack)

- **Language**: Kotlin 2.2+
- **UI Toolkit**: Jetpack Compose, Material 3
- **Architecture**: MVVM (Model - View - ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Audio Engine**: AndroidX Media3 (ExoPlayer, MediaSessionService)
- **Asynchronous**: Kotlin Coroutines & Flow
- **Network / Database**: Firebase Firestore, Retrofit, OkHttp
- **Image Loading**: Coil Compose

---

## 📂 Cấu Trúc Thư Mục (Project Structure)

```text
com.example.musicapp/
├── components/       # Các UI Component tái sử dụng (CardSongItem, MiniPlayer,...)
├── data/             # Models (Song, SongUIModel) & Repository (SongRepository)
├── di/               # Hilt Modules (PlayerModule, MyApplication)
├── player/           # Media3 Player Manager & MusicService (Background Playback)
├── theme/            # Material 3 Theme, Colors, Typography
├── ui/
│   ├── home/         # HomeScreen & HomeViewModel
│   └── play/         # PlayerScreen
├── MainActivity.kt   # Activity chính khởi chạy UI và Service
└── SongViewModel.kt  # ViewModel quản lý trạng thái phát nhạc toàn cục
```

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

1. **Yêu cầu hệ thống**:
   - Android Studio (phiên bản mới nhất, hỗ trợ Kotlin 2.0+ và Compose).
   - JDK 11 hoặc cao hơn.
   - Thiết bị Android hoặc Emulator chạy **Android 8.0 (API 26)** trở lên.

2. **Các bước thực hiện**:
   - Clone hoặc mở dự án trong Android Studio.
   - Đảm bảo tệp `google-services.json` đã được đặt đúng trong thư mục `app/` (để kết nối Firebase Firestore).
   - Chờ Gradle Sync hoàn tất các dependencies.
   - Kết nối thiết bị hoặc bật Emulator, sau đó nhấn **Run (`Shift + F10`)** để biên dịch và chạy ứng dụng.

---

## 📖 Hướng Dẫn Sử Dụng App

1. **Màn hình chính (Home)**:
   - Khi mở ứng dụng, danh sách bài hát sẽ được tải tự động từ cơ sở dữ liệu.
   - Chạm vào bất kỳ bài hát nào trong danh sách để bắt đầu phát nhạc.

2. **Điều khiển phát nhạc**:
   - **MiniPlayer**: Xuất hiện ở phía dưới màn hình khi có bài hát đang phát hoặc tạm dừng. Bạn có thể bấm Play/Pause nhanh hoặc chạm vào để mở màn hình chi tiết (`PlayerScreen`).
   - **PlayerScreen**: Cho phép xem chi tiết bài hát, tua nhạc đến thời điểm bất kỳ, chuyển bài trước/sau.

3. **Phát nhạc dưới nền**:
   - Bạn có thể nhấn nút **Home** hoặc tắt màn hình điện thoại, nhạc vẫn tiếp tục phát.
   - Sử dụng các nút điều khiển trực tiếp trên **Notification** ở thanh thông báo của hệ thống.
