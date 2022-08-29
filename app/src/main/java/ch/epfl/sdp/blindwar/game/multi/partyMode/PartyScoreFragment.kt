package ch.epfl.sdp.blindwar.game.multi.partyMode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R

/**
 * Fragment with to display fragment_party_score.xml with a back button to return to the previous fragment
 *
 */
class PartyScoreFragment: Fragment() {

    private lateinit var quitTemp: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_party_score, container, false)
        quitTemp = view.findViewById(R.id.quitTemp)
        quitTemp.setOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }
    /*
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_party_score, container, false)
        quitTemp = view.findViewById<ImageButton>(R.id.quitTemp).also { button ->
            button.setOnClickListener {
                activity?.onBackPressed()
            }
        }
        return view
    }*/



}