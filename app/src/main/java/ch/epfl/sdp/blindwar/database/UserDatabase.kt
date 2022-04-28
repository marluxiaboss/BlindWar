package ch.epfl.sdp.blindwar.database



import ch.epfl.sdp.blindwar.data.music.URIMusicMetadata
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.Mode
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


object UserDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = database.getReference("Users")

    /**
     * Get user reference to manipulate user infos
     *
     * @param uid
     * @return
     */
    private fun getUserReference(uid: String): DatabaseReference {
        return userReference.child(uid)
    }

    /**
     * Get user statistics reference to manipulate user statistics
     *
     * @param uid
     * @return
     */
    private fun getUserStatisticsReference(uid: String): DatabaseReference {
        return getUserReference(uid).child("userStatistics")
    }

    /**
     * Get elo reference to manipulate elo
     *
     * @param uid user identification
     * @return elo reference of specified user
     */
    fun getEloReference(uid: String): DatabaseReference {
        return getUserStatisticsReference(uid).child("elo")
    }

    fun getImageReference(uid: String): DatabaseReference {
        return userReference.child(uid).child("profilePicture")
    }

    /**
     * Function to add an User to the database (used when creating account)
     *
     * @param user to be added
     */
    // Add user to database
    fun addUser(user: User) {
        userReference.child(user.uid).setValue(user)
    }

    /**
     * Remove user from database
     *
     * @param uid user identification
     */
    fun removeUser(uid: String) {
        userReference.child(uid).removeValue()
    }

    /**
     * Function to add a liked music in user's list of liked music (in particular when he presses
     * the like button)
     * @param uid
     * @param music
     */
    fun addLikedMusic(uid: String, music: URIMusicMetadata) {
        val userRef = getUserReference(uid)
        userRef.get().addOnSuccessListener {
            val user: User? = it.getValue(User::class.java)
            if (user != null) {
                var duplicate = false
                for (likedMusic in user.likedMusics) {
                    if (music.title == likedMusic.title) {
                        duplicate = true
                    }
                }
                if (!duplicate) {
                    user.likedMusics.add(music)
                    userRef.setValue(user)
                }
            }
        }
    }

    /**
     * Add the gameResult to the matchHistory of the user.
     * @param uid
     * @param gameResult
     */
    fun addGameResult(uid: String, gameResult: GameResult){
        val userRef = getUserReference(uid)
        userRef.get().addOnSuccessListener {
            val user: User? = it.getValue(User::class.java)
            if (user != null) {
                // temporarily added so that old profiles don't crash
                user.matchHistory = mutableListOf()
                user.matchHistory.add(gameResult)
                userRef.setValue(user)
            }
        }
    }
    /**
     * Set elo of an user
     *
     * @param uid user identification
     */
    fun setElo(uid: String, elo: Int) {
        getEloReference(uid).setValue(elo)
    }

    fun setFirstName(uid: String, fn: String) {
        userReference.child(uid).child("firstName").setValue(fn)
    }
    fun setLastName(uid: String, ln: String) {
        userReference.child(uid).child("lastName").setValue(ln)
    }
    fun setPseudo(uid: String, pseudo: String) {
        userReference.child(uid).child("pseudo").setValue(pseudo)
    }
    fun setProfilePicture(uid: String, pp: String) {
        userReference.child(uid).child("profilePicture").setValue(pp)
    }
    fun setBirthdate(uid: String, date: Long) {
        userReference.child(uid).child("birthDate").setValue(date)
    }
    fun setGender(uid: String, gender: String) {
        userReference.child(uid).child("gender").setValue(gender)
    }
    fun setDescription(uid: String, desc: String) {
        userReference.child(uid).child("description").setValue(desc)
    }

    /**
     * Reset set user statistics
     *
     * @param uid user identification
     */
    private fun setUserStatistics(uid: String, userStatistics: AppStatistics) {
        getUserStatisticsReference(uid).setValue(userStatistics)
    }

    // Allow user to select a profile picture and store it in database
    fun addProfilePicture(uid: String, path: String) {
        getImageReference(uid).setValue(path)
    }

    fun addUserListener(uid: String, listener: ValueEventListener) {
        userReference.child(uid).addValueEventListener(listener)
    }

    /**
     * Gets UserStatistics of a user. Needs to be treated as a future(addOnSuccessListener)
     * @param uid
     * @return Task<DataSnapshot>
     */
    private fun getUserStatistics(uid: String): Task<DataSnapshot> {
        val userStatisticsRef = getUserStatisticsReference(uid)
        return userStatisticsRef.get()
    }

    /**
     * Gets the userStatistics of the user from the database and update its statistics
     * using the score of the game.
     * @param uid
     * @param score
     * @param fails
     */
    fun updateSoloUserStatistics(uid: String, score: Int, fails: Int) {
        getUserStatistics(uid).addOnSuccessListener {
            var userStatistics: AppStatistics? = it.getValue(AppStatistics::class.java)
            if (userStatistics != null) {
                userStatistics.correctnessUpdate(score, fails, Mode.SOLO)
                setUserStatistics(uid, userStatistics)
            }
        }
    }
}