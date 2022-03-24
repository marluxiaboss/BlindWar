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
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ProfileActivity : AppCompatActivity() {
    private val database = UserDatabase()
    private val imageDatabase = ImageDatabase()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val profilePic = findViewById<ImageView>(R.id.profileImageView)

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val user = dataSnapshot.getValue<User>()
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // user id should be set according to authentication
        if (currentUser != null) {
            database.addUserListener(currentUser.uid, userInfoListener)
        }
        profilePic.setOnClickListener {
            choosePicture()
        }
        setContentView(R.layout.activity_profile)
    }


    fun choosePicture() {
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    if (data.data != null) {
                        profilePic.setImageURI(data.data)

                        // Upload picture to database
                        val imagePath = imageDatabase.uploadImage(data.data!!,
                            findViewById(android.R.id.content))

                        // Update user profilePic
                        database.addProfilePicture("JOJO", imagePath)

                    }
                }

            }
        }
        val intent = Intent()
        intent.type = "images/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }


    fun logoutButton(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun backToMainButton(view: View) {
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

    fun statisticsButton(view: View) {
        startActivity(Intent(this, StatisticsActivity::class.java))
    }

}