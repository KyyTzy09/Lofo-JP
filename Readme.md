# 📢 LoFo (Lost & Found) - Android App & Backend API

Aplikasi pelaporan dan pelacakan barang hilang berbasis digital untuk lingkungan sekolah. Dikembangkan sebagai solusi modern menggantikan metode konvensional (grup pesan manual) dengan memanfaatkan teknologi **QR Code (Client-Side Vector Fallback Rendering)** dan **Integrasi Lokasi berbasis Koordinat Geografis Reaktif**.

Aplikasi ini memisahkan data sensitif autentikasi dengan identitas sosial pengguna untuk menjamin privasi. Pengguna dapat mendaftarkan barang milik pribadi untuk mendapatkan **QR Code unik**. Ketika barang berstiker QR Code tersebut hilang dan ditemukan oleh orang lain, penemu cukup melakukan *scanning*, dan sistem otomatis memperbarui titik **Latitude & Longitude** terakhir barang tersebut ke database pemilik. Pemilik juga bisa mempublikasikan pengumuman kehilangan yang terintegrasi dengan data visual barang bawaan dari server AI secara asinkronus.

---

## 👥 Profil Kelompok & Pembagian Tugas (Kelompok 5)
Proyek ini disusun oleh siswa kelas XI program keahlian Pengembangan Perangkat Lunak dan Gim (RPL) SMK Telkom Purwokerto:

| No | Nama Anggota | Anggota / Role | Pembagian Tugas & Tanggung Jawab Resmi |
|:---|:---|:---|:---|
| 1 | **Albeynaro Javier W.** | Android Frontend Developer | Merancang arsitektur komponen UI, mengimplementasikan `LazyVerticalGrid` bento layout, menyusun *Empty State* card, dan mengintegrasikan animasi putar (*rotation*) reaktif pada TopAppBar. |
| 2 | **Fiky Alrasya** | Fullstack Developer & API Orchestrator | Membangun REST API Laravel (MySQL ULID), setup *Background Queue Worker* QR, mengonfigurasi *Retrofit Network Client* Android, dan mengelola state asinkronus di layer ViewModel. |
| 3 | **Kinara Aurelya Syahna** | System Analyst / QA Lead | Menyusun dokumen Analisis Kebutuhan Data, merancang diagram ERD database, membuat proposal formal, dan memimpin skenario pengujian unit (*Testing Report*) sistem. |
| 4 | **Nadya Almira Cordelia F.** | Android Form & Hardware Integration | Mengimplementasikan Jetpack Compose Form Validation, mengelola *Runtime Permissions Request* (Kamera & GPS), serta menyusun kerangka objek penampung data lokal sebelum dikirim ke API. |
| 5 | **Rangga Prayoga Wibowo** | AI & Voice Integration Engineer | Merancang arsitektur *Voice Command Modal*, integrasi modul audio lokal (Speech-to-Text Parser), dan menguji coba responsivitas mikrofon hardware pada device fisik client. |

---

## ⚡ Fitur Utama Sistem
* **Autentikasi Aman & Modern (Kebal Format)**: Menggunakan Laravel Sanctum dengan Primary Key berbasis **ULID** demi keamanan global ID. Sistem otomatis menormalisasi input nomor telepon pengguna (`08` atau `+62` otomatis dilebur menjadi `62` murni di database).
* **Manajemen Barang dengan Background Queue**: Pembuatan barang terintegrasi dengan **Laravel Queue & Jobs (`queue:work`)**. Proses *generate* dan upload QR Code dipindah ke background proses agar *response time* API instan di bawah 0.5 detik.
* **Siat Kritis Anti Race-Condition QR**: Android Client dilengkapi algoritma *Client-Side Fallback*. Jika data QR dari Cloudinary masih mengantre di background job server, Android secara otomatis merender gambar QR secara lokal menggunakan komponen Bitmap ZXing.
* **Swipe-to-Refresh Modern**: Halaman beranda menggunakan komponen Material 3 `PullToRefreshBox` yang terikat reaktif dengan state loading ViewModel untuk sinkronisasi data *real-time*.
* **Pelacakan Lokasi Terakhir (Scan-to-Update)**: Histori koordinat GPS barang ter-update otomatis begitu QR Code terpindai oleh penemu di lingkungan sekolah menggunakan integrasi peta OpenStreetMap lokal.
* **AI Voice Announcement**: Membuat laporan kehilangan instan via suara (*Speech-to-Text*) yang ditembak ke AI Python port 8001 menggunakan pemodelan Gemini/Grox untuk ekstraksi entitas judul, lokasi, dan tanggal kehilangan.
* **Scheduler Pengingat Otomatis (Cron Job)**: Menggunakan Laravel Schedule harian yang berjalan otomatis pada pukul 08:00 pagi untuk mendeteksi laporan mengendap $\ge$ 3 hari, lalu mengirimkan push notification via **Google Firebase Cloud Messaging (FCM) API v1 OAuth2 JWT Bearer**.

---

## 🛠️ Tech Stack & Arsitektur

### Mobile Client:
* **Language**: Kotlin 1.9+
* **UI Framework**: Jetpack Compose (Material 3 Component)
* **Asynchronous**: Kotlin Coroutines & Flow (State Management)
* **Networking**: Retrofit 2 & OkHttp3
* **Image Loader**: Coil (dengan SvgDecoder Factory)
* **QR Engine**: ZXing Embedded Core Generator
* **Push Notification**: Firebase Cloud Messaging (FCM) Service

### Backend API & Infrastructure:
* **Framework**: Laravel 11 (PHP 8.2+)
* **Database**: MySQL 8.0 (Relational Database)
* **Queue Driver**: Database Queue Worker Engine
* **Task Scheduling**: Laravel Schedule (di-trigger Cron Job VPS tiap menit)
* **Production Server**: VPS Linux (Ubuntu Server) dengan Caddy Reverse Proxy & SSL Automation

---

## 📦 Link Repository & Hasil Build APK

Silakan akses *source code* murni dan hasil kompilasi aplikasi siap install melalui tautan resmi di bawah ini:

* **Link Repository Backend (API Laravel)**:  
  `https://github.com/KyyTzy09/LoFo-API-Laravel`

* **Link Repository Android App (Kotlin)**:  
  `https://github.com/KyyTzy09/Lofo-JP`

* **Link Download APK Aplikasi (Siap Pakai)**:  
  [📥 Download LoFo_App_v1.0.apk (Direct Server)](https://lofo-api.my.id/download/lofo-app.apk)  
  [📥 Download LoFo_App_v1.0.apk (Mediafire Fallback)](https://www.mediafire.com/file/gx4csk4deraehsp/LoFo.apk/file)

> ⚠️ **Catatan Instalasi Perangkat**: Karena aplikasi ini dibangun khusus untuk keperluan proyek sekolah (PAS) Kelompok 5 dan belum dipublikasikan ke Google Play Store komersial, sistem Android akan memunculkan peringatan *Play Protect*. Cukup klik **"Detail / Selengkapnya"** lalu pilih **"Tetap Instal (Install Anyway)"**. Pastikan izin mic, kamera, dan lokasi diaktifkan agar seluruh fitur asisten suara AI dan pelacakan QR Code berjalan normal.