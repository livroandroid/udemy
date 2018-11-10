package br.com.livroandroid.playermp3

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.util.Log

/**
 * Classe para encapsular o acesso ao MediaPlayer
 *
 * @author ricardo
 */
class PlayerMp3 : OnCompletionListener {

    companion object {
        private const val TAG = "player_mp3"
        private const val NOVO = 0
        private const val TOCANDO = 1
        private const val PAUSADO = 2
        private const val PARADO = 3
    }

    // Começaa o status zerado
    private var status = NOVO
    private var player: MediaPlayer? = null
    // Caminho da música
    private var mp3: String? = null
    val isPlaying: Boolean
        get() = status == TOCANDO || status == PAUSADO

    init {
        // Cria o MediaPlayer
        player = MediaPlayer()

        // Executa o listener quando terminar a música
        player?.setOnCompletionListener(this)
    }

    fun start(mp3: String) {
        this.mp3 = mp3

        try {
            when (status) {
                TOCANDO -> {
                    player!!.stop()
                    player!!.reset()
                    player!!.setDataSource(mp3)
                    Log.d(TAG,"Play $mp3")
                    player!!.prepare()
                    player!!.start()
                }
                PARADO -> {
                    player!!.reset()
                    player!!.setDataSource(mp3)
                    Log.d(TAG,"Play $mp3")
                    player!!.prepare()
                    player!!.start()
                }
                NOVO -> {
                    player!!.setDataSource(mp3)
                    Log.d(TAG,"Play $mp3")
                    player!!.prepare()
                    player!!.start()
                }
                PAUSADO -> player!!.start()
            }

            status = TOCANDO
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }

    }

    fun pause() {
        player!!.pause()
        status = PAUSADO
    }

    fun stop() {
        player!!.stop()
        status = PARADO
    }

    // Encerra o MediaPlayer e libera a memória
    fun close() {
        stop()
        player!!.release()
        player = null
        status = NOVO
    }

    /**
     * @see android.media.MediaPlayer.OnCompletionListener.onCompletion
     */
    override fun onCompletion(mp: MediaPlayer) {
        status = NOVO
        Log.d(TAG, "Fim da música: " + mp3!!)
    }
}
