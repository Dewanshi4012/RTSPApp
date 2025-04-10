# RTSP Streaming App
This Android application allows users to stream RTSP (Real-Time Streaming Protocol) video feeds directly on their devices. Built using VLC's LibVLC library for seamless playback and FFmpegKit for recording, the app provides a simple yet powerful solution to handle RTSP streams.

## 📱 Features
- **🎥 RTSP Video Streaming –** Stream RTSP URLs directly on your Android device with minimal latency.
- **⏯️ Playback Controls –** Effortlessly Play, Pause, or Stop the live stream.
- **🎞️ Stream Recording –** Save live RTSP streams to your local storage in .mp4 format using FFmpegKit.
- **💡 User-Friendly Interface –** Clean UI with input field and essential control buttons.
- **🔒 Error Handling –** Get notified when an RTSP URL is invalid, unsupported, or inaccessible.

## ⚙️ Tech Stack
- **Language:** Kotlin
- **UI:** XML, SurfaceView
- **Streaming Library:** LibVLC (VLC for Android)
- **Recording Library:** FFmpegKit

## Screenshots 📷
<img src="https://github.com/user-attachments/assets/9206edb0-8427-44f3-9576-a8fd1fc8fd8c" alt="Description"  width="800" />

## 🚀 Getting Started
### 1️⃣ Prerequisites
- Android Studio
- Android SDK (API 21 or higher)
- LibVLC and FFmpegKit dependencies added to your project

### 2️⃣ Clone the Repository
```bash
  git clone (https://github.com/Dewanshi4012/RTSPApp.git)
  ```
### 3️⃣ Run the App
- Open the project in Android Studio
- Connect your Android device or start an emulator
- Click Run ▶️

## 📖 How to Use the App
- **Enter RTSP URL –** Paste a valid RTSP link in the input field.
- **Click "Play" –** The video stream will begin playing in the SurfaceView.
- **Pause or Stop –** Use the respective buttons to control playback.
- **Record Stream –** Hit the "Record" button to start saving the live stream locally in .mp4 format. Click again to stop recording.
- **💡Tip-** Ensure the RTSP URL you enter is publicly accessible or available on your local network.
For testing purposes, you may use the following RTSP link
- **Please note:** This stream is not owned or created by the developer(Me). Recording or redistributing its content may be subject to copyright restrictions. 
- ```bash
  rtsp://77.110.228.219/axis-media/media.amp 
  ```

