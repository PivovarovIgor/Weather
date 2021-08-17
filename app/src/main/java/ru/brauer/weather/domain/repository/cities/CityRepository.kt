package ru.brauer.weather.domain.repository.cities

import ru.brauer.weather.domain.data.City

object CityRepository {
    val cities: List<City> = listOf(
        City("Москва",55.755819, 37.617644, "https://www.freezone.net/upload/medialibrary/3c0/3c0d2d7644cc99a744a0c996fd7be737.jpg"),
        City("Санкт-Петербург", 59.939099, 30.315877, "https://rustur.ru/wp-content/uploads/2019/10/s1200-3.jpg"),
        City("Калининград", 54.710162, 20.510137, "https://ocean-media.su/wp-content/uploads/2020/08/kaliningrad.jpg"),
        City("Краснодар", 45.035470, 38.975313, "https://cdn.pixabay.com/photo/2018/07/18/19/53/krasnodar-3547171_1280.jpg"),
        City("Архангельск", 64.539911, 40.515762, "https://nesiditsa.ru/wp-content/uploads/2012/06/25.pur-na1.jpeg"),
        City("Новосибирск", 55.030204, 82.920430, "https://amsterdamtravel.ru/images/glavniedostoprimechatelnostinovosibirska_583E0114.jpg"),
        City("Челябинск", 55.159902, 61.402554, "https://www.bygeo.ru/uploads/posts/2018-05/1525837311_01.jpg"),
        City("Хабаровск", 48.480229, 135.071917, "https://gulaytour.ru/wp-content/uploads/2017/11/1a829e347c13756a1b7b67e73c91a07c.jpg"),
        City("Владивосток", 43.115542, 131.885494, "https://primamedia.gcdn.co/f/big/1894/1893355.jpg?2b7bbd85ed97162f574ced983cb049b4"),
    )
}