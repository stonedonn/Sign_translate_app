package com.example.resultapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.resultapp.*
import com.example.resultapp.databinding.ActivityResultBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
import java.util.*


class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private var mTTS: TextToSpeech? = null
    private var mEditText: EditText? = null
    private var mButtonSpeak: Button? = null
    var finalVideo = ""
    var value = "shit"
    var message = ""
    var finalResult = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("VideoKey")) {
            finalVideo = intent.getStringExtra("VideoKey").toString()

        } else {
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }
        //File(videoFile.toString())
        binding.btnPlay.setOnClickListener {
            when(isVideoPlaying){
                true->{
                    isVideoPlaying=false
                    binding.videoView.stopPlayback()
                    binding.btnPlay.text="stop"
                    binding.videoView.setVideoURI(videoUri)

                }
                false->{
                    isVideoPlaying=true
                    binding.btnPlay.text="play"
                    binding.videoView.start()
                }
            }
        }
        binding.translation.setOnClickListener {
//
//                print("value")
//                print(value)
//                finalResult = judgeString()
//                    binding.textResult.text = finalResult
            GlobalScope.launch() {
                value = getJson("http://192.168.1.35:8000/language/")
                delay(1000L)
                print("I'm working in Coroutine.")
                finalResult = judgeString()
                binding.root.post {
                    binding.textResult.setText(finalResult)
                }
            }
        }
        //initializeFields()

//        mTTS = TextToSpeech(this, OnInitListener { i ->
//            if (i == TextToSpeech.SUCCESS) {
//                val result = mTTS!!.setLanguage(Locale.KOREA)
//                if (result == TextToSpeech.LANG_MISSING_DATA
//                    || result == TextToSpeech.LANG_NOT_SUPPORTED
//                ) {
//                    Log.e("TTS", "Language Not Supported")
//                } else {
//                    mButtonSpeak!!.isEnabled = true
//                }
//            } else {
//                Log.e("TTS", "Initialization Failed")
//            }
//        })
//        mButtonSpeak!!.setOnClickListener {
//            speak()
//            closeKeyboard()
//        }
    }

    private fun speak() {
        val text = mEditText!!.text.toString()
        mTTS!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroy() {
        if (mTTS != null) {
            mTTS!!.stop()
            mTTS!!.shutdown()
        }
        super.onDestroy()
    }

    private fun initializeFields() {
        mEditText = findViewById(R.id.editText)
        mButtonSpeak = findViewById(R.id.btn_speak)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                REQUEST_VIDEO_CAPTURE_CODE->{
                    binding.videoView.setVideoURI(videoUri)
                    binding.videoView.requestFocus()

                }
            }
        }
    }

    private fun getJson(Url: String): String {

        val response = StringBuilder()
        val stringUrl: String = Url
        val url: URL
        try {
            url = URL(stringUrl)
            val httpconn: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpconn.setRequestProperty("Accept", "application/json")
            if (httpconn.getResponseCode() === HttpURLConnection.HTTP_OK) {
                val input =
                    BufferedReader(InputStreamReader(httpconn.getInputStream()), 8192)
                var strLine: String? = null
                while (input.readLine().also { strLine = it } != null) {
                    response.append(strLine)
                }
                input.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("This is the response match API $response for url$stringUrl")
        return response.toString()
    }

    private fun judgeString(): String {
        val responseJSON = JSONObject(value)
        message = responseJSON.getString("pre_ans_str")
        return message
    }
}