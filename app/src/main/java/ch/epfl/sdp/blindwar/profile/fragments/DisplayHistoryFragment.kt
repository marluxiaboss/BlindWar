package ch.epfl.sdp.blindwar.profile.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.data.music.metadata.URIMusicMetadata
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.model.GameResult
import ch.epfl.sdp.blindwar.profile.model.User
import ch.epfl.sdp.blindwar.profile.util.LeaderboardRecyclerAdapter
import ch.epfl.sdp.blindwar.profile.util.MusicDisplayRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DisplayHistoryFragment : Fragment() {

    private lateinit var musicRecyclerView: RecyclerView
    private var titles = mutableListOf<String>()
    private var artists = mutableListOf<String>()
    private var images = mutableListOf<String>()
    private val LIKED_MUSIC_TYPE = "liked musics"
    private val MATCH_HISTORY_TYPE = "match history"
    private val LEADERBOARD_TYPE = "leaderboard"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_display_music, container, false)

        postToList()
        musicRecyclerView = view.findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)


        // showing the back button in action bar
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return view
    }

    /**
     * Add element of MusicMetadata to appropriate list.
     * @param title
     * @param artist
     * @param image
     */
    private fun addToList(title: String, artist: String, image: String) {
        titles.add(title)
        artists.add(artist)
        images.add(image)
    }

    /**
     * Fetch likedMusics from user and call addToList(with the listener) to add the element
     * to the lists that will be displayed.
     * Creates a different display based on the argument of the Fragment.
     */
    private fun postToList() {
        val historyType = arguments?.getString(HISTORY_TYPE)
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            if (historyType == LIKED_MUSIC_TYPE) {
                UserDatabase.addUserListener(currentUser.uid, userLikedMusicsListener)
            }

            if (historyType == MATCH_HISTORY_TYPE) {
                loadLeaderboard()
                UserDatabase.addUserListener(currentUser.uid, userMatchHistoryListener)
            }

            if (historyType == LEADERBOARD_TYPE) {
                loadLeaderboard()
            }
        }

        /*
        else {
            for (i in 1..20) {
                addToList("HELLO", "JOJO", R.mipmap.ic_launcher_round_base)
            }
        }
         */
    }

    private val userLikedMusicsListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            if (user != null) {
                val likedMusics: MutableList<URIMusicMetadata> = user.likedMusics
                for (music in likedMusics) {
                    addToList(music.title, music.artist, music.imageUrl)
                }
            } else {
                for (i in 1..10) {
                    addToList("HELLO", "JOJO", "no image")
                }
            }
            musicRecyclerView.adapter = MusicDisplayRecyclerAdapter(titles, artists, images)
        }


        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    private val userMatchHistoryListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            if (user != null) {
                val matchHistory: MutableList<GameResult> = user.matchHistory
                for (match in matchHistory) {
                    addToList(
                        "Number of rounds: " + match.gameNbrRound.toString(),
                        "Score: " + match.gameScore.toString(),
                        "no image"
                    )
                }
            } else {
                for (i in 1..10) {
                    addToList("HELLO", "JOJO", "no image")
                }
            }
            musicRecyclerView.adapter = MusicDisplayRecyclerAdapter(titles, artists, images)
        }


        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    /**
     * Retrieves all Users from the database an get their pseudo and elo
     */
    private fun loadLeaderboard() {
        UserDatabase.getLeaderboardData().addOnSuccessListener {
            val usersDatabase: Map<String, Any> = it.value as Map<String, Any>
            val users = usersDatabase.values
            val pseudos = mutableListOf<String>()
            val elos = mutableListOf<String>()
            for(user in users) {
                val userMap: Map<String, String> = user as Map<String, String>
                val userStatMap: Map<String, Map<String, Long>> = user as Map<String, Map<String, Long>>
                userMap["pseudo"]?.let { it1 -> pseudos.add(it1) }
                userStatMap["userStatistics"]?.get("elo")?.let { it1 -> elos.add(it1.toString()) }
            }
        }
    }

    companion object {
        const val HISTORY_TYPE = "type"


        fun newInstance(name: String): DisplayHistoryFragment {
            val fragment = DisplayHistoryFragment()

            val bundle = Bundle().apply {
                putString(HISTORY_TYPE, name)
            }

            fragment.arguments = bundle

            return fragment
        }
    }


    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    } */
}