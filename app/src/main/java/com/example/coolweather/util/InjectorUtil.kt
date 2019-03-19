package com.example.coolweather.util

import com.example.coolweather.data.PlaceRepository
import com.example.coolweather.data.WeatherRepository
import com.example.coolweather.data.db.CoolWeatherDatabase
import com.example.coolweather.data.network.CoolWeatherNetwork
import com.example.coolweather.ui.MainModelFactory
import com.example.coolweather.ui.area.ChooseAreaModelFactory
import com.example.coolweather.ui.weather.WeatherModelFactory

object InjectorUtil {

    private fun getPlaceRepository() = PlaceRepository.getInstance(
        CoolWeatherDatabase.getPlaceDao(),
        CoolWeatherNetwork.getInstance()
    )

    private fun getWeatherRepository() = WeatherRepository.getInstance(
        CoolWeatherDatabase.getWeatherDao(),
        CoolWeatherNetwork.getInstance()
    )

    fun getChooseAreaModelFactory() = ChooseAreaModelFactory(getPlaceRepository())

    fun getWeatherModelFactory() = WeatherModelFactory(getWeatherRepository())

    fun getMainModelFactory() = MainModelFactory(getWeatherRepository())

}