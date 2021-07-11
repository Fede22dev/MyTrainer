package it.app.mytrainer.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.app.mytrainer.R
import it.app.mytrainer.models.ObjAthlete
import it.app.mytrainer.ui.activities.home.schedule.trainer.ActivityViewDataAthlete
import kotlinx.android.synthetic.main.card_recycle_view_list_client_trainer.view.*
import java.util.*

/**
 * Adapter use to manage the recycler for the
 * list client of trainer (all and followed)
 */

class RecyclerListClientTrainerAdapter(
    private val context: Context,
    private val data: ArrayList<ObjAthlete>,
) : RecyclerView.Adapter<RecyclerListClientTrainerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_recycle_view_list_client_trainer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val athlete = data[position]
        holder.textViewName.text = athlete.nameAthlete.capitalize(Locale.ROOT)
        holder.textViewSurname.text = athlete.surnameAthlete.capitalize(Locale.ROOT)

        if (athlete.urlPhotoAthlete.isNotBlank()) {
            Glide.with(context).load(athlete.urlPhotoAthlete.toUri()).into(holder.photoClient)
        } else {
            holder.photoClient.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.person_placeholder))
        }

        holder.card.setOnClickListener {
            val intent = Intent(context, ActivityViewDataAthlete::class.java)
            intent.putExtra("Athlete", athlete)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //Creating the single card
    inner class ViewHolder(
        itemView: View,
        val photoClient: ImageView = itemView.photoClient,
        val textViewName: TextView = itemView.textViewNameClientTrainer,
        val textViewSurname: TextView = itemView.textViewSurnameClientTrainer,
        val card: CardView = itemView.cardListClientTrainer,
    ) : RecyclerView.ViewHolder(itemView)
}