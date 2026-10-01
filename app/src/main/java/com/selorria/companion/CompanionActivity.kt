package com.selorria.companion

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.sceneview.SceneView
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader
import kotlinx.coroutines.delay
import java.util.Locale

class CompanionActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val message = intent.getStringExtra("message").orEmpty()

        tts = TextToSpeech(this) { result ->
            if (result == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "selorria-reminder")
            }
        }

        setContent { CompanionScreen(message) }
    }

    @Composable
    private fun CompanionScreen(message: String) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val cameraManipulator = rememberCameraManipulator()
        val model = rememberModelInstance(
            modelLoader,
            "models/companion_placeholder.glb"
        )

        // Phase 2.2 expression controller.
        // These names are the contract for the final rigged character GLB.
        var animation by remember { mutableStateOf("Idle") }
        var expression by remember { mutableStateOf("Neutral") }
        var autoBlink by remember { mutableStateOf(true) }

        // Automatic blink scheduler. On the current placeholder this is a
        // no-op visually if the GLB has no Blink clip; on the production
        // character it becomes real eye blinking without changing the app.
        LaunchedEffect(autoBlink) {
            if (!autoBlink) return@LaunchedEffect
            while (true) {
                delay(4500)
                animation = "Blink"
                delay(650)
                animation = when (expression) {
                    "Happy" -> "Happy"
                    "Thinking" -> "Thinking"
                    else -> "Idle"
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFBF5FF)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                SceneView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.62f)
                        .align(Alignment.TopCenter),
                    engine = engine,
                    modelLoader = modelLoader,
                    cameraManipulator = cameraManipulator
                ) {
                    model?.let { instance ->
                        io.github.sceneview.node.ModelNode(
                            modelInstance = instance,
                            scaleToUnits = 1.6f,
                            autoAnimate = true,
                            animationName = animation,
                            animationLoop = animation != "Blink",
                            animationSpeed = if (animation == "Blink") 1.4f else 1f
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.94f),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        "●  Companion is alive",
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp),
                        color = Color(0xFF514854),
                        fontSize = 13.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        "Phase 2.2 • Expressions",
                        fontSize = 13.sp,
                        color = Color(0xFF8B7C8F)
                    )

                    Text(
                        "Good morning ✨",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        message,
                        fontSize = 17.sp,
                        color = Color(0xFF514854),
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Expression", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ExpressionChip("Neutral", expression == "Neutral") {
                            expression = "Neutral"
                            animation = "Idle"
                        }
                        ExpressionChip("Happy", expression == "Happy") {
                            expression = "Happy"
                            animation = "Happy"
                        }
                        ExpressionChip("Thinking", expression == "Thinking") {
                            expression = "Thinking"
                            animation = "Thinking"
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Automatic blinking")
                        FilterChip(
                            selected = autoBlink,
                            onClick = { autoBlink = !autoBlink },
                            label = { Text(if (autoBlink) "ON" else "OFF") }
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            tts?.speak(
                                message,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                "selorria-reminder-repeat"
                            )
                            animation = if (expression == "Happy") "Happy" else "Talk"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔊 Say it again")
                    }

                    Spacer(Modifier.height(6.dp))

                    OutlinedButton(
                        onClick = { finish() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }

    @Composable
    private fun ExpressionChip(
        label: String,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            label = { Text(label) }
        )
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
