package ch.epfl.sdp.blindwar.game.multi.partyMode.model

/**
 * Class representing a player in the party mode game with a name and a score
 */
class PartyPlayer(name: String, score: Int) {
    var name: String = name
        private set
    var score: Int = score
        private set
}