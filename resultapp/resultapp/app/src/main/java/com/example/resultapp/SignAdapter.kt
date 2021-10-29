package com.example.resultapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.resultapp.R

class SignAdapter(val signList: ArrayList<Signlanguages>) : RecyclerView.Adapter<SignAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.signlist, parent, false)

        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val sign: Signlanguages = signList.get(curPos)
                Toast.makeText(parent.context, "이름 : ${sign.name}\n타입 : ${sign.type}\n설명 : ${sign.desc}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onBindViewHolder(holder: SignAdapter.CustomViewHolder, position: Int) {
        holder.sign.setImageResource(signList.get(position).sign)
        holder.name.text = signList.get(position).name
        holder.type.text = signList.get(position).type
        holder.desc.text = signList.get(position).desc
    }

    override fun getItemCount(): Int {
        return signList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // val sign: Int, val name: String, val type: String, val desc: String

        val sign = itemView.findViewById<ImageView>(R.id.iv_giyeok)  // 수어
        val name = itemView.findViewById<TextView>(R.id.tv_name)      // 이름
        val type = itemView.findViewById<TextView>(R.id.tv_type)      // 타입
        val desc = itemView.findViewById<TextView>(R.id.tv_desc)      // 설명
    }

}