package com.example.resultapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.resultapp.VideoInterface.Companion.IPADDRESS
import com.example.resultapp.databinding.ActivityMainBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

public val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
public val CAMERA_PERMISSION_FLAG = 100
public val STORAGE_PERMISSION = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
public val STORAGE_PERMISSION_FLAG = 200

public val REQUEST_VIDEO_CAPTURE_CODE = 0
public var videoUri: Uri?=null // video 저장될 Uri
public var isVideoPlaying = false // videoView에 영상이 재생되고 있는지 상태를 확인인
lateinit var videoFile: File
var value = ""


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var toggle: Boolean = true
    var value = ""
    var message = ""
    var finalResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.list.setOnClickListener {
            val intent = Intent(this, signlist::class.java)
            startActivity(intent)
        }
        binding.dictionary.setOnClickListener {
            var intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://sldict.korean.go.kr/"))
            startActivity(intent2)
        }
        binding.support.setOnClickListener {
            val intent3 = Intent(this, support::class.java)
            startActivity(intent3)
        }
        binding.translate.setOnClickListener {
            val recordVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            videoFile = File(
                File("${cacheDir}/Pictures").apply{
                    if(!this.exists()){
                        this.mkdirs()
                    }
                },
                newVideoFileName()
            )
            videoUri = FileProvider.getUriForFile(
                this,
                "com.example.resultapp.fileprovider",
                videoFile
            )
            recordVideoIntent.resolveActivity(packageManager)?.also{
                recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,videoUri)
                startActivityForResult(recordVideoIntent,REQUEST_VIDEO_CAPTURE_CODE)
            }



        }


        if(checkPermission(CAMERA_PERMISSION,CAMERA_PERMISSION_FLAG)){
            checkPermission(STORAGE_PERMISSION,STORAGE_PERMISSION_FLAG)
        }
    }

    protected fun newVideoFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "test.avi"
    }

    private fun checkPermission(permissions:Array<out String>, flag:Int):Boolean{
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            for(permission in permissions){
                if(ContextCompat.checkSelfPermission(
                        this,
                        permission
                    )!= PackageManager.PERMISSION_GRANTED
                ){
                    ActivityCompat.requestPermissions(this,permissions, flag)
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_FLAG->{
                for(grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"카메라 권한을 승인해야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else{
                        checkPermission(STORAGE_PERMISSION,STORAGE_PERMISSION_FLAG)
                    }
                }
            }
            STORAGE_PERMISSION_FLAG->{
                for(grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"저장소 권한을 승인해야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_VIDEO_CAPTURE_CODE -> {

                        uploadVideo1()


                        val intent5 = Intent(this, ResultActivity::class.java)
                        intent5.putExtra("VideoKey",videoFile.toString())
                        startActivity(intent5)



                    print("***********8")



                }
            }
        }
    }
    private fun uploadVideo1() {
        val retrofit = Retrofit.Builder()
            .baseUrl(IPADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val postApi: VideoInterface = retrofit.create(VideoInterface::class.java)
        val testFile = File(videoFile.toString())

        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), testFile)

        val multiPartBody: MultipartBody.Part = MultipartBody.Part
            .createFormData("model_pic", testFile.name, requestBody)

        val call: Call<RequestBody?>? = postApi.uploadVideo(multiPartBody)

        call?.enqueue(object : Callback<RequestBody?> {
            override fun onResponse(call: Call<RequestBody?>?, response: Response<RequestBody?>?) {
                Log.d("good", "good")
                println("*********")
                println(requestBody)
            }
            override fun onFailure(call: Call<RequestBody?>?, t: Throwable?) {
                Log.d("fail", "fail")
                println("*********")
                if (t != null) {
                    t.message?.let { Log.d("레트로핏 결과1", it) }
                }
            }
        })
    }

}





