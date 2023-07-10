package com.example.basicmusicapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.MainActivity
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.models.Song

class MusicService : Service() {
    private val binder = MyBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentSong: Song? = null
    var isPlayingServiceMedia = true
    override fun onBind(intent: Intent?): IBinder? {
        var actionMusic = intent!!.getIntExtra("action_music_service", 0)
        handleActionMusic(actionMusic)
        return binder
    }

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    fun setSong(song: Song) {
        if (currentSong == null) {
            currentSong = song
            mediaPlayer = MediaPlayer.create(baseContext, currentSong!!.fileSong)
            mediaPlayer!!.start()
            isPlayingServiceMedia = true
            sendNotification(currentSong)
        } else {
            currentSong = song
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = MediaPlayer.create(baseContext, currentSong!!.fileSong)
            mediaPlayer!!.start()
            isPlayingServiceMedia = true
            sendNotification(currentSong)
        }

    }

    fun isPlaying(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    fun play() {
        mediaPlayer!!.start()
        isPlayingServiceMedia = true
    }

    fun pause() {
        mediaPlayer!!.pause()
        isPlayingServiceMedia = false
    }

    fun duration(): Int {
        return mediaPlayer!!.duration
    }

    fun currentPosition(): Int {
        return mediaPlayer!!.currentPosition
    }

    fun seekTo(position: Int) {
        mediaPlayer!!.seekTo(position)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun sendNotification(song: Song?) {
        val bitmap = BitmapFactory.decodeResource(resources, song!!.image)
        val intentBack = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intentBack, PendingIntent.FLAG_CANCEL_CURRENT)

        val remoteView = RemoteViews(packageName, R.layout.layout_custom_notification)
        remoteView.setTextViewText(R.id.tvTitleSong, song!!.title)
        remoteView.setTextViewText(R.id.tvSingerName, song!!.singer)
        remoteView.setImageViewBitmap(R.id.imgSong, bitmap)

        remoteView.setOnClickPendingIntent(
            R.id.btnClearNotification,
            getPendingIntent(this, Constants.ACTION_CLEAR)
        )
        if (mediaPlayer!!.isPlaying) {
            remoteView.setOnClickPendingIntent(
                R.id.btnPausePlayNotification, getPendingIntent(
                    this,
                    Constants.ACTION_PAUSE
                )
            )
            remoteView.setImageViewResource(R.id.btnPausePlayNotification, R.drawable.pause_icon)
        } else {
            remoteView.setOnClickPendingIntent(
                R.id.btnPausePlayNotification, getPendingIntent(
                    this,
                    Constants.ACTION_RESUME
                )
            )
            remoteView.setImageViewResource(R.id.btnPausePlayNotification, R.drawable.play_icon)
        }

        remoteView.setOnClickPendingIntent(
            R.id.btnNextNotification, getPendingIntent(
                this,
                Constants.ACTION_NEXT
            )
        )
        remoteView.setOnClickPendingIntent(
            R.id.btnPrevNotification,
            getPendingIntent(this, Constants.ACTION_PREV)
        )

        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.music_player)
            .setContentIntent(pendingIntent)
            .setCustomContentView(remoteView)
            .setSound(null)
            .build()
        startForeground(1, notification)
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent {
        val intent = Intent(this, MyReceiver::class.java)
        intent.putExtra("action_music", action)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    private fun handleActionMusic(action: Int) {
        when (action) {
            Constants.ACTION_PAUSE -> {
                isPlayingServiceMedia = false
                mediaPlayer!!.pause()
                sendNotification(currentSong)
            }
            Constants.ACTION_RESUME -> {
                isPlayingServiceMedia = true
                mediaPlayer!!.start()
                sendNotification(currentSong)
            }
            Constants.ACTION_CLEAR -> {
                Log.d("Serviceeee", "end service")
                if (mediaPlayer != null) {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.release()
                    mediaPlayer = null
                    stopSelf()
                }
            }
            Constants.ACTION_NEXT -> {
                sendNotification(currentSong)
            }
            Constants.ACTION_PREV -> {
                sendNotification(currentSong)
            }
        }
    }
}