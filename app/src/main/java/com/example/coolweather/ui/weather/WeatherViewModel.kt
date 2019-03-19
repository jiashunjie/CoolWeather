package com.example.coolweather.ui.weather


import android.widget.Toast

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.coolweather.CoolWeatherApplication
import com.example.coolweather.data.WeatherRepository
import com.example.coolweather.data.model.weather.Weather
import com.example.coolweather.ui.MainActivity


import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    var weather = MutableLiveData<Weather>()
    var bingPicUrl = MutableLiveData<String>()
    var refreshing = MutableLiveData<Boolean>()
    var weatherInitialized = MutableLiveData<Boolean>()
    var weatherId = ""
    private var key = MainActivity.KEY
    fun getWeather() {
        launch({
            weather.value = repository.getWeather(weatherId, key)
            weatherInitialized.value = true
        }, {
            Toast.makeText(CoolWeatherApplication.context, it.message, Toast.LENGTH_SHORT).show()
        })
        getBingPic(false)
    }

    fun refreshWeather() {
        refreshing.value = true
        launch({
            weather.value = repository.refreshWeather(weatherId, key)
            refreshing.value = false
            weatherInitialized.value = true
        }, {
            Toast.makeText(CoolWeatherApplication.context, it.message, Toast.LENGTH_SHORT).show()
            refreshing.value = false
        })
        getBingPic(true)
    }

    fun isWeatherCached() = repository.isWeatherCached()
    fun getCachedWeather() = repository.getCachedWeather()
    fun onRefresh() {
        refreshWeather()
    }

    private fun getBingPic(refresh: Boolean) {
        launch({
            bingPicUrl.value = if (refresh) repository.refreshBingPic() else repository.getBingPic()
        }, {
            Toast.makeText(CoolWeatherApplication.context, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }
}