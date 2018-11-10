package br.com.livroandroid.helloservice

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.livroandroid.helloservice.ex1.HelloService
import br.com.livroandroid.helloservice.ex1.HelloSimpleService
import br.com.livroandroid.helloservice.ex2.HelloServiceThread
import br.com.livroandroid.helloservice.ex3.HelloIntentService
import br.com.livroandroid.helloservice.ex4.HelloServiceAndroid8
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NotificationUtil.createChannel(this)

        val cls = HelloServiceAndroid8::class.java

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

