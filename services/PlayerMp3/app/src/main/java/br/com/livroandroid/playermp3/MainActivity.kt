package br.com.livroandroid.playermp3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import br.com.livroandroid.playermp3.utils.NotificationUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "player_mp3"
    }

    private var playerMp3: PlayerMp3Interface? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // (*3*)
            // Recupera a interface para interagir com o servico
            val connection = service as Mp3Service.Mp3ServiceBinder
            playerMp3 = connection.getService()
            Log.d(TAG, "onServiceConnected, interfaceMp3 conectada: " + playerMp3!!)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // (*6*)
            Log.d(TAG, "onServiceDisconnected, liberando recursos.")
            playerMp3 = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        NotificationUtil.createChannel(this)

        val intent = Intent(this, Mp3Service::class.java)
        Log.d(TAG, "Iniciando o service")
        // (*1*)
        startService(intent)

        // Faz o bind
        // (*2*)
        val b = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "Service conectado: $b")

        // Solicita as permissões
//        PermissionUtils.validate(this, 0,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                alertAndFinish()
                return
            }
        }
        // Se chegou aqui está OK :-)
    }

    private fun alertAndFinish() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
                .setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.")
            // Add the buttons
            builder.setPositiveButton("OK") { _, _ -> finish() }
            val dialog = builder.create()
            dialog.show()

        }
    }

    fun onClickPlay(view: View) {
        // (*4*)
        val mp3 = tArquivo.text.toString()
        if (mp3.isNotEmpty()) {
            Log.d(TAG, "play: $mp3")
            playerMp3?.play(mp3)

            // Cria o MediaPlayer
//            val player = MediaPlayer()
//            player.setDataSource(mp3)
//            Log.d(TAG,"Play $mp3")
//            player.prepare()
//            player.start()
        }
    }

    fun onClickPause(view: View) {
        // (*4*)
        Log.d(TAG, "pause")
        playerMp3?.pause()
    }

    fun onClickStop(view: View) {
        // (*4*)
        Log.d(TAG, "stop")
        playerMp3?.stop()
        NotificationUtil.cancell(this, 1)
    }

    override fun onStop() {
        super.onStop()
        if (playerMp3 != null && playerMp3!!.isPlaying()) {
            // (*5*)
            Log.d(TAG, "Activity destruida. A musica continua.")
            unbindService(serviceConnection)
            // Cria a notificacao para o usuario voltar ao player.
            val mp3 = playerMp3?.getMp3()
            if (mp3 != null) {
                val intent = Intent(this, MainActivity::class.java)
                NotificationUtil.create(this, 1,intent, getString(R.string.app_name), mp3)
            }
        } else {
            // (*7*)
            Log.d(TAG, "Activity destruida. Para o servico, pois nao existe musica tocando.")
            unbindService(serviceConnection)
            stopService(Intent(this, Mp3Service::class.java))
        }
    }
}
