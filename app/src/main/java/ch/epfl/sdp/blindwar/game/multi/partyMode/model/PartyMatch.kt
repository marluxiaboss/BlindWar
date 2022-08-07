package ch.epfl.sdp.blindwar.game.multi.partyMode.model

/**
 * Class for the list of players in the match and score for each player
 */
class PartyMatch {
    var players: List<PartyPlayer> = emptyList()
    var scores: List<Int> = emptyList()
}