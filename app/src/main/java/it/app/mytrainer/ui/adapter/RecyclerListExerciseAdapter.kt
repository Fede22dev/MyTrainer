package it.app.mytrainer.ui.adapter

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import it.app.mytrainer.R
import it.app.mytrainer.models.ObjSearchExercise
import kotlinx.android.synthetic.main.card_recycle_view_search_exercise.view.*
import me.zhanghai.android.fastscroll.PopupTextProvider
import java.util.*

/**
 * Adapter used to manage the recycler used in the
 * search option, inside the creation of the schedule.
 */

class RecyclerListExerciseAdapter(
    private val activity: Activity,
    private val data: ArrayList<ObjSearchExercise>,
) : RecyclerView.Adapter<RecyclerListExerciseAdapter.ViewHolder>(), PopupTextProvider {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_recycle_view_search_exercise, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = data[position]
        holder.textViewTypeExercise.text = exercise.muscle
        holder.textViewNameExercise.text = exercise.nameExercise

        holder.card.setOnClickListener {
            val intent = Intent()
            intent.putExtra("NameExercise",
                exercise.nameExercise!!.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
            activity.setResult(RESULT_OK, intent)
            activity.finish()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getPopupText(position: Int): String {
        return data[position].muscle.toString()
    }

    //Creating the single card
    inner class ViewHolder(
        itemView: View,
        val textViewTypeExercise: TextView = itemView.textViewTypeExercise,
        val textViewNameExercise: TextView = itemView.textViewCardNameOfExercise,
        val card: CardView = itemView.cardSearchExercise,
    ) : RecyclerView.ViewHolder(itemView)
}