package br.com.livroandroid.helloservice.ex1

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import br.com.livroandroid.helloservice.MainActivity
import br.com.livroandroid.helloservice.NotificationUtil
import org.jetbrains.anko.doAsync

/**
 * Created by Ricardo Lecheta on 15/03/2015.
 */
class HelloService : Service() {
    private var count: Int = 0
    private var running: Boolean = false

    // Constantes
    companion object {
        private const val MAX = 10
        private const val TAG = "hello_service"
    }

    override fun onBind(i: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d(TAG, "HelloService.onCreate() - Service criado")

        running = true

        doAsync {
            fazerAlgo()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "HelloService.onStartCommand() - startId: $startId")



        return super.onStartCommand(intent, flags, startId)
    }

    private fun fazerAlgo() {
        val mainThread  = Looper.myLooper() == Looper.getMainLooper()
        Log.d(TAG, ">> HelloService mainThread: $mainThread")

        try {
            while (running && count < MAX) {
                // Simula algum processamento
                Thread.sleep(1000)
                Log.d(TAG, "HelloService executando... $count")
                count++
            }
            Log.d(TAG, "HelloService fim.")
        } catch (e: InterruptedException) {
            Log.e(TAG, e.message, e)
        } finally {

            // Auto-Encerra o serviço se o contador chegou a 10
            stopSelf()

            // Cria uma notificação para avisar o usuário que terminou.
            val it = Intent(getContext(), MainActivity::class.java)
            NotificationUtil.create(getContext(), 1, it, "Fim do serviço.")
        }
    }

    private fun getContext(): Context {
        return this
    }

    override fun onDestroy() {
        // Ao encerrar o serviço, altere o flag para a thread parar (isto é importante para encerrar
        // a thread caso alguém tenha chamado o stopService(intent)
        running = false
        Log.d(TAG, "HelloService.onDestroy() - Service destruído")
    }
}
