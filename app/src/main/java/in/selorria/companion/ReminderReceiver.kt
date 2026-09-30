package com.selorria.companion

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reminder = Intent(context, ReminderActivity::class.java).apply {
            putExtra(
                "message",
                intent.getStringExtra("message") ?: "It's time!"
            )
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        }

        context.startActivity(reminder)
    }
}
