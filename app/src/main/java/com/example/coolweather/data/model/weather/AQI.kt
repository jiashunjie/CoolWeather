package com.example.coolweather.data.model.weather

class AQI {
    lateinit var city: AQICity

    inner class AQICity {
        var aqi = ""
        var pm25 = ""
    }
}