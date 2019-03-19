package com.example.coolweather.ui

import androidx.lifecycle.ViewModel
import com.example.coolweather.data.WeatherRepository

class MainViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun isWeatherCached() = repository.isWeatherCached()
}