package com.example.ch01.section3_recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.ch01.R
import com.example.ch01.databinding.ActivityTest32Binding
import com.example.ch01.databinding.RecyclerItemUpdateDeleteBinding

class Test3_2Activity : AppCompatActivity() {
    val binding: ActivityTest32Binding by lazy {
        ActivityTest32Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val data: MutableList<String> = mutableListOf()

        for (i in 1..100) data.add("${i * 100}")
        binding.main.adapter = MyAdapter2(data)
        binding.main.layoutManager = LinearLayoutManager(this)

    }
}

class MyViewHolder2(val binding: RecyclerItemUpdateDeleteBinding) :
    RecyclerView.ViewHolder(binding.root)

class MyAdapter2(val data: MutableList<String>) : RecyclerView.Adapter<MyViewHolder2>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder2 {
        return MyViewHolder2(
            RecyclerItemUpdateDeleteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder2,
        position: Int
    ) {
        val binding = holder.binding
        binding.rcTvItemData.text = data[position]
        /*
                binding.rcTvBtnDelete.setOnClickListener {
                    data.removeAt(position)
                    notifyItemRemoved(position)
                    //위 코드로 해당 항목이 삭제 되지만 . 아래에 있는 항목의 index가 바뀜을 알려주지 않으면
                    //아래 항목의 index위치가 변경되었음을 다음의 코드로 알려줘야함.
                    notifyItemRangeChanged(position, data.size - position)
                }
                binding.rcTvBtnUpdate.setOnClickListener {
                    var newData = data[position].toInt()
                    newData++
                    data[position] = newData.toString()
                    notifyItemChanged(position)
                }*/

        binding.rcTvBtnUpdate.setOnClickListener {
            if (position in 0 until data.size) {
                val currentValue = data[position].toInt()
                val newValue = (currentValue + 1).toString()

                //복제본
                val newList = data.toMutableList()
                newList[position] = newValue
                setData(newList)
            }
        }

        binding.rcTvBtnDelete.setOnClickListener {
            if (position in 0 until data.size) {
                val newList = data.toMutableList()
                newList.removeAt(position)
                setData(newList)
            }
        }



    }

    override fun getItemCount() = data.size

    fun setData(newNumbers: MutableList<String>) {
        val diffCallback = MyDiffCallback(data, newNumbers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        data.clear()
        data.addAll(newNumbers)
        diffResult.dispatchUpdatesTo(this)
    }
}


class MyDiffCallback(
    private val oldList: MutableList<String>,
    private val newList: MutableList<String>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldItemPosition == newItemPosition && oldItemPosition < oldList.size && newItemPosition < newList.size
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}