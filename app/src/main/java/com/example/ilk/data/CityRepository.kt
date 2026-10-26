package com.example.ilk.data

import com.example.ilk.model.City
import com.example.ilk.model.CityCoordinates

class CityRepository {
    private val turkishProvinces = listOf(
        CityCoordinates("Adana", "Türkiye", 37.0, 35.32),
        CityCoordinates("Adıyaman", "Türkiye", 37.76, 38.27),
        CityCoordinates("Afyonkarahisar", "Türkiye", 38.75, 30.55),
        CityCoordinates("Ağrı", "Türkiye", 39.71, 43.05),
        CityCoordinates("Aksaray", "Türkiye", 38.36, 34.03),
        CityCoordinates("Amasya", "Türkiye", 40.65, 35.83),
        CityCoordinates("Ankara", "Türkiye", 39.93, 32.85),
        CityCoordinates("Antalya", "Türkiye", 36.88, 30.70),
        CityCoordinates("Ardahan", "Türkiye", 41.11, 42.70),
        CityCoordinates("Artvin", "Türkiye", 41.18, 41.82),
        CityCoordinates("Aydın", "Türkiye", 37.84, 27.84),
        CityCoordinates("Balıkesir", "Türkiye", 39.64, 27.88),
        CityCoordinates("Bartın", "Türkiye", 41.63, 32.33),
        CityCoordinates("Batman", "Türkiye", 37.88, 41.12),
        CityCoordinates("Bayburt", "Türkiye", 40.25, 40.22),
        CityCoordinates("Bilecik", "Türkiye", 40.15, 29.98),
        CityCoordinates("Bingöl", "Türkiye", 38.88, 40.49),
        CityCoordinates("Bitlis", "Türkiye", 38.40, 42.10),
        CityCoordinates("Bolu", "Türkiye", 40.73, 31.60),
        CityCoordinates("Burdur", "Türkiye", 37.72, 30.28),
        CityCoordinates("Bursa", "Türkiye", 40.18, 29.06),
        CityCoordinates("Çanakkale", "Türkiye", 40.15, 26.41),
        CityCoordinates("Çankırı", "Türkiye", 40.60, 33.61),
        CityCoordinates("Çorum", "Türkiye", 40.55, 34.95),
        CityCoordinates("Denizli", "Türkiye", 37.77, 29.08),
        CityCoordinates("Diyarbakır", "Türkiye", 37.91, 40.23),
        CityCoordinates("Düzce", "Türkiye", 40.84, 31.15),
        CityCoordinates("Edirne", "Türkiye", 41.67, 26.55),
        CityCoordinates("Elazığ", "Türkiye", 38.67, 39.22),
        CityCoordinates("Erzincan", "Türkiye", 39.75, 39.50),
        CityCoordinates("Erzurum", "Türkiye", 39.90, 41.26),
        CityCoordinates("Eskişehir", "Türkiye", 39.77, 30.52),
        CityCoordinates("Gaziantep", "Türkiye", 37.06, 37.38),
        CityCoordinates("Giresun", "Türkiye", 40.91, 38.38),
        CityCoordinates("Gümüşhane", "Türkiye", 40.46, 39.48),
        CityCoordinates("Hakkâri", "Türkiye", 37.58, 43.73),
        CityCoordinates("Hatay", "Türkiye", 36.20, 36.16),
        CityCoordinates("Iğdır", "Türkiye", 39.91, 44.04),
        CityCoordinates("Isparta", "Türkiye", 37.76, 30.55),
        CityCoordinates("İstanbul", "Türkiye", 41.00, 28.97),
        CityCoordinates("İzmir", "Türkiye", 38.41, 27.12),
        CityCoordinates("Kahramanmaraş", "Türkiye", 37.58, 36.93),
        CityCoordinates("Karabük", "Türkiye", 41.20, 32.62),
        CityCoordinates("Karaman", "Türkiye", 37.17, 33.22),
        CityCoordinates("Kars", "Türkiye", 40.60, 43.09),
        CityCoordinates("Kastamonu", "Türkiye", 41.38, 33.78),
        CityCoordinates("Kayseri", "Türkiye", 38.73, 35.48),
        CityCoordinates("Kırıkkale", "Türkiye", 39.84, 33.51),
        CityCoordinates("Kırklareli", "Türkiye", 41.73, 27.22),
        CityCoordinates("Kırşehir", "Türkiye", 39.14, 34.15),
        CityCoordinates("Kilis", "Türkiye", 36.71, 37.11),
        CityCoordinates("Kocaeli", "Türkiye", 40.76, 29.91),
        CityCoordinates("Konya", "Türkiye", 37.86, 32.48),
        CityCoordinates("Kütahya", "Türkiye", 39.42, 29.98),
        CityCoordinates("Malatya", "Türkiye", 38.35, 38.30),
        CityCoordinates("Manisa", "Türkiye", 38.61, 27.42),
        CityCoordinates("Mardin", "Türkiye", 37.32, 40.74),
        CityCoordinates("Mersin", "Türkiye", 36.80, 34.63),
        CityCoordinates("Muğla", "Türkiye", 37.21, 28.36),
        CityCoordinates("Muş", "Türkiye", 38.74, 41.49),
        CityCoordinates("Nevşehir", "Türkiye", 38.62, 34.71),
        CityCoordinates("Niğde", "Türkiye", 37.96, 34.67),
        CityCoordinates("Ordu", "Türkiye", 40.98, 37.87),
        CityCoordinates("Osmaniye", "Türkiye", 37.07, 36.24),
        CityCoordinates("Rize", "Türkiye", 41.02, 40.52),
        CityCoordinates("Sakarya", "Türkiye", 40.77, 30.39),
        CityCoordinates("Samsun", "Türkiye", 41.28, 36.33),
        CityCoordinates("Siirt", "Türkiye", 37.93, 41.94),
        CityCoordinates("Sinop", "Türkiye", 42.02, 35.15),
        CityCoordinates("Sivas", "Türkiye", 39.75, 37.01),
        CityCoordinates("Şanlıurfa", "Türkiye", 37.15, 38.79),
        CityCoordinates("Şırnak", "Türkiye", 37.52, 42.45),
        CityCoordinates("Tekirdağ", "Türkiye", 40.97, 27.51),
        CityCoordinates("Tokat", "Türkiye", 40.31, 36.55),
        CityCoordinates("Trabzon", "Türkiye", 41.00, 39.71),
        CityCoordinates("Tunceli", "Türkiye", 39.10, 39.54),
        CityCoordinates("Uşak", "Türkiye", 38.68, 29.40),
        CityCoordinates("Van", "Türkiye", 38.50, 43.37),
        CityCoordinates("Yalova", "Türkiye", 40.65, 29.26),
        CityCoordinates("Yozgat", "Türkiye", 39.82, 34.81),
        CityCoordinates("Zonguldak", "Türkiye", 41.45, 31.79)
    )

    fun getTurkishCities(): List<City> {
        return turkishProvinces.map {
            City(
                cityName = it.name,
                latitude = it.latitude,
                longitude = it.longitude,
                temperature = "...",
                condition = "Yükleniyor...",
                isLoading = true
            )
        }
    }

    fun getCityCoordinates(cityName: String): CityCoordinates? {
        return turkishProvinces.find { it.name.equals(cityName, ignoreCase = true) }
    }
}
