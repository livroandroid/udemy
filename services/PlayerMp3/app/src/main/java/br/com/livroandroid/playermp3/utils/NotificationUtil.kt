package br.com.livroandroid.playermp3.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder

import br.com.livroandroid.playermp3.R

/**
 * Classe utilitária para criar notificações.
 *
 * http://developer.android.com/guide/topics/ui/notifiers/notifications.html
 */
object NotificationUtil {

    const val ACTION_VISUALIZAR = "br.com.livroandroid.hellonotification.ACTION_VISUALIZAR"

    const val CHANNEL_ID = "1"

    // Registra o canal (channel)
    fun createChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val appName = context.getString(R.string.app_name)
            val c = NotificationChannel(CHANNEL_ID, appName, NotificationManager.IMPORTANCE_DEFAULT)
            c.lightColor = Color.BLUE
            c.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            manager.createNotificationChannel(c)
        }
    }

    // Cria uma notificação
    fun create(context: Context, id: Int, intent: Intent, title: String, msg: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Intent para disparar o broadcast
        val p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Cria a notificação
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(p)
            .setContentTitle(title)
            .setContentText(msg)
            .setSmallIcon(R.drawable.img_start)
            .setAutoCancel(true)
        // Dispara a notificação
        val n = builder.build()
        manager.notify(id, n)
    }

    fun cancell(context: Context, id: Int) {
        val nm = NotificationManagerCompat.from(context)
        nm.cancel(id)
    }
}
