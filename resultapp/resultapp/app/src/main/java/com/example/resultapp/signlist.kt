package com.example.resultapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resultapp.databinding.ActivitySignListBinding



/**
 * 리사이클러뷰를 이용한 수어 리스트 출력
 */
class signlist : AppCompatActivity() {

    private lateinit var binding: ActivitySignListBinding
    var toggle:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_sign_list)

        val signList = arrayListOf(
            Signlanguages(R.drawable.giyeok, "ㄱ", "지수어", "ㄱ입니다."),
            Signlanguages(R.drawable.nieon, "ㄴ", "지수어", "ㄴ입니다."),
            Signlanguages(R.drawable.digeut, "ㄷ", "지수어", "ㄷ입니다."),
            Signlanguages(R.drawable.rieul, "ㄹ", "지수어", "ㄹ입니다."),
            Signlanguages(R.drawable.mium, "ㅁ", "지수어", "ㅁ입니다."),
            Signlanguages(R.drawable.biumb, "ㅂ", "지수어", "ㅂ입니다."),
            Signlanguages(R.drawable.siot, "ㅅ", "지수어", "ㅅ입니다."),
            Signlanguages(R.drawable.ieng, "ㅇ", "지수어", "ㅇ입니다."),
            Signlanguages(R.drawable.jieut, "ㅈ", "지수어", "ㅈ입니다."),
            Signlanguages(R.drawable.cieut, "ㅊ", "지수어", "ㅊ입니다."),
            Signlanguages(R.drawable.kieuk, "ㅋ", "지수어", "ㅋ입니다."),
            Signlanguages(R.drawable.tieuk, "ㅌ", "지수어", "ㅌ입니다."),
            Signlanguages(R.drawable.piub, "ㅍ", "지수어", "ㅍ입니다."),
            Signlanguages(R.drawable.hieug, "ㅎ", "지수어", "ㅎ입니다."),
            Signlanguages(R.drawable.a, "ㅏ", "지수어", "ㅏ입니다."),
            Signlanguages(R.drawable.ya, "ㅑ", "지수어", "ㅑ입니다."),
            Signlanguages(R.drawable.eo, "ㅓ", "지수어", "ㅓ입니다."),
            Signlanguages(R.drawable.yeo, "ㅕ", "지수어", "ㅕ입니다."),
            Signlanguages(R.drawable.o, "ㅗ", "지수어", "ㅗ입니다."),
            Signlanguages(R.drawable.yo, "ㅛ", "지수어", "ㅛ입니다."),
            Signlanguages(R.drawable.u, "ㅜ", "지수어", "ㅜ입니다."),
            Signlanguages(R.drawable.yu, "ㅠ", "지수어", "ㅠ입니다."),
            Signlanguages(R.drawable.eu, "ㅡ", "지수어", "ㅡ입니다."),
            Signlanguages(R.drawable.i, "ㅣ", "지수어", "ㅣ입니다."),
            Signlanguages(R.drawable.ae, "ㅐ", "지수어", "ㅐ입니다."),
            Signlanguages(R.drawable.aye, "ㅒ", "지수어", "ㅒ입니다."),
            Signlanguages(R.drawable.ee, "ㅔ", "지수어", "ㅔ입니다."),
            Signlanguages(R.drawable.ye, "ㅖ", "지수어", "ㅖ입니다."),
        )

        binding.rvSign.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSign.setHasFixedSize(true)

        binding.rvSign.adapter = SignAdapter(signList)
    }

}