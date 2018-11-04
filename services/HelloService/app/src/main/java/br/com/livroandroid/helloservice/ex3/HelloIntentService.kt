package br.com.livroandroid.helloservice.ex3

import android.app.IntentService
import android.content.Intent
import android.os.Looper
import android.util.Log
import br.com.livroandroid.helloservice.MainActivity
import br.com.livroandroid.helloservice.NotificationUtil

class HelloIntentService : IntentService("LivroAndroid") {
    private var count: Int = 0
    private var running: Boolean = false
    // Constantes
    companion object {
        private const val MAX = 1000
        private const val TAG = "hello_service"
    }

    override fun onCreate() {
        Log.d(TAG, ">> HelloService.onCreate()")
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        val mainThread  = Looper.myLooper() == Looper.getMainLooper()
        Log.d(TAG, ">> HelloService mainThread: $mainThread")

        running = true
        count = 0

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
        Log.d(TAG,  "HelloService.onDestroy()")
        running = false
    }
}
