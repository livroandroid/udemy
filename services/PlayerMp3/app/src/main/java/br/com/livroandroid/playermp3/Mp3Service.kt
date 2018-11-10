package br.com.livroandroid.playermp3

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import br.com.livroandroid.playermp3.utils.NotificationUtil
import android.support.v4.app.NotificationCompat

/**
 * Service que fica tocando uma mp3 em background
 *
 * @author ricardo
 */
class Mp3Service : Service(), PlayerMp3Interface {

    companion object {
        private const val TAG = "player_mp3"
    }

    private val player = PlayerMp3()
    private var mp3: String? = null

    override fun getMp3(): String? {
        return mp3
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying
    }

    inner class Mp3ServiceBinder : Binder() {
        fun getService() : PlayerMp3Interface {
            return this@Mp3Service
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, ">> HelloService.onStartCommand()")

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
            0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, NotificationUtil.CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Player Mp3 is running")
            .setSmallIcon(R.drawable.img_start)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        Log.d(TAG, ">> HelloService.startForeground Android 8!")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        // retorna a classe ConexaoInterfaceMp3 para a activity utilizar
        Log.d(TAG, "Mp3Service onBind(). Aqui retorna o IBinder.")
        return Mp3ServiceBinder()
    }

    // InterfaceMp3.play(mp3)
    override fun play(mp3: String) {
        this.mp3 = mp3
        try {
            player.start(mp3)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }

    // InterfaceMp3.pause()
    override fun pause() {
        player.pause()
    }

    // InterfaceMp3.stop()
    override fun stop() {
        player.stop()
    }

    override fun onDestroy() {
        Log.d(TAG, "Mp3Service onDestroy().")
        player.close()
    }
}
