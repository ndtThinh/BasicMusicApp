package com.example.basicmusicapp.broadcastreceiver

import android.content.*
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.basicmusicapp.service.MusicService
import com.example.basicmusicapp.service.MyService

open class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var actionMusic = intent!!.getIntExtra("action_music", 0)
        //send action to service
        var intentService = Intent(context, MyService::class.java)
        intentService.putExtra("action_music_service", actionMusic)
        context!!.startService(intentService)
//        context!!.bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE)
        //send action to fragment
        val intentFrag = Intent("action_music")
        intentFrag.putExtra("action_music", actionMusic)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentFrag)
    }


}