package com.example.rtspapp

import android.app.PictureInPictureParams

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rtspapp.databinding.ActivityMainBinding
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.Session

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var libVLC: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private var isRecording = false
    private var recordingSession: Session? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val options = arrayListOf("--aout=opensles", "--audio-time-stretch", "-vvv")
        libVLC = LibVLC(this, options)

        mediaPlayer = MediaPlayer(libVLC)

        binding.playButton.setOnClickListener { playStream() }
        binding.pauseButton.setOnClickListener { pauseStream() }
        binding.stopButton.setOnClickListener { stopStream() }
        binding.pipButton.setOnClickListener{enterPipMode()}
        binding.recordButton.setOnClickListener { toggleRecording() }
    }

    private fun playStream() {
        val url = binding.rtspUrlInput.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a valid RTSP URL", Toast.LENGTH_SHORT).show()
            return
        }

        if (isPlaying) {
            Toast.makeText(this, "Stream is already playing", Toast.LENGTH_SHORT).show()
            return
        }

        val media = Media(libVLC, Uri.parse(url)).apply {
            addOption("--aout=opensles")
            addOption("--audio-time-stretch")
            addOption("-vvv")
        }

        mediaPlayer.media = media
        mediaPlayer.vlcVout.setVideoSurface(binding.videoView.holder.surface, binding.videoView.holder)
        mediaPlayer.vlcVout.setWindowSize(binding.videoView.width, binding.videoView.height)
        mediaPlayer.vlcVout.attachViews()
        mediaPlayer.play()

        isPlaying = true
        Toast.makeText(this, "Streaming started", Toast.LENGTH_SHORT).show()
    }

    private fun pauseStream() {
        if (isPlaying) {
            mediaPlayer.pause()
            Toast.makeText(this, "Stream paused", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No stream to pause", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopStream() {
        if (isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.vlcVout.detachViews()
            isPlaying = false
            Toast.makeText(this, "Stream stopped", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No stream to stop", Toast.LENGTH_SHORT).show()
        }
    }
    private fun toggleRecording() {
        if (!isRecording) {
            startRecording()
            binding.recordButton.text = "Stop Recording"
        } else {
            stopRecording()
            binding.recordButton.text = "Start Recording"
        }
    }
    private fun startRecording() {
        val url = binding.rtspUrlInput.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a valid RTSP URL", Toast.LENGTH_SHORT).show()
            return
        }

        val outputFilePath = "${getExternalFilesDir(null)?.absolutePath}/recorded_stream_${System.currentTimeMillis()}.mp4"
        val ffmpegCommand = "-rtsp_transport tcp -i \"$url\" -vcodec copy -acodec copy -f mp4 \"$outputFilePath\""

        Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show()
        isRecording = true

        recordingSession = FFmpegKit.executeAsync(ffmpegCommand) { session ->
            val returnCode = session.returnCode
            val logs = session.allLogsAsString
            runOnUiThread {
                isRecording = false
                binding.recordButton.text = "Start Recording"

                if (ReturnCode.isSuccess(returnCode)) {
                    Toast.makeText(this, "Recording saved: $outputFilePath", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Recording failed:\n$logs", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun stopRecording() {
        recordingSession?.cancel()
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(binding.videoView.width, binding.videoView.height)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        } else {
            Toast.makeText(this, "PiP not supported on this device", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        if (::libVLC.isInitialized) {
            libVLC.release()
        }
    }
}
