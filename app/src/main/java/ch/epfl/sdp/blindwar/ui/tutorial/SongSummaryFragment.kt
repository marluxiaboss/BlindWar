package ch.epfl.sdp.blindwar.ui.tutorial

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso

class SongSummaryFragment : Fragment() {
    private lateinit var likeAnimation: LottieAnimationView
    private lateinit var skip: ImageButton
    private var likeSwitch: Boolean = false
    private var success: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_song_summary, container, false)

        val artistView = view.findViewById<ImageView>(R.id.artistImageView)
        val artistText = view.findViewById<TextView>(R.id.artistTextView)
        val trackText = view.findViewById<TextView>(R.id.trackTextView)

        /** Like animation **/
        likeAnimation = view.findViewById(R.id.likeView)

        likeSwitch = if (arguments != null && (arguments?.containsKey("liked")!!)) {
            arguments?.getBoolean("liked")!!
        } else false

        setLikeListener()

        /** Background color **/
        success = arguments?.get("success") as Boolean
        val background = view.findViewById<ConstraintLayout>(R.id.song_summary_fragment)

        if (success) {
            background.setBackgroundColor(resources.getColor(R.color.success))
        } else {
            background.setBackgroundColor(resources.getColor(R.color.black))
        }

        /** Like animation **/
        likeAnimation = view.findViewById(R.id.likeView)

        skip = view.findViewById<ImageButton>(R.id.skip_next_summary).also { button ->
            button.setOnClickListener{
                activity?.onBackPressed()
            }
        }

        likeSwitch = if (arguments != null && (arguments?.containsKey("liked")!!)) {
            val layout = view.findViewById<ConstraintLayout>(R.id.song_summary_fragment)
            skip.visibility = View.GONE
            layout.background =
                if (success)
                    view.resources.getDrawable(R.drawable.back_frame_success)
                else
                    view.resources.getDrawable(R.drawable.back_frame_failure)
            arguments?.getBoolean("liked")!!
        }  else
            false

        setLikeListener()

        /** TODO: define constant key Strings **/
        artistText.text = arguments?.get("artist").toString()
        trackText.text = arguments?.get("title").toString()
        Picasso.get().load(arguments?.get("image").toString()).into(artistView)

        return view
    }

    private fun setLikeListener() {
        if (likeSwitch) {
            likeAnimation.setMinAndMaxFrame(45, 70)
        } else {
            likeAnimation.setMinAndMaxFrame(10, 30)
        }

        likeAnimation.setOnClickListener {
            if (!likeSwitch) {
                likeAnimation.setMinAndMaxFrame(10, 30)
                likeAnimation.repeatCount = 0
                //likeAnim.speed = 1f
                likeAnimation.playAnimation()
            } else {
                likeAnimation.setMinAndMaxFrame(45, 70)
                likeAnimation.repeatCount = 0
                likeAnimation.playAnimation()
            }

            likeSwitch = !likeSwitch
        }
    }

    fun liked(): Boolean {
        return likeSwitch
    }

    fun success(): Boolean {
        return success
    }
}