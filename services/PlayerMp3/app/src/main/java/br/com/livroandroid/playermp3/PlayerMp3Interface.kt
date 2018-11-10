package br.com.livroandroid.playermp3

/**
 * Interface para fazer o bind do Service MediaPlayerService que a implementa
 *
 * @author ricardo
 */
interface PlayerMp3Interface {
    // true se esta tocando
    fun isPlaying(): Boolean

    // Caminho da musica
    fun getMp3(): String?

    // Inicia a musica
    fun play(mp3: String)

    // Faz pause da musica
    fun pause()

    // Para a musica
    fun stop()
}
