package com.example.basicmusicapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.MainActivity
import com.example.basicmusicapp.PlayingSongActivity
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.repository.DataSongs

class MusicService : Service() {
    private val binder = MyBinder()
    private var mediaPlayer: MediaPlayer? = null
    var currentSong: Song? = null
    private var mIndex: Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var actionMusic = intent!!.getIntExtra("action_music_receiver", 0)
        Log.d("Action Music", actionMusic.toString())
        handleActionMusic(actionMusic)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        Log.d("Action Music", "end service onDestroy")
    }

    fun setSong(song: Song, index: Int) {
        if (currentSong == null) {
            currentSong = song
            mIndex = index
            mediaPlayer = MediaPlayer.create(baseContext, currentSong!!.fileSong)
            mediaPlayer!!.start()
            autoNextSong()
            sendNotification(currentSong)
        } else {
            currentSong = song
            mIndex = index
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = MediaPlayer.create(baseContext, currentSong!!.fileSong)
            mediaPlayer!!.start()
            autoNextSong()
            sendNotification(currentSong)
        }

    }

    private fun autoNextSong() {
        mediaPlayer!!.setOnCompletionListener {
            nextSong()
        }
    }

    fun isPlaying(): Boolean {
        if (mediaPlayer != null) {
            return mediaPlayer!!.isPlaying
        }
        return false
//        return mediaPlayer!!.isPlaying
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

    private fun nextSong() {
        if (mIndex != null) {
            if (mIndex == DataSongs().listSongs.size - 1) {
                mIndex = 0
            } else {
                mIndex++
            }
            initMediaPlayer()
        }
    }

    private fun prevSong() {
        if (mIndex != null) {
            if (mIndex == 0) {
                mIndex = DataSongs().listSongs.size - 1
            } else {
                mIndex--
            }
            initMediaPlayer()
        }
    }

    private fun initMediaPlayer() {
        currentSong = DataSongs().listSongs[mIndex]
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
        mediaPlayer = MediaPlayer.create(baseContext, currentSong!!.fileSong)
        mediaPlayer!!.start()
        autoNextSong()
        sendNotification(currentSong)
    }


    private fun sendNotification(song: Song?) {
        val bitmap = BitmapFactory.decodeResource(resources, song!!.image)
        val intentBack = Intent(this, PlayingSongActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intentBack, PendingIntent.FLAG_CANCEL_CURRENT)

        val remoteView = RemoteViews(packageName, R.layout.layout_custom_notification)
        remoteView.setTextViewText(R.id.tvTitleSongPlaying, song!!.title)
        remoteView.setTextViewText(R.id.tvSingerNamePlaying, song!!.singer)
        remoteView.setImageViewBitmap(R.id.imgSongPlaying, bitmap)

        remoteView.setOnClickPendingIntent(
            R.id.btnClearNotificationPlaying,
            getPendingIntent(this, Constants.ACTION_CLEAR)
        )
        if (mediaPlayer!!.isPlaying) {
            remoteView.setOnClickPendingIntent(
                R.id.btnPausePlayNotificationPlaying, getPendingIntent(
                    this,
                    Constants.ACTION_PAUSE
                )
            )
            remoteView.setImageViewResource(
                R.id.btnPausePlayNotificationPlaying,
                R.drawable.pause_icon
            )
        } else if (!mediaPlayer!!.isPlaying) {
            remoteView.setOnClickPendingIntent(
                R.id.btnPausePlayNotificationPlaying, getPendingIntent(
                    this,
                    Constants.ACTION_RESUME
                )
            )
            remoteView.setImageViewResource(
                R.id.btnPausePlayNotificationPlaying,
                R.drawable.play_icon
            )
        }

        remoteView.setOnClickPendingIntent(
            R.id.btnNextNotificationPlaying, getPendingIntent(
                this,
                Constants.ACTION_NEXT
            )
        )
        remoteView.setOnClickPendingIntent(
            R.id.btnPrevNotificationPlaying,
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
                mediaPlayer!!.pause()
                Log.d("Action Music", "mediaPlayer: " + mediaPlayer!!.isPlaying.toString())
                sendNotification(currentSong)
            }
            Constants.ACTION_RESUME -> {
                mediaPlayer!!.start()
                Log.d("Action Music", "mediaPlayer: " + mediaPlayer!!.isPlaying.toString())
                sendNotification(currentSong)
            }
            Constants.ACTION_CLEAR -> {
//                val intent=Intent(this,MusicService::class.java)
//                stopService(intent)
                stopSelf()
                Log.d("Action Music", "end service")
            }
            Constants.ACTION_NEXT -> {
                nextSong()
                Log.d("Action Music", "next song")
            }
            Constants.ACTION_PREV -> {
                prevSong()
                Log.d("Action Music", "prev song")
            }
        }
    }
}