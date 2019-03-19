package com.example.coolweather.ui.area

import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolweather.CoolWeatherApplication
import com.example.coolweather.data.PlaceRepository
import com.example.coolweather.data.model.place.City
import com.example.coolweather.data.model.place.County
import com.example.coolweather.data.model.place.Province
import com.example.coolweather.ui.area.ChooseAreaFragment.Companion.LEVEL_CITY
import com.example.coolweather.ui.area.ChooseAreaFragment.Companion.LEVEL_COUNTY
import com.example.coolweather.ui.area.ChooseAreaFragment.Companion.LEVEL_PROVINCE
import kotlinx.coroutines.launch

class ChooseAreaViewModel(private val repository: PlaceRepository) : ViewModel() {
    var currentLevel = MutableLiveData<Int>()
    var dataChanged = MutableLiveData<Int>()
    var isLoading = MutableLiveData<Boolean>()
    var areaSelected = MutableLiveData<Boolean>()
    var selectedProvince: Province? = null
    var selectedCity: City? = null
    var selectedCounty: County? = null
    lateinit var provinces: MutableList<Province>
    lateinit var cities: MutableList<City>
    lateinit var counties: MutableList<County>
    val dataList = ArrayList<String>()

    fun getProvinces() {
        currentLevel.value = LEVEL_PROVINCE
        launch {
            provinces = repository.getProvinceList()
            dataList.addAll(provinces.map { it.provinceName })
        }
    }

    private fun getCities() = selectedProvince?.let {
        currentLevel.value = LEVEL_CITY
        launch {
            cities = repository.getCityList(it.provinceCode)
            dataList.addAll(cities.map { it.cityName })
        }
    }

    private fun getCounties() = selectedCity?.let {
        currentLevel.value = LEVEL_COUNTY
        launch {
            counties = repository.getCountyList(it.provinceId, it.cityCode)
            dataList.addAll(counties.map { it.countyName })
        }
    }

    fun onListViewItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when {
            currentLevel.value == LEVEL_PROVINCE -> {
                selectedProvince = provinces[position]
                getCities()
            }
            currentLevel.value == LEVEL_CITY -> {
                selectedCity = cities[position]
                getCounties()
            }
            currentLevel.value == LEVEL_COUNTY -> {
                selectedCounty = counties[position]
                areaSelected.value = true
            }
        }
    }

    fun onBack() {
        if (currentLevel.value == LEVEL_COUNTY) {
            getCities()
        } else if (currentLevel.value == LEVEL_CITY) {
            getProvinces()
        }
    }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            isLoading.value = true
            dataList.clear()
            block()
            dataChanged.value = dataChanged.value?.plus(1)
            isLoading.value = false
        } catch (t: Throwable) {
            t.printStackTrace()
            Toast.makeText(CoolWeatherApplication.context, t.message, Toast.LENGTH_SHORT).show()
            dataChanged.value = dataChanged.value?.plus(1)
            isLoading.value = false
        }
    }
}