package ch.epfl.sdp.blindwar.game.multi.partyMode.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindwar.R

class PartyRankingRecyclerAdapter(
    private var ranks: List<String>,
    private var pseudos: List<String>,
    private var correctAnswers: List<String>,
    private var wrongAnswers: List<String>
) : RecyclerView.Adapter<PartyRankingRecyclerAdapter.ViewHolder>()
    {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userRank: TextView = itemView.findViewById(R.id.userRank)
        val userPseudo: TextView = itemView.findViewById(R.id.userPseudo)
        val userElo: TextView = itemView.findViewById(R.id.userElo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.leaderboard_cardview,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Add an header to describe the data of the leaderboard
        if (position == 0) {
            holder.userRank.text = "Rank"
            holder.userPseudo.text = "Name"
            holder.userElo.text = "Correct ansower/Wrong answers"
        } else {
            holder.userRank.text = "#${(position)}"
            holder.userPseudo.text = pseudos[position - 1]
            holder.userElo.text =
                "${correctAnswers[position - 1]}/${wrongAnswers[position - 1]}"
        }
    }

    override fun getItemCount(): Int {
        return ranks.size
    }
}