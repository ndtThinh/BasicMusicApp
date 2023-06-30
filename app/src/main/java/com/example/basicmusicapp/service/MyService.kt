package com.example.basicmusicapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.basicmusicapp.Constants.ACTION_CLEAR
import com.example.basicmusicapp.Constants.ACTION_NEXT
import com.example.basicmusicapp.Constants.ACTION_PAUSE
import com.example.basicmusicapp.Constants.ACTION_PREV
import com.example.basicmusicapp.Constants.ACTION_RESUME
import com.example.basicmusicapp.Constants.CHANNEL_ID
import com.example.basicmusicapp.MainActivity
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.models.Song

class MyService : Service() {
    var isPlaying = true
    var msong: Song? = null
    private var mediaPlayer: MediaPlayer? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("Thinh", "My service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent!!.extras
        if (bundle != null) {
            val song: Song? = intent?.getSerializableExtra("song_object") as? Song
            if (song != null) {
                msong = song
                sendNotification(song)
            }
        }

        var actionMusic = intent.getIntExtra("action_music_service", 0)
        handleActionMusic(actionMusic)
        return START_NOT_STICKY
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
        remoteView.setImageViewResource(R.id.btnPausePlayNotification, R.drawable.pause_icon)
        remoteView.setOnClickPendingIntent(
            R.id.btnClearNotification,
            getPendingIntent(this, ACTION_CLEAR)
        )
        if (isPlaying) {
            remoteView.setOnClickPendingIntent(
                R.id.btnPausePlayNotification, getPendingIntent(
                    this,
                    ACTION_PAUSE
                )
            )
            remoteView.setImageViewResource(R.id.btnPausePlayNotification, R.drawable.pause_icon)
        } else {
            remoteView.setOnClickPendingIntent(
                R.id.btnPausePlayNotification, getPendingIntent(
                    this,
                    ACTION_RESUME
                )
            )
            remoteView.setImageViewResource(R.id.btnPausePlayNotification, R.drawable.play_icon)
        }
        remoteView.setOnClickPendingIntent(
            R.id.btnNextNotification, getPendingIntent(
                this,
                ACTION_NEXT
            )
        )
        remoteView.setOnClickPendingIntent(
            R.id.btnPrevNotification,
            getPendingIntent(this, ACTION_PREV)
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
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
            ACTION_PAUSE -> {
                isPlaying = false
                sendNotification(msong)
            }
            ACTION_RESUME -> {
                isPlaying = true
                sendNotification(msong)
            }
            ACTION_CLEAR -> {
                Log.d("Serviceeee", "end service")
            }
            ACTION_NEXT -> {
                sendNotification(msong)
            }
            ACTION_PREV -> {
                sendNotification(msong)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Thinh", "My service destroyed")
    }
}