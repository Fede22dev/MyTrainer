package it.app.mytrainer.ui.fragments.registrationTrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import it.app.mytrainer.R
import it.app.mytrainer.models.Trainer
import kotlinx.android.synthetic.main.fragment_info_trainer.*
import kotlinx.android.synthetic.main.fragment_info_trainer.view.*


class FragmentInfoTrainer : Fragment() {

    private lateinit var chipList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_info_trainer, container, false)

        chipList = ArrayList()

        view.chip1.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip2.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip3.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip4.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip5.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip6.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip7.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip8.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        view.chip9.setOnCheckedChangeListener { buttonView, _ ->
            Trainer.putSpec(buttonView.text.toString())
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        gymFieldTrainer.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotBlank()) {
                    Trainer.putGym(text.toString())
                } else {
                    Trainer.removeGym()
                }
            }
        }
    }
}