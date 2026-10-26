# Hava Durumu (Weather App)

Jetpack Compose, Clean Architecture (3-Katmanlı Mimari) ve Material Design 3 prensiplerine uygun olarak geliştirilmiş; Türkiye'nin 81 ilinin anlık, saatlik ve 7 günlük hava durumu verilerini sunan modern bir Android uygulaması.

<table align="center">
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e6129721-a584-409b-bb3d-ecff202dcdf4" width="180" alt="Onboarding" /><br />
      <sub><b>Onboarding</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/3b10afb8-47bf-4647-9305-35a2b7a5d9eb" width="180" alt="Şehir detay" /><br />
      <sub><b>Şehir Detay Sayfası</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e2bdd59d-6393-44c1-95a5-77d734855519" width="180" alt="Açık Tema Modu" /><br />
      <sub><b>Açık Tema Modu</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/d5088109-5157-4ed6-b19c-8e4924d633a7" width="180" alt="Uygulama Ayarları" /><br />
      <sub><b>Uygulama Ayarları</b></sub>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/9f3fc813-fe6f-483d-af99-18dfdc4894f2" width="180" alt="Ana Ekran Widget'ı" /><br />
      <sub><b>Ana Ekran Widget'ı</b></sub>
    </td>
  </tr>
</table>

---

## Özellikler

- **Türkiye'nin 81 İli**: Tüm iller koordinatlarıyla önceden tanımlanmıştır; anlık arama ve filtreleme desteklenir.
- **Anlık, Saatlik ve 7 Günlük Tahmin**: Open-Meteo API entegrasyonu ile rüzgar hızı, nem oranı, saatlik sıcaklıklar ve 7 günlük tahmin verileri sunulur.
- **Glassmorphism & Dinamik Gradientler**: Hava durumuna göre değişkenlik gösteren renk paletleri ve yarı saydam cam tasarımı.
- **Açık & Koyu Tema Desteği**: Kullanıcı tercihine göre kolayca geçiş yapılabilen sistem ve uygulama temaları.
- **Ana Ekran Widget'ı (Jetpack Glance)**: Varsayılan şehrin anlık durumunu ve emojisini gösteren, dokunulduğunda uygulamayı başlatan widget.
- **3-Katmanlı Mimarisi (Clean Architecture)**: `UI` (Compose + ViewModel + StateFlow) -> `Data` (Repository + Retrofit) -> `Model` mimarisi.
- **Onboarding Akışı**: İlk kurulumda kullanıcıyı karşılayan 3 adımlı bilgilendirme ve varsayılan şehir seçim ekranı.
- **Dinamik Ayarlar Menüsü**: Varsayılan şehri arama yaparak güncelleyebilme ve tema yönetimi.
- **Ekran Döndürme Uyumlu**: `rememberSaveable` ve State preservation ile cihaz döndürme işlemlerinde durum kaybı yaşanmaz.

---

## Proje Mimarisi

Uygulama, ölçeklenebilir ve sürdürülebilir **Clean Architecture (3-Katmanlı Mimari)** prensiplerine göre yapılandırılmıştır:

```text
com.example.ilk
├── MainActivity.kt            # Uygulama Giriş Noktası & Tema Yönetimi
├── WeatherWidget.kt           # Jetpack Glance Ana Ekran Widget'ı
│
├── model/                     # Katman 1: Veri Modelleri
│   ├── City.kt                # Şehir ve koordinat veri sınıfları
│   ├── Forecast.kt            # Anlık, saatlik ve günlük hava durumu veri modelleri
│   └── WeatherCondition.kt    # WMO kodlarından Türkçe metin ve emoji dönüşümleri
│
├── data/                      # Katman 2: Veri Kaynakları & Repositories
│   ├── ApiClient.kt           # Singleton Retrofit istemcisi
│   ├── ForecastApi.kt         # Open-Meteo REST API arayüzü
│   ├── CityRepository.kt      # 81 İl koordinat verisi ve şehir arama
│   └── WeatherRepository.kt   # Hava durumu veri çekme ve yedekleme deposu
│
└── ui/                        # Katman 3: Kullanıcı Arayüzü & ViewModel
    ├── LoadState.kt           # Mühürlü Sınıf (Loading, Success, Error, Empty)
    ├── city/                  # CityScreen, CityViewModel, CityCard
    ├── forecast/              # ForecastScreen, ForecastViewModel
    ├── settings/              # SettingsScreen
    ├── onboarding/            # OnboardingScreen, OnboardingManager
    ├── component/             # Glassmorphism, WeatherAnimation
    └── theme/                 # Color, Theme, Type
```

---

## Kullanılan Teknolojiler

| Bileşen | Teknoloji / Kütüphane |
| :--- | :--- |
| **Programlama Dili** | Kotlin |
| **Kullanıcı Arayüzü (UI)** | Jetpack Compose, Material Design 3, Canvas |
| **Mimari & Durum Yönetimi** | ViewModel, StateFlow, `rememberSaveable` |
| **Ağ İstemcisi** | Retrofit 2, Gson Converter, OkHttp |
| **Ana Ekran Widget** | Jetpack Glance (`androidx.glance:glance-appwidget`) |
| **Eşzamanlılık** | Kotlin Coroutines (`Dispatchers.IO`), Flow |
| **Veri Saklama** | SharedPreferences |
| **Derleme Araçları** | Gradle (Kotlin DSL) |

---

## API Bilgisi

Uygulama, [Open-Meteo Weather API](https://open-meteo.com/) servisini kullanmaktadır:

- **Endpoint**: `GET https://api.open-meteo.com/v1/forecast`
- **Parametreler**:
  - `latitude`, `longitude`
  - `current_weather=true`
  - `hourly=temperature_2m,relative_humidity_2m`
  - `daily=temperature_2m_max,temperature_2m_min`
  - `timezone=auto`

---

## Kurulum ve Çalıştırma

1. Repozitörü klonlayın:
   ```bash
   git clone <repository-url>
   ```
2. Projeyi Android Studio (Ladybug veya güncel sürüm) ile açıp `Gradle Sync` yapın.
3. Çalıştırma konfigürasyonunu seçerek uygulamayı başlatın (`Shift + F10`).

---

## Lisans

Bu proje MIT Lisansı altında sunulmuştur.
