package ch.epfl.sdp.blindwar.database

import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

object MatchDatabase {
    const val COLLECTION_PATH = "match"

    /**
     * return if the player has already created a multiplayer game
     *
     * @param userUID
     * @param userPseudo
     * @param userElo
     * @param numberOfPlayerMax
     * @param game
     * @param db
     * @param isPrivate
     * @return
     */
    fun createMatch(
        userUID: String,
        userPseudo: String,
        userElo: Int,
        numberOfPlayerMax: Int = 2,
        game: GameInstance,
        db: FirebaseFirestore,
        isPrivate: Boolean = false
    ): Match? {
        if (userUID.isNotEmpty()) {
            val match = Match(
                userUID,
                userElo,
                mutableListOf(userUID),
                mutableListOf(userPseudo),
                game,
                mutableListOf(0),
                numberOfPlayerMax,
                isPrivate
            )
            db.collection(COLLECTION_PATH).document(match.uid).set(match)
            db.collection(UserDatabase.COLLECTION_PATH).document(userUID)
                .update("matchId", match.uid)
            return match
        }
        return null
    }

    /**
     * Delete match when done
     * TODO update elo ?
     * @param uid
     */
    fun removeMatch(uid: String, db: FirebaseFirestore): Boolean {
        val matchDocument = getMatchSnapshot(uid, db)
        if (matchDocument != null) {
            val match = matchDocument.toObject(Match::class.java)
            match?.listPlayers?.forEach { it ->
                UserDatabase.removeMatchId(it)
            }
            db.collection(COLLECTION_PATH).document(uid).delete()
            return true
        }
        return false
    }

    /**
     * add a player to the match and return the document
     * TODO use exception to handle diffrent errors
     * @param match
     * @param user
     * @return
     */
    fun connect(match: Match, user: User, db: FirebaseFirestore): DocumentReference? {
        if (match.listPlayers!!.size >= match.maxPlayer) return null
        if (user.matchId.isNotEmpty()) return null
        if (match.listPlayers!!.contains(user.uid)) return null
        match.listPlayers!!.add(user.uid)
        match.listPseudo!!.add(user.pseudo)
        match.listResult!!.add(0)
        db.collection(UserDatabase.COLLECTION_PATH).document(user.uid).update("matchId", match.uid)
        db.collection(COLLECTION_PATH).document(match.uid).set(match)
        return db.collection(COLLECTION_PATH).document(match.uid)
    }

    /**
     * Get match snapshot from a uid
     *
     * @param uid
     * @param db
     * @return
     */
    fun getMatchSnapshot(uid: String, db: FirebaseFirestore): DocumentSnapshot? {
        val query = db.collection("match").whereEqualTo("uid", uid).limit(1).get()
        while (!query.isComplete); //TODO avoid active waiting
        return query.result.documents[0]
    }


}