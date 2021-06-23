package it.app.mytrainer.ui.adapter

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
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

class RecycleListExerciseAdapter(
    private val activity: Activity,
    private val data: ArrayList<ObjSearchExercise>,
) :
    RecyclerView.Adapter<RecycleListExerciseAdapter.ViewHolder>() {

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

        holder.card.setOnClickListener{
            val intent = Intent()
            intent.putExtra("NameExercise", exercise.nameExercise)
            activity.setResult(RESULT_OK, intent)
            activity.finish()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(
        itemView: View,
        val textViewTypeExercise: TextView = itemView.textViewTypeExercise,
        val textViewNameExercise: TextView = itemView.textViewCardNameOfExercise,
        val card: CardView = itemView.cardSearchExercise,
    ) : RecyclerView.ViewHolder(itemView)
}