# 🌤️ Modern Hava Durumu Uygulaması (Weather App)

Jetpack Compose, Clean Architecture (3-Katmanlı Mimari) ve Material Design 3 ilkelerine uygun olarak geliştirilmiş, Türkiye'nin 81 ilinin anlık, saatlik ve 7 günlük hava durumu verilerini sunan modern bir Android uygulaması.

---

## ✨ Özellikler

- **🇹🇷 Türkiye'nin 81 İli**: Tüm iller koordinatlarıyla hazır tanımlı, hızlı arama ve filtreleme olanağı.
- **🌦️ Anlık, Saatlik & 7 Günlük Tahmin**: Open-Meteo API entegrasyonu ile canlı rüzgar hızı, nem oranı, saatlik sıcaklık ve 7 günlük tahmin verileri.
- **💎 Glassmorphism & Akışkan Gradientler**: Hava durumuna (güneşli, karlı, yağmurlu, fırtınalı) göre dinamik değişen renk psikolojisi ve cam efekti kartlar.
- **🌗 Açık & Koyu Tema Desteği**: Kullanıcı tercihine veya sistem ayarına duyarlı, yüksek kontrastlı tema seçeneği.
- **📱 Ana Ekran Widget'ı (Jetpack Glance)**: Varsayılan şehrinizin canlı hava durumunu ve animasyonlu emojisini ana ekranınızda gösteren widget.
- **🚀 3-Katmanlı Mimarisi (Clean Architecture)**: `UI` (Compose + ViewModel + StateFlow) -> `Data` (Repository + Retrofit) -> `Model` ayrımı.
- **📱 Onboarding Akışı**: Uygulamayı ilk kez açan kullanıcılar için 3 adımlı karşılama ve varsayılan şehir seçim ekranı.
- **⚙️ Gelişmiş Ayarlar Menüsü**: Varsayılan şehri canlı aramayla değiştirebilme ve tema yönetimi.
- **🔄 Ekran Döndürme Desteği**: `rememberSaveable` ve state preservation ile ekran yatay/dikey döndürüldüğünde durum kaybı yaşanmaz.
- **🛡️ Kesintisiz Çalışma (Fallback) Garantisi**: Ağ gecikmelerinde veya sunucu kısıtlamalarında çökme/hata yerine otomatik akıllı yedekleme mekanizması.

---

## 🏗️ Proje Mimarisi

Uygulama, sürdürülebilir ve test edilebilir **3-Katmanlı Mimarisi (Clean Architecture)** ile geliştirilmiştir:

```text
com.example.ilk
├── MainActivity.kt            # Uygulama Giriş Noktası & Tema Yönetimi
├── WeatherWidget.kt           # Jetpack Glance Ana Ekran Widget'ı
│
├── model/                     # MİMARİ KATMAN 1: Veri Modelleri
│   ├── City.kt                # Şehir ve koordinat veri modelleri
│   ├── Forecast.kt            # Anlık, saatlik ve günlük hava durumu modelleri
│   └── WeatherCondition.kt    # WMO Hava kodlarından Türkçe metin ve emoji dönüşümleri
│
├── data/                      # MİMARİ KATMAN 2: Veri Kaynakları & Repositories
│   ├── ApiClient.kt           # Singleton Retrofit istemcisi
│   ├── ForecastApi.kt         # Open-Meteo REST API arayüzü
│   ├── CityRepository.kt      # 81 İl koordinat deposu ve şehir arama
│   └── WeatherRepository.kt   # Hava durumu veri çekme ve yedekleme (fallback) deposu
│
└── ui/                        # MİMARİ KATMAN 3: Kullanıcı Arayüzü & ViewModel
    ├── LoadState.kt           # Sealed Class (Loading, Success, Error, Empty)
    ├── city/                  # CityScreen, CityViewModel, CityCard
    ├── forecast/              # ForecastScreen, ForecastViewModel
    ├── settings/              # SettingsScreen
    ├── onboarding/            # OnboardingScreen, OnboardingManager
    ├── component/             # Glassmorphism, WeatherAnimation
    └── theme/                 # Color, Theme, Type
```

---

## 🛠️ Kullanılan Teknolojiler

| Bileşen | Teknoloji / Kütüphane |
| :--- | :--- |
| **Dil** | Kotlin (100% Idiomatic) |
| **Arayüz (UI)** | Jetpack Compose, Material 3, Canvas Graphics |
| **Arayüz Durumu (State)** | ViewModel, StateFlow, `rememberSaveable` |
| **Ağ (Networking)** | Retrofit 2, Gson Converter, OkHttp |
| **Ana Ekran Widget** | Jetpack Glance (`androidx.glance:glance-appwidget`) |
| **Asenkron Programlama** | Kotlin Coroutines (`Dispatchers.IO`), Flow |
| **Veri Saklama** | SharedPreferences (`OnboardingManager`) |
| **Derleme Sistemi** | Gradle (Kotlin DSL) |

---

## 📡 API Entegrasyonu

Uygulama, ücretsiz ve API anahtarı gerektirmeyen [Open-Meteo Weather API](https://open-meteo.com/) servisini kullanır:

- **Endpoint**: `GET https://api.open-meteo.com/v1/forecast`
- **Sorgu Parametreleri**:
  - `latitude`, `longitude`
  - `current_weather=true`
  - `hourly=temperature_2m,relative_humidity_2m`
  - `daily=temperature_2m_max,temperature_2m_min`
  - `timezone=auto`

---

## 🚀 Kurulum ve Çalıştırma

1. Projeyi klonlayın veya Android Studio ile açın:
   ```bash
   git clone <repository-url>
   ```
2. Android Studio (Ladybug veya üzeri) ile projeyi Senkronize edin (`Gradle Sync`).
3. Bir emülatör veya fiziksel Android cihaz bağlayın.
4. Uygulamayı çalıştırın (`Shift + F10` veya `app:assembleDebug`).

---

## 📜 Lisans

Bu proje MIT Lisansı ile lisanslanmıştır. Dilediğiniz gibi geliştirebilir ve kullanabilirsiniz.
