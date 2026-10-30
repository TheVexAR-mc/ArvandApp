package com.arvand.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ArvandSplashScreen()
            }
        }
    }
}

@Composable
fun ArvandSplashScreen() {
    // وضعیت‌های انیمیشن
    var startAnimation by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    var isBlackScreen by remember { mutableStateOf(false) }

    // انیمیشن بالا اومدن عکس
    val offsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 800f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "offset"
    )

    // انیمیشن ظاهر شدن متن
    val textAlpha by animateFloatAsState(
        targetValue = if (showText) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "textAlpha"
    )

    // انیمیشن چرخش دایره لودینگ
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    // تغییر رنگ پس‌زمینه (سفید به سیاه)
    val bgColor by animateColorAsState(
        targetValue = if (isBlackScreen) Color.Black else Color.White,
        animationSpec = tween(durationMillis = 1000),
        label = "bgColor"
    )

    // تایمر برای اجرای مرحله به مرحله انیمیشن‌ها
    LaunchedEffect(Unit) {
        delay(500) // 0.5 ثانیه صبر می‌کنیم صفحه سفید بمونه
        startAnimation = true // عکس میاد بالا
        delay(1200) // صبر تا عکس برسه به وسط
        showText = true // متن ظاهر میشه
        delay(800) // صبر تا متن کامل ظاهر بشه
        showLoading = true // دایره لودینگ شروع به چرخیدن می‌کنه
        delay(3000) // 3 ثانیه لودینگ نشون داده میشه
        isBlackScreen = true // صفحه سیاه میشه (پایان اسپلش)
    }

    // گرادیانت رنگ آبی برای متن
    val textGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF0D47A1), Color(0xFF42A5F5)) // آبی پررنگ و آسمانی
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        // اگر صفحه سیاه نشده بود، لوگو و متن رو نشون بده
        if (!isBlackScreen) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    translationY = offsetY // افکت بالا اومدن
                }
            ) {
                // بخش لوگو و دایره لودینگ
                Box(contentAlignment = Alignment.Center) {
                    // 1. دایره لودینگ (فقط وقتی showLoading true باشه نشون داده میشه)
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(140.dp) // سایز دایره (بزرگتر از عکس)
                                .rotate(rotationAngle), // چرخش
                            color = Color(0xFF0D47A1),
                            strokeWidth = 4.dp
                        )
                    }

                    // 2. عکس لوگو (اینجا باید عکس خودت رو بذاری)
                    // فعلاً یه دایره موقت گذاشتم که وقتی عکست رو گذاشتی، خط Image رو آنکامنت کنی
                    
                    /* 
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Arvand Logo",
                        modifier = Modifier.size(100.dp)
                    )
                    */
                    
                    // --- موقت: جایگزین عکس تو (بعداً پاکش کن) ---
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = Color(0xFFE3F2FD) // آبی خیلی روشن
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("A", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        }
                    }
                    // ---------------------------------------------
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 3. متن Arvand
                Text(
                    text = "Arvand",
                    style = TextStyle(
                        brush = textGradient, // اعمال گرادیانت آبی
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold, // فونت کلفت
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier.alpha(textAlpha) // افکت ظاهر شدن نرم
                )
            }
        }
    }
}
