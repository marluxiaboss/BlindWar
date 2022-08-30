package ch.epfl.sdp.blindwar.game.multi.partyMode.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.blindwar.game.multi.partyMode.model.PartyMatch
import ch.epfl.sdp.blindwar.game.multi.partyMode.model.PartyPlayer
import ch.epfl.sdp.blindwar.game.util.GameUtil

/**
 * ViewModel for the party mode, communicating with PartyMatch
 *
 */
class PartyInstanceViewModel: ViewModel() {
    private val basePlayer = PartyPlayer("Player", -1)
    private val basePlayers = mutableListOf<PartyPlayer>(basePlayer)
    private val basePartyMatch = PartyMatch(basePlayers)
    var partGameInstance = MutableLiveData<PartyMatch>().let {
        it.value = basePartyMatch
        it
    }

    fun setPartyGameInstance(partyMatch: PartyMatch) {
        partGameInstance.value = partyMatch
    }

}