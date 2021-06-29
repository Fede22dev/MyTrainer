package it.app.mytrainer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import it.app.mytrainer.R
import kotlinx.android.synthetic.main.card_recycle_view_schedule_athlete.view.*

class RecycleViewScheduleAthlete(
    private val context: Context,
    private val data: ArrayList<String>,
) : RecyclerView.Adapter<RecycleViewScheduleAthlete.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_recycle_view_schedule_athlete, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTypeOfWO.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(
        itemView: View,
        val textViewTypeOfWO: TextView = itemView.textViewCardTypeOfWO,
        val card: CardView = itemView.cardDaysOfSchedule,
    ) : RecyclerView.ViewHolder(itemView)
}
