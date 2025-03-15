package uz.yayra.otabek.equilibrium

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.yayra.otabek.equilibrium.ui.theme.EquilibriumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EquilibriumTheme {
                AnimatedCircularProgressBar()
            }
        }
    }
}

@Composable
fun AnimatedCircularProgressBar() {
    val isRunning = remember { mutableStateOf(true) }
    val timeText = remember { mutableLongStateOf(15000L) }
    val progress = remember { mutableFloatStateOf(1f) }

    LaunchedEffect(isRunning.value) {
        while (isRunning.value && timeText.longValue > 0) {
            delay(1000)
            timeText.longValue -= 1000
            progress.floatValue = timeText.longValue / 15000f
        }
        if (timeText.longValue == 0L) isRunning.value = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = {
            if (!isRunning.value) {
                timeText.longValue = 15000L
                progress.floatValue = 1f
                isRunning.value = true
            }
        }, modifier = Modifier.align(Alignment.TopCenter)) {
            Image(
                painter = painterResource(id = R.drawable.play), contentDescription = null
            )
        }
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
        ) {
            val stroke = Stroke(width = 24f, cap = StrokeCap.Round)
            drawArc(
                color = Color(0xFFEAEEF5), startAngle = -90f, sweepAngle = 360f, useCenter = false, style = stroke
            )
            drawArc(
                color = Color(0xFF25AFD2), startAngle = -90f, sweepAngle = progress.floatValue * 360f, useCenter = false, style = stroke
            )
        }
        Text(
            text = timeText.longValue.toTimeFormat(),
            modifier = Modifier.align(Alignment.Center),
            color = if (timeText.longValue > 5000L) Color.Black else Color.Red,
            fontSize = 36.sp, fontWeight = FontWeight.Medium
        )
    }
}


@SuppressLint("DefaultLocale")
fun Long.toTimeFormat(): String {
    val minutes = this / 60000
    val seconds = (this % 60000) / 1000
    return String.format("%02d:%02d", minutes, seconds)
}