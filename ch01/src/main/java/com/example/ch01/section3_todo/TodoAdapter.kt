package com.example.ch01.section3_todo

import android.content.ContentValues
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ch01.R
import com.example.ch01.databinding.RecyclerItemHeaderBinding
import com.example.ch01.databinding.RecyclerItemMainBinding

//하나의 rc에 두가지 타입의 항목
//항목의 데이터를 두가지 타입으로
//두 타입의 객체를 동일 타입으로 핸들링 하기 위해 상위타입
abstract class Item {
    abstract val type: Int//하위 너네가 구체적으로 타입지정해라

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_DATA = 1
    }
}

data class HeaderItem(override val type: Int = Item.TYPE_HEADER, var date: String) : Item()
data class DataItem(
    override val type: Int = TYPE_DATA,
    var id: Int,
    var title: String,
    var content: String,
    var completed: Boolean
) : Item()

class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)

class DataViewHolder(val binding: RecyclerItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class TodoAdapter(val activity: Test3_3Activity, val items: MutableList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == Item.TYPE_HEADER) {
            HeaderViewHolder(
                RecyclerItemHeaderBinding.inflate(
                    LayoutInflater.from(activity),
                    parent,
                    false
                )
            )
        } else {
            DataViewHolder(
                RecyclerItemMainBinding.inflate(
                    LayoutInflater.from(activity),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = items.get(position)
        if (item.type == Item.TYPE_HEADER) {
            val viewHolder = holder as HeaderViewHolder
            val headerItem = item as HeaderItem
            viewHolder.binding.tvItemHeader.text = headerItem.date
        } else {
            val viewHolder = holder as DataViewHolder
            val dataItem = item as DataItem

            viewHolder.binding.tvRcItemTitle.text = dataItem.title
            viewHolder.binding.tvRcContent.text = dataItem.content
            if (dataItem.completed) {
                viewHolder.binding.ivRcCompleted.setImageResource((R.drawable.icon_completed))
            } else {
                viewHolder.binding.ivRcCompleted.setImageResource((R.drawable.icon))
            }
            // 유저가 IV클릭 처리 상태 변경을 위해
            viewHolder.binding.ivRcCompleted.setOnClickListener {
                val values = if (dataItem.completed) {
                    viewHolder.binding.ivRcCompleted.setImageResource(R.drawable.icon)
                    ContentValues().apply {
                        put("completed", 0)
                    }
                } else {
                    viewHolder.binding.ivRcCompleted.setImageResource(R.drawable.icon_completed)
                    ContentValues().apply {
                        put("completed", 1)
                    }
                }
                updateTodo(activity, (values), "_id = ${dataItem.id}")
                dataItem.completed = !dataItem.completed
            }
        }
    }

    override fun getItemCount() = items.size

    //rc의 항목 타입이 여러개인 경우 아래 함수를 사용해 각 항목의 타입이 무엇인지 알려줘야함
    override fun getItemViewType(position: Int): Int {
        return items.get(position).type
    }

}