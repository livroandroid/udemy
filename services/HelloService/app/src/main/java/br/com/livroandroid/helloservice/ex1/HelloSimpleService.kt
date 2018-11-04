package br.com.livroandroid.helloservice.ex1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * Created by Ricardo Lecheta on 15/03/2015.
 */
class HelloSimpleService : Service() {
    companion object {
        private const val TAG = "hello_service"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand() $startId")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG,"onDestroy()")
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
