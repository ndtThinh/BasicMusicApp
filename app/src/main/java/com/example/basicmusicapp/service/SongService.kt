package com.example.basicmusicapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.PlayingSongActivity
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.models.SongMusic
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.min

class SongService : Service() {
    private val binder = MySongBinder()
    var mediaPlayer: MediaPlayer? = null
    var currentSong: SongMusic? = null
    var liveDataCurrentSong = MutableLiveData<SongMusic>()
    var liveDataMediaPlayer=MutableLiveData<MediaPlayer>()
    var mIndex: Int = 0
    var listSongCurrent = ArrayList<SongMusic>()
    var style: String = ""

    interface OnCompleteMedia {
        fun onAutoNext()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class MySongBinder : Binder() {
        fun getService(): SongService {
            return this@SongService
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        PlayingSongActivity.isBound = false
        Log.d("SongService", "Stop song service")
    }

    fun setSongCurrent(
        song: SongMusic,
        index: Int,
        listSongMusic: ArrayList<SongMusic>
    ) {
        if (currentSong == null) {
            currentSong = song
            liveDataCurrentSong.value = song
            mIndex = index
            listSongCurrent = listSongMusic
            createMediaPlayer(currentSong!!)
        } else if (currentSong != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
            currentSong = song
            liveDataCurrentSong.value = song
            createMediaPlayer(currentSong!!)
            listSongCurrent = listSongMusic
        }
        Log.d("SongCurrent", "index: $index")
    }

    private fun createMediaPlayer(song: SongMusic) {

        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        mediaPlayer!!.setAudioAttributes(audioAttributes)
        try {
            mediaPlayer!!.setDataSource(song.fileSong);
            mediaPlayer!!.prepareAsync();
            mediaPlayer!!.setOnPreparedListener {
                it.start()
                sendNotification(song)
                liveDataMediaPlayer.value=mediaPlayer
                autoNextSong()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun nextSong() {
        mIndex++
        if (mIndex == listSongCurrent.size) {
            mIndex = 0
        }
        currentSong = listSongCurrent[mIndex]
        liveDataCurrentSong.value = currentSong
        createMediaPlayer(currentSong!!)

        Log.d("SongCurrent", "index: $mIndex")
    }

    private fun prevSong() {
        mIndex--
        if (mIndex < 0) {
            mIndex = listSongCurrent.size - 1
        }
        currentSong = listSongCurrent[mIndex]
        liveDataCurrentSong.value = currentSong
        createMediaPlayer(currentSong!!)
        Log.d("SongCurrent", "index: $mIndex")
    }

    fun autoNextSong() {
        mediaPlayer!!.setOnCompletionListener {
            nextSong()
        }
    }

    fun changeUiSong(onCompleteMedia: OnCompleteMedia) {
        onCompleteMedia.onAutoNext()
    }

    fun seekTo(position: Int) {
        mediaPlayer!!.seekTo(position)
    }

    fun currentPosition(): Int {
        return mediaPlayer!!.currentPosition
    }

    fun duration(): Int {
        return if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.duration
        } else {
            0
        }
    }

    fun isPlaying(): Boolean {
        return if (mediaPlayer != null) {
            mediaPlayer!!.isPlaying
        } else {
            false
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var actionMusic = intent!!.getIntExtra("action_music_receiver", 0)
        Log.d("Action Music", actionMusic.toString())
        handleActionMusic(actionMusic)
        return START_NOT_STICKY
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

    fun sendNotification(song: SongMusic) {
        Thread {
            try {
                val url = URL(song.imageSong)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val remoteView = RemoteViews(packageName, R.layout.layout_custom_notification)
                val intentBack = Intent(this, PlayingSongActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("song", currentSong)
                bundle.putInt("style", 0)
                bundle.putInt("index", mIndex)
                intentBack.putExtra("listSong", listSongCurrent)
                intentBack.putExtra("kind", Constants.SONG_PLAYING)
                intentBack.putExtras(bundle)
                val pendingIntent =
                    PendingIntent.getActivity(
                        this,
                        0,
                        intentBack,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )

                remoteView.setTextViewText(R.id.tvTitleSongPlaying, song.nameSong)
                remoteView.setTextViewText(R.id.tvSingerNamePlaying, song.nameSinger)
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

                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun handleActionMusic(actionMusic: Int) {
        when (actionMusic) {
            Constants.ACTION_PAUSE -> {
                mediaPlayer!!.pause()
                Log.d("Action Music", "mediaPlayer: " + mediaPlayer!!.isPlaying.toString())
                sendNotification(currentSong!!)
            }
            Constants.ACTION_RESUME -> {
                mediaPlayer!!.start()
                Log.d("Action Music", "mediaPlayer: " + mediaPlayer!!.isPlaying.toString())
                sendNotification(currentSong!!)
            }
            Constants.ACTION_CLEAR -> {
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