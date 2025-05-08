package com.example.ch01.section3_todo

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import com.example.ch01.databinding.FragmentAddBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddFragment : Fragment() {
    lateinit var binding: FragmentAddBinding

    //fragment 화면을 출력하기 위해 자동 호출
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(LayoutInflater.from(activity), container, false)

        return binding.root
    }

    //fragment 화면을 구성한 이후 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //기본으로 현재 날짜 출력
        binding.tvTodoDate.text = SimpleDateFormat("yyyy-MM-dd").format(Date())
        //유저가 날짜를 클릭했을 때 DatePickerDialog
        binding.tvTodoDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(activity as Context, {view, year, month, dayOfMonth ->
                //유저가 날짜를 선택한 후 콜백함수
                binding.tvTodoDate.text = "${year}-${month + 1}-${dayOfMonth}"

            }, year, month, day).show()
        }

        binding.btnTodoAdd.setOnClickListener {
            if (!binding.etTodoTitle.text?.toString().equals("") && !binding.etTodoContent.text?.toString().equals("")) {
                val contentValues = ContentValues().apply {
                    put("title", binding.etTodoTitle.text.toString())
                    put("content", binding.etTodoContent.text.toString())
                    put("date", SimpleDateFormat("yyyy-MM-dd").parse(binding.tvTodoDate.text.toString()).time)
                }
                insertTodo(activity as Context, contentValues)
                activity?.supportFragmentManager?.popBackStack()
            }
        }

    }
}