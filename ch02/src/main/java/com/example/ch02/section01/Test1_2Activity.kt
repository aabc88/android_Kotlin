package com.example.ch02.section01

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.lifecycleScope
import com.example.ch02.R
import com.example.ch02.databinding.ActivityTest12Binding
import kotlinx.coroutines.launch

class Test1_2Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest12Binding

    //코드 필요 시 아래와 같이 직접 생성
    /*val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        {preferencesDataStoreFile("my_prefs")})*/

    //위처럼 객체를 생성할 수 있지만 by 위임으로 생성하는 것이 일반적.
    //by에 의해 생성하면 singleton을 보장한다.
    //클래스 멤버변수로 선언되어야 내부적으로 setter/getter함수가 만들어진다.
    val dataStore: DataStore<Preferences> by preferencesDataStore("my_prefs")
    val USER_NAME = stringPreferencesKey("user_name") //이 키에의해 저장되는 데이터 타입이 string
    val AGE = intPreferencesKey("age")

    //데이터를 저장하기 위한 개발자 함수 이함수에서 datastore를 이용해서 데이터 저장할것
    //이 함수는 coroutine에 의해 실행 되어야 함. suspend로 선언
    private suspend fun save() {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = "kim"
            preferences[AGE] = 10
        }
    }

    private suspend fun get() {
        //collect : coroutine에서 데이터를 획득한 후, flow로 데이터를 발행
        //flow 데이터를 수신하기 위해서.. collect
        dataStore.data.collect {
            val userName = it[USER_NAME] ?: ""
            val age = it[AGE] ?: 0
            Log.d("ddddd", "$userName, $age")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest12Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSave.setOnClickListener {
            //코루틴 구동시켜 코루틴에 의해 save()함수 호출되게
            //코루틴은 스코프가 있어야하고 그 스코프 내에서만 동작
            lifecycleScope.launch {
                //lifecyclerscope 내에서 하나의 코루틴이 구동
                save()

            }
        }

        binding.btnGet.setOnClickListener {
            lifecycleScope.launch {
                get()
            }
        }
    }
}