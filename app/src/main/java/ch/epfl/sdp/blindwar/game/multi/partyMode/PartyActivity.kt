package ch.epfl.sdp.blindwar.game.multi.partyMode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.multi.partyMode.fragments.PartyConfigurationFragment
import ch.epfl.sdp.blindwar.game.multi.partyMode.viewmodels.PartyInstanceViewModel
import ch.epfl.sdp.blindwar.game.util.GameActivity
import ch.epfl.sdp.blindwar.profile.fragments.DisplayHistoryFragment.Companion.newInstance

// Activity to configure the party mode
class PartyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party)
        val playButton = findViewById<ImageButton>(R.id.playButton)
        val configButton = findViewById<ImageButton>(R.id.configButton)
        playButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameActivity.GAME_FORMAT_EXTRA_NAME, GameFormat.SOLO)
            startActivity(intent)
        }
        configButton.setOnClickListener {
            // launch the party configuration fragment
            showFragment(PartyConfigurationFragment())
        }
    }

    /**
     * Shows the selected fragment
     *
     * @param fragment to show
     */
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_menu_container, fragment)
            commit()
        }
    }

}