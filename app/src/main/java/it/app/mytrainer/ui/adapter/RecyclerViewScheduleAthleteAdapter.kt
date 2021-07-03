package it.app.mytrainer.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import it.app.mytrainer.R
import it.app.mytrainer.firebase.firestore.FireStore
import kotlinx.android.synthetic.main.activity_view_data_athlete.*
import kotlinx.android.synthetic.main.card_recycle_view_schedule_athlete.view.*
import java.util.*

class RecycleViewScheduleAthlete(
    private val activity: Activity,
    private val data: ArrayList<String>,
    private val trainerId: String,
    private val athleteId: String,
) : RecyclerView.Adapter<RecycleViewScheduleAthlete.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_recycle_view_schedule_athlete, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTypeOfWO.text = data[position]
        holder.card.setOnLongClickListener {
            MaterialAlertDialogBuilder(activity)
                .setTitle(activity.getString(R.string.title_cancel_day_schedule))
                .setMessage(activity.getString(R.string.dialog_cancel_day_schedule))

                //If cancel has pressed anything happens
                .setNegativeButton(activity.getString(R.string.cancel_button)) { _, _ ->

                }
                //If accept button has pressed, the current id will be delete from auth, store and storage
                .setPositiveButton(activity.getString(R.string.accept_button)) { _, _ ->
                    val fireStore = FireStore()
                    fireStore.deleteDaySchedule(trainerId,
                        athleteId,
                        data[position],
                        data.size) { result ->
                        if (result) {
                            Snackbar.make(activity.linearLayoutViewDataAthlete,
                                data[position].capitalize(Locale.ROOT) + " " + activity.getString(R.string.succesfully_day_remove),
                                Snackbar.LENGTH_LONG)
                                .setBackgroundTint(ContextCompat.getColor(activity,
                                    R.color.app_foreground))
                                .setTextColor(ContextCompat.getColor(activity, R.color.white))
                                .show()
                            data.removeAt(position)
                            notifyDataSetChanged()
                        } else {
                            Snackbar.make(activity.linearLayoutViewDataAthlete,
                                activity.getString(R.string.id_trainer_mismatch),
                                Snackbar.LENGTH_LONG)
                                .setBackgroundTint(ContextCompat.getColor(activity,
                                    R.color.app_foreground))
                                .setTextColor(ContextCompat.getColor(activity, R.color.white))
                                .show()
                        }
                    }
                }.show()
            true
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
