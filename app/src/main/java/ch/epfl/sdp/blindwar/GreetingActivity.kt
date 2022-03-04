package ch.epfl.sdp.blindwar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.game.GameTutorial

class GreetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        val game = GameTutorial(assets)
        game.nextRound()

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.textViewGreeting).apply {
            text = message
        }
    }
}