package ch.epfl.sdp.blindwar.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.user.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ProfileActivity : AppCompatActivity() {
    private val database = UserDatabase()
    private val imageDatabase = ImageDatabase()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            val nameView = findViewById<TextView>(R.id.nameView)
            val emailView = findViewById<TextView>(R.id.emailView)
            val eloView = findViewById<TextView>(R.id.eloView)
            if (user != null) {
                nameView.text = user.firstName
                emailView.text = user.email
                eloView.text = user.userStatistics.elo.toString()
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    if (data.data != null) {
                        val profilePic = findViewById<ImageView>(R.id.profileImageView)
                        profilePic!!.setImageURI(data.data)

                        // Upload picture to database
                        val imagePath = imageDatabase.uploadImage(
                            data.data!!,
                            findViewById(android.R.id.content)
                        )

                        // Update user profilePic
                        database.addProfilePicture("JOJO", imagePath)

                    }
                }

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // user id should be set according to authentication
        if (currentUser != null) {
            database.addUserListener(currentUser.uid, userInfoListener)
        }
        setContentView(R.layout.activity_profile)
        val profilePic = findViewById<ImageView>(R.id.profileImageView)
        /*
        profilePic.setOnClickListener {
            choosePicture()
        } */

    }


    fun choosePicture(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }


    fun logoutButton(view: View) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            startActivity(Intent(this, SplashScreenActivity::class.java))
        }

    }


    fun statisticsButton(view: View) {
        startActivity(Intent(this, StatisticsActivity::class.java))
    }

}