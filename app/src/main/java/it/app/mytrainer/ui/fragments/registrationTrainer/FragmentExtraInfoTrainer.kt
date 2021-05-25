package it.app.mytrainer.ui.fragments.registrationTrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import it.app.mytrainer.R
import it.app.mytrainer.models.Trainer
import kotlinx.android.synthetic.main.fragment_extra_info_trainer.*
import kotlinx.android.synthetic.main.fragment_extra_info_trainer.view.*

class FragmentExtraInfoTrainer : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_extra_info_trainer, container, false)

        view.chip1SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip1SpecTrainer.text.toString())
        }

        view.chip2SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip2SpecTrainer.text.toString())
        }

        view.chip3SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip3SpecTrainer.text.toString())
        }

        view.chip4SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip4SpecTrainer.text.toString())
        }

        view.chip5SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip5SpecTrainer.text.toString())
        }

        view.chip6SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip6SpecTrainer.text.toString())
        }

        view.chip7SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip7SpecTrainer.text.toString())
        }

        view.chip8SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip8SpecTrainer.text.toString())
        }

        view.chip9SpecTrainer.setOnClickListener {
            Trainer.putSpec(view.chip9SpecTrainer.text.toString())
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        gymFieldTrainer.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotBlank()) {
                    Trainer.putGym(text.toString().trim())
                } else {
                    Trainer.removeGym()
                }
            }
        }
    }
}