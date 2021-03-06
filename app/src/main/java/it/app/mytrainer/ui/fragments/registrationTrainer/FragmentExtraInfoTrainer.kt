package it.app.mytrainer.ui.fragments.registrationTrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.MapTrainer
import kotlinx.android.synthetic.main.fragment_extra_info_trainer.*
import kotlinx.android.synthetic.main.fragment_extra_info_trainer.view.*
import java.util.*

/**
 * Managing all the data that are insert
 * in that fragment (Gym, Specialization)
 */

class FragmentExtraInfoTrainer : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val view = inflater.inflate(R.layout.fragment_extra_info_trainer, container, false)

        //Setting the action on chips. If selected it will be put in the
        //trainer map
        view.chip1SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip1SpecTrainer.text.toString())
        }

        view.chip2SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip2SpecTrainer.text.toString())
        }

        view.chip3SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip3SpecTrainer.text.toString())
        }

        view.chip4SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip4SpecTrainer.text.toString())
        }

        view.chip5SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip5SpecTrainer.text.toString())
        }

        view.chip6SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip6SpecTrainer.text.toString())
        }

        view.chip7SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip7SpecTrainer.text.toString())
        }

        view.chip8SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip8SpecTrainer.text.toString())
        }

        view.chip9SpecTrainer.setOnClickListener {
            MapTrainer.putSpec(view.chip9SpecTrainer.text.toString())
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        setupGymField()
    }

    // Fun for the gym field, when it change, if is not blank or null,
    // it will be puttied in the trainer map
    private fun setupGymField() {
        gymFieldTrainer.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotBlank()) {
                    MapTrainer.putGym(text.toString().trim()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
                } else {
                    MapTrainer.removeGym()
                }
            }
        }
    }
}