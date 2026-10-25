package com.example.ilk

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WeatherAnimation(
    weatherCode: Int,
    isDay: Boolean = true,
    modifier: Modifier = Modifier.size(140.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "weather")

    // Yavaş dönen güneş ışınları
    val sunRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sunRot"
    )

    // Bulut sürükleme
    val cloudDrift by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloudDrift"
    )

    // Yağmur damlası ilerlemesi
    val rainProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rain"
    )

    // Kar tanesi dönüşü ve ilerlemesi
    val snowProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "snow"
    )

    // Şimşek flaş
    val lightningAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2800
                0f at 0
                1f at 100
                0.2f at 200
                1f at 350
                0f at 500
                0f at 2800
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "lightning"
    )

    // Güneş nabzı (büyüyüp küçülme)
    val sunPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sunPulse"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        when (weatherCode) {

            // ── Güneşli (0) ─────────────────────────────────────────────
            0 -> {
                drawSun(cx, cy, w, sunRotation, sunPulse)
            }

            // ── Az Bulutlu (1) ──────────────────────────────────────────
            1 -> {
                // Küçük güneş sol üstte
                val sc = Offset(cx - w * 0.10f, cy - h * 0.22f)
                drawCircle(Color(0xFFFFD54F).copy(alpha = 0.35f), w * 0.18f, sc)
                drawCircle(Color(0xFFFFB300), w * 0.13f, sc)
                // Bulut önde
                drawCloud(cx + w * 0.06f, cy + h * 0.08f + cloudDrift, w, Color.White)
            }

            // ── Parçalı Bulutlu (2) ─────────────────────────────────────
            2 -> {
                // Güneş yarı arkada
                val sc = Offset(cx + w * 0.16f, cy - h * 0.16f)
                drawCircle(Color(0xFFFFD54F).copy(alpha = 0.25f), w * 0.20f, sc)
                drawCircle(Color(0xFFFFB300).copy(alpha = 0.7f), w * 0.14f, sc)
                // İki bulut
                drawCloud(cx - w * 0.08f, cy + h * 0.04f + cloudDrift, w, Color.White.copy(alpha = 0.90f))
                drawCloud(cx + w * 0.12f, cy + h * 0.12f - cloudDrift * 0.5f, w * 0.75f, Color.White.copy(alpha = 0.70f))
            }

            // ── Kapalı / Çok Bulutlu (3) ────────────────────────────────
            3 -> {
                drawCloud(cx - w * 0.08f, cy - h * 0.06f + cloudDrift * 0.5f, w, Color(0xFFB0BEC5))
                drawCloud(cx + w * 0.10f, cy + h * 0.10f - cloudDrift * 0.3f, w * 0.85f, Color(0xFF90A4AE))
            }

            // ── Yağmurlu (51-67, 80-82) ─────────────────────────────────
            in 51..67, in 80..82 -> {
                drawCloud(cx, cy - h * 0.14f, w, Color(0xFF607D8B))
                drawRain(cx, cy, w, h, rainProgress)
            }

            // ── Karlı (71-77, 85-86) ─────────────────────────────────────
            in 71..77, in 85..86 -> {
                drawCloud(cx, cy - h * 0.14f, w, Color(0xFFB0BEC5))
                drawSnow(cx, cy, w, h, snowProgress)
            }

            // ── Sisli (45, 48) ───────────────────────────────────────────
            45, 48 -> {
                // Yatay sis şeritleri
                for (i in 0..3) {
                    val yOffset = cy - h * 0.18f + i * h * 0.14f + cloudDrift * (if (i % 2 == 0) 1f else -1f)
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f - i * 0.04f),
                        start = Offset(w * 0.05f, yOffset),
                        end = Offset(w * 0.95f, yOffset),
                        strokeWidth = 10.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                drawCloud(cx, cy - h * 0.02f + cloudDrift, w * 0.9f, Color.White.copy(alpha = 0.55f))
            }

            // ── Fırtına / Şimşek (95-99) ─────────────────────────────────
            in 95..99 -> {
                drawCloud(cx, cy - h * 0.16f, w, Color(0xFF455A64))
                drawLightning(cx, cy, w, h, lightningAlpha)
                // Sağanak yağmur da var
                drawRain(cx, cy, w, h, rainProgress, dropColor = Color(0xFF90CAF9).copy(alpha = 0.5f))
            }

            // ── Varsayılan (bulutlu) ─────────────────────────────────────
            else -> {
                drawCloud(cx, cy + cloudDrift, w * 0.9f, Color(0xFFCFD8DC))
            }
        }
    }
}

