package ru.brauer.weather.domain.repository.cities

import ru.brauer.weather.domain.data.City

object CityRepository {
    val cities: List<City> = listOf(
        City("Москва",55.755819, 37.617644),
        City("Санкт-Петербург", 59.939099, 30.315877),
        City("Калининград", 54.710162, 20.510137),
        City("Краснодар", 45.035470, 38.975313),
        City("Архангельск", 64.539911, 40.515762),
        City("Новосибирск", 55.030204, 82.920430),
        City("Хабаровск", 48.480229, 135.071917),
        City("Владивосток", 43.115542, 131.885494),
    )
}