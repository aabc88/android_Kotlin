package com.example.ch01.section2_viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.ch01.R
import com.example.ch01.databinding.ActivityTest21Binding
import com.example.ch01.databinding.ViewpagerItemBinding
import com.example.ch01.section1_fragment.fragment.OneFragment
import com.example.ch01.section1_fragment.fragment.TwoFragment

class Test2_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest21Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTest21Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vp_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //일반 뷰로 항목을 구성하는 경우
        //binding.vpMain.adapter = ViewPagerAdapter()
        //Fragment로 항목을 구성하는 경우
        binding.vpMain.adapter = MyFragmentStateAdapter(this)
        binding.vpMain.orientation = ViewPager2.ORIENTATION_VERTICAL

    }
}


//일반 뷰로 항목을 구성하는 경우
//RecyclerView의 Adapter를 그대로 이용
class PagerViewHolder(val binding: ViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root) {

}

class ViewPagerAdapter : RecyclerView.Adapter<PagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(
            ViewpagerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.binding.run {
            when (position) {
                0 -> tv1.text = "첫번째"
                1 -> tv1.text = "두번째"
                2 -> tv1.text = "세번째"
            }
        }
    }
}

class MyFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments: List<Fragment> = listOf(OneFragment(), TwoFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}