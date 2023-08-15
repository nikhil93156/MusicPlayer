package com.example.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class Musicservice: Service() {
    private var myBinder=MyBinder()
    var mediaPlayer:MediaPlayer?=null
    private lateinit var runnable: Runnable // it helps  to run one code repatedly
    override fun onBind(p0: Intent?): IBinder? {
       return myBinder
    }
    inner class MyBinder:Binder(){//inner class help in to return the object of main class
        fun currentService():Musicservice{
            return this@Musicservice
        }
    }
    fun seekbarsetup(){
        runnable=Runnable{
            Playlist.binding.currentTime.text= formatDuration(mediaPlayer!!.currentPosition.toLong())
            Playlist.binding.sk.progress=mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }
}