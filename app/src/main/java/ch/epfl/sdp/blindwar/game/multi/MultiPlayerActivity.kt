package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * Activity that lets the user start a multiplayer game
 *
 * @constructor creates a MultiPlayerActivity
 */
class MultiPlayerActivity : AppCompatActivity() {
    private val LIMIT_MATCH: Long = 10
    private var eloDelta = 200
    private var dialog: AlertDialog? = null
    private var isCanceled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)
        eloDelta = 200
    }

    /**
     * Starts a multiplayer game played with a friend
     *
     * @param view
     */
    fun friendButton(view: View) {
        //TODO launch link fragment
        
        setProgressDialog("Wait for connexion")
        //TODO connect if ok, toast if not existing match or full
        val intent = Intent(this, MultiPlayerFriendActivity::class.java)
        startActivity(intent)
    }

    /**
     * Starts a multiplayer game played with a random user
     *
     * @param view
     */
    fun randomButton(view: View) {
        //TODO launch lobby fragment
        setProgressDialog("Wait for matches")
        val user = UserDatabase.getCurrentUser()
        val elo = user.child("userStatistics/elo").value!! as Int
        val matchs = Firebase.firestore.collection("match").whereLessThan("elo", elo + eloDelta)
            .whereGreaterThan("elo", elo - 200)
            .orderBy("elo", Query.Direction.DESCENDING)
            .limit(LIMIT_MATCH).get()
        if (matchs.isSuccessful && !isCanceled) {
            var i = 0
            var match: DocumentReference? = null
            while (match == null && i < LIMIT_MATCH) {
                match =
                    MatchDatabase.connect(
                        matchs.result.documents[i].toObject(Match::class.java)!!,
                        user.getValue(User::class.java)!!,
                        Firebase.firestore
                    )
                i++
            }
            if (match == null && !isCanceled) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_connexion),
                    Toast.LENGTH_LONG
                )
                    .show()
                eloDelta += 100
                randomButton(view)
            } else if (!isCanceled) {
                //match.addSnapshotListener {} //TODO add listener
                dialog!!.hide()
                //TODO CONNECT TO MATCH
            }
        } else if (!isCanceled) {
            randomButton(view)
        }
    }

    /**
     * display progressdialog cancelable for any messages
     *
     * @param message
     */
    private fun setProgressDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setOnCancelListener {
            isCanceled = true
            Toast.makeText(
                applicationContext,
                getString(R.string.toast_canceled_connexion),
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setView(View.inflate(applicationContext, R.layout.fragment_dialog_loading, null))
        (findViewById<TextView>(R.id.textView_multi_loading)).text = message
        dialog = builder.create()
        dialog!!.show()
    }
}
