package com.selorria.companion

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import java.util.Calendar

class MainActivity : ComponentActivity() {
    private lateinit var messageInput: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var saveButton: Button
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageInput = findViewById(R.id.messageInput)
        timePicker = findViewById(R.id.timePicker)
        saveButton = findViewById(R.id.saveButton)
        statusText = findViewById(R.id.statusText)

        // The button becomes visually active as soon as a real reminder
        // message has been entered.
        saveButton.isEnabled = false
        messageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSaveButtonState()
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        saveButton.setOnClickListener {
            scheduleReminder()
        }

        updateSaveButtonState()
    }

    private fun updateSaveButtonState() {
        val hasMessage = messageInput.text.toString().trim().isNotEmpty()
        saveButton.isEnabled = hasMessage

        statusText.text = if (hasMessage) {
            "Your companion is ready. Choose a time and set the reminder."
        } else {
            "Enter a reminder message to continue."
        }
    }

    private fun scheduleReminder() {
        val message = messageInput.text.toString().trim()
        if (message.isEmpty()) {
            messageInput.error = "Enter a reminder message"
            updateSaveButtonState()
            return
        }

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePicker.hour)
            set(Calendar.MINUTE, timePicker.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If today's selected time has already passed, schedule tomorrow.
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("message", message)
        }

        val pending = PendingIntent.getBroadcast(
            this,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= 31 && !alarm.canScheduleExactAlarms()) {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            statusText.text = "Please allow exact alarms, then tap Set Reminder again."
            return
        }

        // Android's AlarmManager is the background/OS-managed alarm engine.
        // It can wake the app even after its process has been killed.
        if (Build.VERSION.SDK_INT >= 23) {
            alarm.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                pending
            )
        } else {
            alarm.setExact(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                pending
            )
        }

        statusText.text = "✓ Reminder set for %02d:%02d".format(
            timePicker.hour,
            timePicker.minute
        )

        // Give the user clear visual confirmation.
        saveButton.text = "✓ Reminder Set"
        saveButton.isEnabled = false
    }
}
