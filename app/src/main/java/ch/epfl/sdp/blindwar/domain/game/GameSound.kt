package ch.epfl.sdp.blindwar.domain.game

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import ch.epfl.sdp.blindwar.data.sound.LocalSoundDataSource
import java.util.*

class GameSound(val assetManager: AssetManager) {
    private val mediaMetadataRetriever = MediaMetadataRetriever()
    private val localSoundDataSource = LocalSoundDataSource(assetManager, mediaMetadataRetriever)
    private val player = MediaPlayer()
    // Map each title with its asset file descriptor and its important metadata
    private lateinit var assetFileDescriptorAndMetaDataPerTitle: Map<String, Pair<AssetFileDescriptor, SongMetaData>>
    private lateinit var playlistNames: MutableSet<String>
    private lateinit var currentMetaData: SongMetaData

    fun soundInitWithSpotifyMetadata(playlist: List<SongMetaData>) {
        assetFileDescriptorAndMetaDataPerTitle = localSoundDataSource.fetchSoundFileDescriptors(playlist)
        playlistNames = refreshPlaylist()
        currentMetaData = SongMetaData("", "", "")
    }

    fun soundInitFromLocalStorage(path: String) {
        this.assetManager.list(path)?.filter { it.endsWith(".mp3")}?.map { assetManager.openFd(it) }
            ?.associateBy({
                // Get the title
                mediaMetadataRetriever.setDataSource(it.fileDescriptor, it.startOffset, it.length)
                return@associateBy mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()
            }, {
                val author = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    .toString()
                val title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    .toString()

                return@associateBy Pair(
                    it,
                    SongImageUrlConstants.SONG_MAP[author]!!
                )
            }) ?: emptyMap()

        currentMetaData = SongMetaData("", "", "")
    }

    private fun refreshPlaylist(): MutableSet<String> {
        return assetFileDescriptorAndMetaDataPerTitle.keys.toSet() as MutableSet<String>
    }

    fun soundTeardown() {
        pause()
        reset()
    }

    fun getCurrentMetadata(): SongMetaData {
        return currentMetaData
    }

    fun nextRound(): SongMetaData {
        // Stop the music
        pause()
        reset()

        if(playlistNames.isEmpty())
            playlistNames = refreshPlaylist()

        // Get a random title
        val random = Random()
        val title = playlistNames.elementAt(random.nextInt(playlistNames.size))
        val afd = assetFileDescriptorAndMetaDataPerTitle[title]?.first
        currentMetaData = assetFileDescriptorAndMetaDataPerTitle[title]?.second!!

        // Remove it to the playlist
        playlistNames.remove(title)

        // Get a random time
        //Log.d("test", super.timeToFind.toString())
        afd?.let { player.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        afd?.let { mediaMetadataRetriever.setDataSource(afd.fileDescriptor, afd.startOffset, it.length) }
        /**
        val time = random.nextInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()
        ?.minus(this.timeToFind) ?: 1)
         **/

        // Keep the start time low enough so that at least half the song can be heard (for now)
        val time = random.nextInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()
            ?.div(2) ?: 1)

        // Change the current music
        player.prepare()
        player.seekTo(time)

        // Play the music
        player.start()

        return currentMetaData
    }

    /** Game Sound Controls **/
    /**
     * TODO: Other sound controls : Sound alteration...
     */

    private fun reset() {
        player.reset()
    }

    fun play() {
        player.start()
    }

    fun pause() {
        player.pause()
    }
}