// ── Güneş çizimi: çok katmanlı halo + dönen yumuşak ışınlar ────────────────
private fun DrawScope.drawSun(cx: Float, cy: Float, w: Float, rotation: Float, pulse: Float) {
    val r = w * 0.22f * pulse
    // Dış halo (en soluk)
    drawCircle(Color(0xFFFFD54F).copy(alpha = 0.08f), r * 2.2f, Offset(cx, cy))
    drawCircle(Color(0xFFFFD54F).copy(alpha = 0.15f), r * 1.7f, Offset(cx, cy))
    drawCircle(Color(0xFFFFD54F).copy(alpha = 0.25f), r * 1.35f, Offset(cx, cy))

    // Dönen ışınlar
    rotate(rotation, pivot = Offset(cx, cy)) {
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val isMajor = i % 3 == 0
            val startR = r * 1.25f
            val endR = r * (if (isMajor) 1.75f else 1.55f)
            val thickness = if (isMajor) 3.5.dp.toPx() else 2.dp.toPx()
            val alpha = if (isMajor) 0.85f else 0.55f

            drawLine(
                color = Color(0xFFFFEE58).copy(alpha = alpha),
                start = Offset(cx + startR * cos(angle).toFloat(), cy + startR * sin(angle).toFloat()),
                end = Offset(cx + endR * cos(angle).toFloat(), cy + endR * sin(angle).toFloat()),
                strokeWidth = thickness,
                cap = StrokeCap.Round
            )
        }
    }

    // Güneş gövdesi — iki renkli gradient simülasyonu (iç parlak, dış sıcak)
    drawCircle(Color(0xFFFFB300), r, Offset(cx, cy))
    drawCircle(Color(0xFFFFF176).copy(alpha = 0.6f), r * 0.55f, Offset(cx, cy))
}

// ── Gerçekçi bulut çizimi: üst üste daireler ───────────────────────────────
private fun DrawScope.drawCloud(
    cx: Float,
    cy: Float,
    w: Float,
    color: Color
) {
    val s = w * 0.38f  // ölçek

    // Gölge/derinlik efekti
    drawCircle(color.copy(alpha = 0.3f), s * 0.78f, Offset(cx - s * 0.22f, cy + s * 0.18f))
    drawCircle(color.copy(alpha = 0.3f), s * 0.90f, Offset(cx + s * 0.10f, cy + s * 0.18f))

    // Ana bulut katmanları
    drawCircle(color, s * 0.52f, Offset(cx - s * 0.46f, cy + s * 0.10f)) // sol küçük
    drawCircle(color, s * 0.68f, Offset(cx - s * 0.14f, cy - s * 0.08f)) // sol orta büyük
    drawCircle(color, s * 0.75f, Offset(cx + s * 0.24f, cy - s * 0.04f)) // sağ büyük
    drawCircle(color, s * 0.52f, Offset(cx + s * 0.56f, cy + s * 0.10f)) // sağ küçük
    // Alt düzlük
    drawRect(
        color = color,
        topLeft = Offset(cx - s * 0.46f, cy + s * 0.10f),
        size = androidx.compose.ui.geometry.Size(s * 1.04f, s * 0.45f)
    )
}

