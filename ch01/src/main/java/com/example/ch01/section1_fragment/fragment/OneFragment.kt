package com.example.ch01.section1_fragment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ch01.databinding.FragmentOneBinding

class OneFragment : Fragment() {
    lateinit var binding: FragmentOneBinding

    //자동 호출되는 라이프사이클 함수
    //Fragment의 화면 구성을 목적으로 호출
    //이 함수에서 리턴시키는 뷰 객체가 Fragment화면이다.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBinding.inflate(inflater, container, false)
        return binding.root
    }
}