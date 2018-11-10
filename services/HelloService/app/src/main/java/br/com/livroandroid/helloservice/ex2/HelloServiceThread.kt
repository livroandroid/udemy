package br.com.livroandroid.helloservice.ex2

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import br.com.livroandroid.helloservice.MainActivity
import br.com.livroandroid.helloservice.NotificationUtil

/**
 * Created by Ricardo Lecheta on 15/03/2015.
 */
class HelloServiceThread : Service() {

    private var running: Boolean = false

    // Constantes
    companion object {
        private const val MAX = 50
        private const val TAG = "hello_service"
    }

    override fun onBind(i: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d(TAG, "HelloService.onCreate() - Service criado")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "HelloService.onStartCommand() - Service iniciado: $startId")
        // Método chamado depois do onCreate(), logo depois que o serviço é iniciado
        // O parametro startId representa o identificador deste serviço
        running = true
        // Delega para uma thread
        HelloThread(startId).start()
        // Chama a implementação da superclasse
        return super.onStartCommand(intent, flags, startId)
    }

    // Thread que faz o trabalho pesado
    internal inner class HelloThread(private var startId: Int) : Thread() {
        init {
            this.startId = startId
        }

        private var count: Int = 0

        override fun run() {
            try {
                while (running && count < MAX) {
                    // Simula algum processamento
                    Thread.sleep(1000)
                    Log.d(TAG, "[$startId] HelloService executando... $count")
                    count++
                }
                Log.d(TAG, "[$startId] HelloService fim.")
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
    }

    fun getContext(): Context {
        return this
    }

    override fun onDestroy() {
        // Ao encerrar o serviço, altere o flag para a thread parar (isto é importante para encerrar
        // a thread caso alguém tenha chamado o stopService(intent)
        //running = false
        Log.d(TAG, "HelloService.onDestroy() - Service destruído")
    }
}
