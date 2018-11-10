package br.com.livroandroid.helloservice.ex4
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.util.Log
import br.com.livroandroid.helloservice.MainActivity
import br.com.livroandroid.helloservice.NotificationUtil
import br.com.livroandroid.helloservice.R

class HelloServiceAndroid8 : IntentService("LivroAndroid") {
    private var count: Int = 0
    private var running: Boolean = false
    // Constantes
    companion object {
        private const val MAX = 1000
        private const val TAG = "hello_service"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, ">> HelloService.onStartCommand()")

        val notificationIntent = Intent(getContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(getContext(), NotificationUtil.CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("HelloService is running on background")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1, notification)

        Log.d(TAG, ">> HelloService.startForeground Android 8!")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.d(TAG, ">> HelloService.onCreate()")
        super.onCreate()
    }

    fun getContext(): Context {
        return this
    }

    override fun onHandleIntent(intent: Intent?) {
        val mainThread  = Looper.myLooper() == Looper.getMainLooper()
        Log.d(TAG, ">> HelloService mainThread: $mainThread")

        running = true
        while (running && count < MAX) {
            // Simula algum processamento
            Thread.sleep(1000)
            Log.d(TAG, "HelloService executando... $count")
            count++
        }
        Log.d(TAG, "<< HelloService.onHandleIntent()")

        val it = Intent(this, MainActivity::class.java)
        NotificationUtil.create(this, 1, it, "Fim do serviço.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "HelloService.onDestroy()")
        running = false
    }
}