// ── Açılı yağmur damlaları ──────────────────────────────────────────────────
private fun DrawScope.drawRain(
    cx: Float, cy: Float, w: Float, h: Float,
    progress: Float,
    dropColor: Color = Color(0xFF64B5F6)
) {
    val columns = 7
    val dropLen = 11.dp.toPx()
    val angle = Math.toRadians(20.0) // hafif eğimli yağmur

    for (i in 0 until columns) {
        val baseX = w * (0.18f + i * 0.11f)
        val phaseOffset = i * 0.14f
        val rawY = cy + ((progress + phaseOffset) % 1f) * (h * 0.55f)
        val y = if (rawY < cy + h * 0.02f) rawY + h * 0.55f else rawY

        val startX = baseX
        val endX = baseX - (dropLen * sin(angle)).toFloat()
        val endY = y + (dropLen * cos(angle)).toFloat()

        drawLine(
            color = dropColor,
            start = Offset(startX, y),
            end = Offset(endX, endY),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

// ── Altı kollu dönen kar kristalleri ────────────────────────────────────────
private fun DrawScope.drawSnow(
    cx: Float, cy: Float, w: Float, h: Float, progress: Float
) {
    val flakes = listOf(
        Triple(w * 0.28f, 0.0f, 0.85f),
        Triple(w * 0.42f, 0.2f, 1.0f),
        Triple(w * 0.56f, 0.4f, 0.75f),
        Triple(w * 0.35f, 0.6f, 0.9f),
        Triple(w * 0.65f, 0.8f, 0.8f)
    )

    for ((baseX, phase, scale) in flakes) {
        val rawY = cy + ((progress + phase) % 1f) * (h * 0.52f)
        val y = if (rawY < cy + h * 0.02f) rawY + h * 0.52f else rawY
        val r = 5.dp.toPx() * scale
        val spinAngle = (progress * 360f + phase * 360f) % 360f

        // Altı kollu kar kristali
        rotate(spinAngle, pivot = Offset(baseX, y)) {
            for (arm in 0 until 6) {
                val a = Math.toRadians((arm * 60).toDouble())
                drawLine(
                    color = Color.White.copy(alpha = 0.88f),
                    start = Offset(baseX, y),
                    end = Offset(baseX + (r * cos(a)).toFloat(), y + (r * sin(a)).toFloat()),
                    strokeWidth = 1.8.dp.toPx(),
                    cap = StrokeCap.Round
                )
                // Kol üzerindeki çapraz çizgiler
                val midX = baseX + (r * 0.55f * cos(a)).toFloat()
                val midY = y + (r * 0.55f * sin(a)).toFloat()
                val perpA1 = a + Math.PI / 3
                val perpA2 = a - Math.PI / 3
                val br = r * 0.28f
                drawLine(
                    color = Color.White.copy(alpha = 0.6f),
                    start = Offset(midX + (br * cos(perpA1)).toFloat(), midY + (br * sin(perpA1)).toFloat()),
                    end = Offset(midX - (br * cos(perpA1)).toFloat(), midY - (br * sin(perpA1)).toFloat()),
                    strokeWidth = 1.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        // Merkez nokta
        drawCircle(Color.White, 2.dp.toPx() * scale.toFloat(), Offset(baseX, y))
    }
}

// ── Şimşek: flaş + parlayan çizgi ───────────────────────────────────────────
private fun DrawScope.drawLightning(
    cx: Float, cy: Float, w: Float, h: Float, alpha: Float
) {
    // Arka parıltı (flaş efekti)
    if (alpha > 0.5f) {
        drawCircle(Color(0xFFFFF59D).copy(alpha = alpha * 0.15f), w * 0.4f, Offset(cx, cy + h * 0.15f))
    }

    // Şimşek gövdesi — kalın dış çizgi (halo)
    val boltPath = Path().apply {
        moveTo(cx + w * 0.06f, cy - h * 0.02f)
        lineTo(cx - w * 0.06f, cy + h * 0.15f)
        lineTo(cx + w * 0.03f, cy + h * 0.15f)
        lineTo(cx - w * 0.08f, cy + h * 0.36f)
    }

    // Şimşek halo (beyaz/sarı)
    drawPath(boltPath, Color(0xFFFFF9C4).copy(alpha = alpha * 0.5f))

    // Asıl şimşek çizgisi
    drawLine(Color(0xFFFDD835).copy(alpha = 0.9f + alpha * 0.1f),
        start = Offset(cx + w * 0.06f, cy - h * 0.02f),
        end = Offset(cx - w * 0.06f, cy + h * 0.15f),
        strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
    drawLine(Color(0xFFFDD835).copy(alpha = 0.9f + alpha * 0.1f),
        start = Offset(cx - w * 0.06f, cy + h * 0.15f),
        end = Offset(cx + w * 0.03f, cy + h * 0.15f),
        strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
    drawLine(Color(0xFFFDD835).copy(alpha = 0.9f + alpha * 0.1f),
        start = Offset(cx + w * 0.03f, cy + h * 0.15f),
        end = Offset(cx - w * 0.08f, cy + h * 0.36f),
        strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)

    // Parlak iç çizgi (beyaz)
    drawLine(Color.White.copy(alpha = alpha * 0.8f),
        start = Offset(cx + w * 0.06f, cy - h * 0.02f),
        end = Offset(cx - w * 0.06f, cy + h * 0.15f),
        strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
    drawLine(Color.White.copy(alpha = alpha * 0.8f),
        start = Offset(cx - w * 0.06f, cy + h * 0.15f),
        end = Offset(cx + w * 0.03f, cy + h * 0.15f),
        strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
    drawLine(Color.White.copy(alpha = alpha * 0.8f),
        start = Offset(cx + w * 0.03f, cy + h * 0.15f),
        end = Offset(cx - w * 0.08f, cy + h * 0.36f),
        strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
}

// Open-Meteo hava durum kodlarını Türkçe metne dönüştürür
fun parseWeatherCodeToText(code: Int, isDay: Boolean = true): String {
    return when (code) {
        0 -> "Açık / Güneşli"
        1 -> "Az Bulutlu"
        2, 3 -> "Parçalı Bulutlu"
        45, 48 -> "Sisli"
        51, 53, 55 -> "Hafif Çisenti"
        56, 57 -> "Dondurucu Çisenti"
        61, 63, 65 -> "Yağmurlu"
        66, 67 -> "Dondurucu Yağmur"
        71, 73, 75, 77 -> "Kar Yağışlı"
        80, 81, 82 -> "Sağanak Yağışlı"
        85, 86 -> "Yoğun Kar Yağışlı"
        95 -> "Gök Gürültülü Fırtına"
        96, 99 -> "Dolu ile Fırtına"
        else -> "Parçalı Bulutlu"
    }
}
