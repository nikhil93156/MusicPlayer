package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object{
        lateinit var MusicListMA:ArrayList<AudioModel>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPLayer)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val recyclerview=binding.rv
        recyclerview.setHasFixedSize(true)
        if(!checkPermission()){
            requestPermission()
            return
        }
        MusicListMA=getAllAudio()
        val layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerview.layoutManager=layoutManager
        recyclerview.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                layoutManager.orientation
            )
        )
        recyclerview.adapter= Adapter(this,MusicListMA)
            }

     private fun checkPermission(): Boolean {
         val result = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
         if(result ==PackageManager.PERMISSION_GRANTED){
                    return true
        }
                 else{
                    return false
            }
    }
    private fun requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,Manifest.permission.READ_EXTERNAL_STORAGE,)){
            Toast.makeText(this@MainActivity,"READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",Toast.LENGTH_LONG).show()
        }
        else{
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),123)
        }
    }
    @SuppressLint("Recycle", "Range")
    private fun getAllAudio():ArrayList<AudioModel>{
        val templist=ArrayList<AudioModel>()
        val selection=MediaStore.Audio.Media.IS_MUSIC + "!=0"//it tells that which type of file we want
        val projection= arrayOf(MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DURATION)
        val cursor=this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,MediaStore.Audio.Media.DATE_ADDED +" DESC",null)
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    val titleC=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val pathC=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val audioModel=AudioModel(path=pathC,title=titleC, duration = durationC)
                    val file=File(audioModel.path)
                    if(file.exists())
                        templist.add(audioModel)
                }while (cursor.moveToNext())
                cursor.close()
            }
        }
        return templist
    }
}