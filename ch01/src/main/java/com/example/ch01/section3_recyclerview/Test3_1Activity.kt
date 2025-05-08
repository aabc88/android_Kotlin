package com.example.ch01.section3_recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ch01.R
import com.example.ch01.databinding.ActivityTest11Binding
import com.example.ch01.databinding.ActivityTest31Binding
import com.example.ch01.databinding.RecyclerItemBinding

class Test3_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest31Binding
    var data = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTest31Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        for (i in 1..100) data.add(i.toString())
        binding.main.adapter = MyAdapter(data)
        //세로방향 나열
        //binding.main.layoutManager = LinearLayoutManager(this)
        //가로방향 나열
        //binding.main.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //grid
        //binding.main.layoutManager = GridLayoutManager(this,2,
          //  GridLayoutManager.VERTICAL, false)
        
        data.add("aaaaaaa,asdsad asd dsa ads das ds ads ads d a ds dsda d sd  da")
        data.add("aaaaaaa,asdsad asd dsa ads")
        data.add("aaaaaaa,asdsad asd dsa adssaddgwrgw f sf fsd fg g g g g g  g wrg ")
        data.add("aaaaaaa,asdsawrg ")

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.main.layoutManager = layoutManager
    }
}

class MyViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val data: MutableList<String>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.binding.rcTvItem.text = data[position]
    }

    override fun getItemCount() = data.size

}