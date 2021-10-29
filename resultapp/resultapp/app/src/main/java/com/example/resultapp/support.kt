package com.example.resultapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.resultapp.databinding.ActivitySupportBinding

class support : AppCompatActivity() {

    private lateinit var binding: ActivitySupportBinding
    var toggle: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.cancel.setOnClickListener {
            val intent4 = Intent(this, MainActivity::class.java)
            startActivity(intent4)
        }
        binding.send.setOnClickListener {

            val email = "bitcom1jo@gmail.com"
            val title = binding.supporttitle.text.toString()
            val message = binding.editText.text.toString()


            val addresses = email.split(",".toRegex()).toTypedArray()

            val intent5 = Intent(Intent.ACTION_SENDTO).apply {

                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,addresses)
                putExtra(Intent.EXTRA_SUBJECT,title)
                putExtra(Intent.EXTRA_TEXT,message)
            }

            if (intent.resolveActivity(packageManager) != null) {

                startActivity(intent5)
            } else {
                Toast.makeText(this@support,"Required App is not installed",Toast.LENGTH_SHORT).show()
            }
        }

    }

}