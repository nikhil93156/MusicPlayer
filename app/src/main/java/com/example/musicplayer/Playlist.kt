package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityPlaylistBinding

class Playlist : AppCompatActivity(),ServiceConnection {
    companion object{
       lateinit var musiclistPA:ArrayList<AudioModel>
       var songposition:Int=0
        var isPlaying:Boolean=false
        var musicservice:Musicservice?=null
        @SuppressLint("StaticFieldLeak")
        lateinit var binding:ActivityPlaylistBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this,R.layout.activity_playlist)
        val i=Intent(this,Musicservice::class.java)
        bindService(i,this, BIND_AUTO_CREATE)
        startService(i)//for starting service
    songposition=intent.getIntExtra("index",0)
        musiclistPA= ArrayList()
        musiclistPA.addAll(MainActivity.MusicListMA)
        setTitle()
        binding.pauseplay.setOnClickListener{
            if(isPlaying) pauseMusic()
            else playMusic()
        }
        binding.previous.setOnClickListener{
            prevnextsong(increment = false)
        }
            binding.next.setOnClickListener{
                prevnextsong(increment = true)
            }
        binding.sk.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) musicservice!!.mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?)=Unit


            override fun onStopTrackingTouch(p0: SeekBar?)=Unit
        })
    }
    private fun setTitle(){
        binding.title.text= musiclistPA[songposition].title
    }
    private fun createMediapayer(){
       try{
           if(musicservice!!.mediaPlayer==null) musicservice!!.mediaPlayer= MediaPlayer()
           musicservice!!.mediaPlayer!!.reset()
           musicservice!!.mediaPlayer!!.setDataSource(musiclistPA[songposition].path)
           musicservice!!.mediaPlayer!!.prepare()
           musicservice!!.mediaPlayer!!.start()
           isPlaying=true
           binding.pauseplay.setImageResource(R.drawable.baseline_pause_circle_outline_24)
           binding.currentTime.text= formatDuration(musicservice!!.mediaPlayer!!.currentPosition.toLong())
           binding.totalTime.text= formatDuration(musicservice!!.mediaPlayer!!.duration.toLong())
           binding.sk.progress=0
           binding.sk.max= musicservice!!.mediaPlayer!!.duration
       }catch (e:Exception){
           return
       }
    }
    private fun playMusic(){
        binding.pauseplay.setImageResource(R.drawable.baseline_pause_circle_outline_24)
        isPlaying=true
        musicservice!!.mediaPlayer!!.start()
    }
    private fun pauseMusic(){
        binding.pauseplay.setImageResource(R.drawable.baseline_play_circle_outline_24)
        isPlaying=false
        musicservice!!.mediaPlayer!!.pause()
    }
    private fun prevnextsong(increment:Boolean){
        if(increment){
          setSongPosition(increment = true)
            setTitle()
            createMediapayer()
        }
        else{
           setSongPosition(increment = false)
            setTitle()
            createMediapayer()
        }
    }
    private fun setSongPosition(increment: Boolean){
        if(increment){
            if(musiclistPA.size-1== songposition)
                songposition=0
            else ++songposition
        }
        else{
            if(songposition==0)
                songposition= musiclistPA.size-1
            else --songposition
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder=service as Musicservice.MyBinder
        musicservice=binder.currentService()
        createMediapayer()
        musicservice!!.seekbarsetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicservice=null
    }
    
}