package com.example.coolweather.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.coolweather.R
import com.example.coolweather.ui.area.ChooseAreaFragment
import com.example.coolweather.ui.weather.WeatherActivity
import com.example.coolweather.util.InjectorUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (KEY.isEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("请先在MainActivity中配置天气API的Key")
            builder.setCancelable(false)
            builder.setPositiveButton("确定") { _, _ ->
                finish()
            }
            builder.show()
        } else {
            val viewModel =
                ViewModelProviders.of(this, InjectorUtil.getMainModelFactory()).get(MainViewModel::class.java)
            if (viewModel.isWeatherCached()) {
                val intent = Intent(this, WeatherActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.container, ChooseAreaFragment()).commit()
            }
        }


    }

    companion object {
        // 备用Key，由于每个Key每天只有1000次免费请求，如果已用超的话请换别的Key使用。
        const val KEY = "9da35b0a6b2c48498ed9e81b9d5206f3"
//        const val KEY = "0099dcee07fd488e8b8866f16453fa2e"
//        const val KEY = "45dd25f63300445e967b461d2037e4f9"
    }
}
