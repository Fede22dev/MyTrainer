package it.app.mytrainer.ui.fragments.registrationTrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import it.app.mytrainer.R
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

        view.chip1.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip1, isChecked)
        }

        view.chip2.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip2, isChecked)
        }

        view.chip3.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip3, isChecked)
        }

        view.chip4.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip4, isChecked)
        }

        view.chip5.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip5, isChecked)
        }

        view.chip6.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip6, isChecked)
        }

        view.chip7.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip7, isChecked)
        }

        view.chip8.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip8, isChecked)
        }

        view.chip9.setOnCheckedChangeListener { buttonView, isChecked ->
            listManager(chip9, isChecked)
        }

        view.btnSaveTrainer.setOnClickListener {

        }

        return view
    }

    private fun listManager(chip: Chip, isCheck: Boolean) {
        if (chipList.size < 2 || !isCheck) {
            if (isCheck) {
                chipList.add(chip.text.toString())
            } else {
                chipList.remove(chip.text.toString())
            }
        } else {
            chip.isChecked = false
        }
    }

}