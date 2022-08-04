package ch.epfl.sdp.blindwar.game.multi.partyMode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.model.config.GameFormat
import ch.epfl.sdp.blindwar.game.util.GameActivity

// Activity to configure the party mode
class PartyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party)
        val playButton = findViewById<ImageButton>(R.id.playButton)
        playButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameActivity.GAME_FORMAT_EXTRA_NAME, GameFormat.SOLO)
            startActivity(intent)
        }
    }

}