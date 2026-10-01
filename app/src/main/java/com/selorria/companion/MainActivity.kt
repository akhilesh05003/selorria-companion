package com.selorria.companion

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ReminderHome() }
    }

    @Composable
    private fun ReminderHome() {
        var message by remember { mutableStateOf("") }
        var hour by remember { mutableIntStateOf(6) }
        var minute by remember { mutableIntStateOf(0) }
        var status by remember { mutableStateOf("Enter a reminder message.") }

        val enabled = message.trim().isNotEmpty()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFFF8FC)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Selorria Companion",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Phase 2 • 3D Companion",
                    color = Color(0xFF766A79),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    "Your companion is becoming alive.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Reminder message") },
                    placeholder = { Text("Good morning! Time to wake up.") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(Modifier.height(16.dp))

                TimePicker(
                    hour = hour,
                    minute = minute,
                    onTimeChanged = { h, m ->
                        hour = h
                        minute = m
                    }
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    enabled = enabled,
                    onClick = {
                        scheduleReminder(message.trim(), hour, minute)
                        status = "✓ Reminder set for %02d:%02d".format(hour, minute)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        if (enabled) "Set Reminder" else "Enter Message First",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))
                Text(status, color = Color(0xFF766A79))
            }
        }
    }

    @Composable
    private fun TimePicker(
        hour: Int,
        minute: Int,
        onTimeChanged: (Int, Int) -> Unit
    ) {
        var h by remember(hour) { mutableIntStateOf(hour) }
        var m by remember(minute) { mutableIntStateOf(minute) }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberPicker(
                value = h,
                range = 0..23,
                onValueChange = {
                    h = it
                    onTimeChanged(h, m)
                }
            )
            Text(":", fontSize = 28.sp, modifier = Modifier.padding(horizontal = 12.dp))
            NumberPicker(
                value = m,
                range = 0..59,
                onValueChange = {
                    m = it
                    onTimeChanged(h, m)
                }
            )
        }
    }

    @Composable
    private fun NumberPicker(
        value: Int,
        range: IntRange,
        onValueChange: (Int) -> Unit
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                onValueChange(if (value <= range.first) range.last else value - 1)
            }) { Text("−", fontSize = 28.sp) }

            Text(
                "%02d".format(value),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(48.dp)
            )

            IconButton(onClick = {
                onValueChange(if (value >= range.last) range.first else value + 1)
            }) { Text("+", fontSize = 28.sp) }
        }
    }

    private fun scheduleReminder(message: String, hour: Int, minute: Int) {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
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
            return
        }

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
    }
}
