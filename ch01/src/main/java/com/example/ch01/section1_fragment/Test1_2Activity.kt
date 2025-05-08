package com.example.ch01.section1_fragment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import com.example.ch01.R
import com.example.ch01.databinding.ActivityTest12Binding
import com.example.ch01.section1_fragment.fragment.OneFragment
import com.example.ch01.section1_fragment.fragment.TwoFragment

class Test1_2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTest12Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //dafualt fragment
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val fragment = OneFragment()
        transaction.add(R.id.fc_1, fragment)
        transaction.commit()

        binding.btnFrag.setOnClickListener {
            //한번 transaction을 commit시키면 close 되어 다시 사용이 안됨. 새로 얻어서 사용
            /*val fragment = TwoFragment()
            transaction.replace(R.id.fc_1, fragment)
            transaction.commit()*/

            //아래의 코드로 TwoFragment가 나오긴 하지만
            //back button에 의해 이전 화면(OneFragment)가 나오지 않는다.
            //stack 정보가 유지되게 commit()직전에 한줄 추가
            val transaction2 = fragmentManager.beginTransaction()
            val fragment = TwoFragment()
            transaction2.replace(R.id.fc_1, fragment)
            //null대신 임의의 문자열을 지정해도 된다. 문자열이 스택정보의 식별자가 되어
            //복잡한 앱을 만드는 경우 fragment의 backStack을 여러개 만들 수 있다.
            //일반적으로 null로 지정하면 back에 의해 이전 fragment로 되돌아 가기만 하면 되는 경우
            transaction2.addToBackStack(null)
            transaction2.commit()
        }

    }
}