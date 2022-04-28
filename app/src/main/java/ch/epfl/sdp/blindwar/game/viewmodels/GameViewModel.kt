package ch.epfl.sdp.blindwar.game.viewmodels

import android.content.Context
import android.content.res.Resources
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.database.UserDatabase
import com.google.firebase.auth.FirebaseAuth
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.game.util.GameHelper
import ch.epfl.sdp.blindwar.audio.MusicViewModel
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.model.config.GameMode
import ch.epfl.sdp.blindwar.game.model.config.GameParameter
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel

/**
 * Class representing an instance of a game
 *
 * @param gameInstance object that defines the parameters / configuration of a game
 * @param context of the Game
 * @constructor Construct a class that represent the game logic
 */
class GameViewModel(
    gameInstance: GameInstance,
    private val context: Context,
    private val resources: Resources
): ViewModel() {
    /** Encapsulates the characteristics of a game instead of its logic
     *
     */
    private val game: GameInstance = gameInstance
    private lateinit var musicViewModel: MusicViewModel
    private val profileViewModel = ProfileViewModel()

    private val gameParameter: GameParameter = gameInstance
        .gameConfig
        .parameter
    
    private val mode: GameMode = gameInstance
        .gameConfig
        .mode

    /** Player game score **/
    var score = 0
        private set

    var round = 0
        private set

    /** Survival mode specific **/
    val lives = MutableLiveData(gameParameter.lives)

    /**
     * Prepares the game following the configuration
     *
     */
    fun init() {
        this.musicViewModel = MusicViewModel(
            game.onlinePlaylist,
            context, resources
        )
    }

    /**
     * Record the game instance to the player history
     * clean up player and assets
     *
     */
    private fun endGame() {
        val fails = round - score

        profileViewModel.updateStats(score, fails)
        musicViewModel.soundTeardown()
    }

    /**
     * Pass to the next round
     *
     * @return true if the game is over after this round, false otherwise
     */
    fun nextRound(): Boolean {
        if (mode == GameMode.SURVIVAL && lives.value!! <= 0) {
            endGame()
            return true
        }

        if (round >= gameParameter.round) {
            endGame()
            return true
        }

        musicViewModel.nextRound()
        musicViewModel.normalMode()
        return false
    }

    /**
     * Depends on the game instance parameter
     *
     * @return
     */
    fun currentMetadata(): MusicMetadata? {
        if (gameParameter.hint) {
            return musicViewModel.getCurrentMetadata()
        }

        return null
    }

    /**
     * Try to guess a music by its title
     *
     * @param titleGuess Title that the user guesses
     * @return True if the guess is correct
     */
    fun guess(titleGuess: String, isVocal: Boolean): Boolean {
        return if (
            GameHelper.isTheCorrectTitle(titleGuess, currentMetadata()!!.title, isVocal)
        ) {
            score += 1
            round += 1
            musicViewModel.summaryMode()
            true
        } else
            false
    }

    /**
     * Current round has timed out
     *
     */
    fun timeout() {
        round += 1
        lives.value = lives.value?.minus(1)
    }

    /**
     * Play the current music if in pause
     *
     */
    fun play() {
        musicViewModel.play()
    }

    /**
     * Pause the current music if playing
     *
     */
    fun pause() {
        musicViewModel.pause()
    }
}