package ch.epfl.sdp.blindwar.game.multi.partyMode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R

/**
 * Fragment that displays the score of each PartyPlayer at the end of the game
 *
 */
class PartyScoreFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_party_score, container, false)
    }

}