package com.selorria.companion

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.Locale

class ReminderActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private var message = "It's time!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        message = intent.getStringExtra("message") ?: "It's time!"
        findViewById<TextView>(R.id.messageText).text = message

        tts = TextToSpeech(this, this)

        findViewById<Button>(R.id.dismissButton).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.snoozeButton).setOnClickListener {
            finish()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
            tts.speak(
                message,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "selorria-reminder"
            )
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
