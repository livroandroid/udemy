package br.com.livroandroid.helloservice

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NotificationUtil.createChannel(this)

        val cls = HelloService::class.java

        // Start
        btStart.setOnClickListener {
            startService(Intent(this, cls))
        }
        // Stop
        btStop.setOnClickListener {
            stopService(Intent(this, cls))
        }
    }
}

