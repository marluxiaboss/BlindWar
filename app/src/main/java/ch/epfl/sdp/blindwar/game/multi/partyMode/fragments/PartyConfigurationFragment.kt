package ch.epfl.sdp.blindwar.game.multi.partyMode.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.multi.partyMode.model.PartyMatch
import ch.epfl.sdp.blindwar.game.multi.partyMode.model.PartyPlayer
import ch.epfl.sdp.blindwar.game.multi.partyMode.viewmodels.PartyInstanceViewModel

/**
 * Fragment for the configuration of the PartyMatch
 */
class PartyConfigurationFragment : Fragment() {
    private val partyInstanceViewModel: PartyInstanceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_display_music, container, false)
        val basePlayer1 = PartyPlayer("Player1", 1)
        val basePlayer4 = PartyPlayer("Player4", 4)
        val basePlayer3 = PartyPlayer("Player3", 3)
        val basePlayer2 = PartyPlayer("Player2", 2)
        val basePlayer5 = PartyPlayer("Player5", 5)
        val basePlayers = mutableListOf<PartyPlayer>(basePlayer1, basePlayer2, basePlayer3,
            basePlayer4, basePlayer5)
        val basePartyMatch = PartyMatch(basePlayers)
        partyInstanceViewModel.setPartyGameInstance(basePartyMatch)
        return view
    }
}