package it.app.mytrainer.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import it.app.mytrainer.R
import it.app.mytrainer.ui.activities.home.schedule.ActivityScheduleViewAthlete
import kotlinx.android.synthetic.main.card_recycle_view_schedule_athlete.view.*

class RecycleScheduleAdapter(private val context: Context, private var data: ArrayList<String>) :
    RecyclerView.Adapter<RecycleScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_recycle_view_schedule_athlete, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTypeOfWO.text = data[position]

        holder.card.setOnClickListener {
            val intent = Intent(context, ActivityScheduleViewAthlete::class.java)
            intent.putExtra("TypeWO", data[position])
            context.startActivity(intent)
        }
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