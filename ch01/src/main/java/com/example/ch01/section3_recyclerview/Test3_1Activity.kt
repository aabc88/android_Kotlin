package com.example.ch01.section3_recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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

//        data.add("aaaaaaa,asdsad asd dsa ads das ds ads ads d a ds dsda d sd  da")
//        data.add("aaaaaaa,asdsad asd dsa ads")
//        data.add("aaaaaaa,asdsad asd dsa adssaddgwrgw f sf fsd fg g g g g g  g wrg ")
//        data.add("aaaaaaa,asdsawrg ")
        //val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //binding.main.layoutManager = layoutManager

        binding.main.layoutManager = LinearLayoutManager(this)
        binding.main.addItemDecoration(MyDecoration(this))

    }
}

class MyViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val data: MutableList<String>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.binding.rcTvItem.text = data[position]
    }

    override fun getItemCount() = data.size

}

class MyDecoration(val context: Context) : RecyclerView.ItemDecoration() {
    //항목이 찍히기 전 최초 한번 바탕 드로잉이 필요할 때
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        c.drawBitmap(
            BitmapFactory.decodeResource(context.getResources(), R.drawable.stadium),
            0f,
            0f,
            null
        )
    }

    //항목이 모두 찍힌 . 최후에 한번 항목 위에 추가 드로잉이 필요한 경우
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        //이미지를 rc의 center에
        val width = parent.width
        val height = parent.height

        //이미지 사이즈
        val dr = ResourcesCompat.getDrawable(context.resources, R.drawable.kbo, null)
        val drWidth = dr?.intrinsicWidth
        val drHeight = dr?.intrinsicHeight

        val left = width / 2 - drWidth?.div(2) as Int
        val top = height / 2 - drHeight?.div(2) as Int

        //이미지 그리기
        c.drawBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.kbo),
            left.toFloat(), top.toFloat(), null
        )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //매개변수로 몇번째 항목을 꾸미기 위해 호출된건지, 획득
        //1을 더하는건 알고리즘 편의성
        val index = parent.getChildAdapterPosition(view) + 1

        if (index % 3 == 0) outRect.set(10, 10, 10, 60)
        else outRect.set(10, 10, 10, 0)

        view.setBackgroundColor(Color.LTGRAY)
        ViewCompat.setElevation(view, 20.0f)
    }
}