package it.app.mytrainer.ui.adapter

import android.app.Activity
import android.content.Intent
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
import it.app.mytrainer.ui.activities.home.schedule.trainer.ActivityUpdateSchedule
import kotlinx.android.synthetic.main.activity_view_data_athlete.*
import kotlinx.android.synthetic.main.card_recycle_view_schedule_athlete.view.*
import java.util.*

/**
 * Adapter use to manage the recycle in the viewDataAthlete
 */

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
        val fireStore = FireStore()

        holder.card.setOnClickListener {
            MaterialAlertDialogBuilder(activity)
                .setTitle(activity.getString(R.string.popup_manage_view_data_athlete))
                .setMessage(activity.getString(R.string.popup_manage_text_view_data_athlete))

                //If update button has pressed, the day will open and the edit mode is available
                .setNegativeButton(activity.getString(R.string.popup_update_view_data_athlete)) { _, _ ->
                    fireStore.checkIdTrainer(athleteId, trainerId) { result ->
                        if (result) {
                            val intent = Intent(activity, ActivityUpdateSchedule::class.java)
                            intent.putExtra("UserId", athleteId)
                            intent.putExtra("TypeWO", data[position])
                            activity.startActivity(intent)
                        } else {
                            Snackbar.make(activity.linearLayoutViewDataAthlete,
                                activity.getString(R.string.id_trainer_mismatch),
                                Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(activity,
                                    R.color.app_foreground))
                                .setTextColor(ContextCompat.getColor(activity,
                                    R.color.white))
                                .show()
                        }
                    }
                }

                .setPositiveButton(activity.getString(R.string.popup_delete_view_data_athlete)) { _, _ ->

                    MaterialAlertDialogBuilder(activity)
                        .setTitle(activity.getString(R.string.title_cancel_day_schedule))
                        .setMessage(activity.getString(R.string.dialog_cancel_day_schedule))

                        .setNegativeButton(activity.getString(R.string.cancel_button)) { _, _ ->

                        }

                        //If delete button has pressed, the current day will be delete
                        .setPositiveButton(activity.getString(R.string.accept_button)) { _, _ ->
                            fireStore.deleteDaySchedule(trainerId,
                                athleteId,
                                data[position],
                                data.size) { result ->
                                if (result) {
                                    Snackbar.make(activity.linearLayoutViewDataAthlete,
                                        data[position].replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                        } + " " + activity.getString(
                                            R.string.succesfully_day_remove),
                                        Snackbar.LENGTH_LONG)
                                        .setBackgroundTint(ContextCompat.getColor(activity,
                                            R.color.app_foreground))
                                        .setTextColor(ContextCompat.getColor(activity,
                                            R.color.white))
                                        .show()
                                    data.removeAt(position)
                                    notifyDataSetChanged()
                                } else {
                                    Snackbar.make(activity.linearLayoutViewDataAthlete,
                                        activity.getString(R.string.id_trainer_mismatch),
                                        Snackbar.LENGTH_SHORT)
                                        .setBackgroundTint(ContextCompat.getColor(activity,
                                            R.color.app_foreground))
                                        .setTextColor(ContextCompat.getColor(activity,
                                            R.color.white))
                                        .show()
                                }
                            }
                        }
                        .show()
                }

                //If cancel has pressed anything happens
                .setNeutralButton(activity.getString(R.string.cancel_button)) { _, _ ->
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //Setting the card, with the right name at the right position
    inner class ViewHolder(
        itemView: View,
        val textViewTypeOfWO: TextView = itemView.textViewCardTypeOfWO,
        val card: CardView = itemView.cardDaysOfSchedule,
    ) : RecyclerView.ViewHolder(itemView)
}