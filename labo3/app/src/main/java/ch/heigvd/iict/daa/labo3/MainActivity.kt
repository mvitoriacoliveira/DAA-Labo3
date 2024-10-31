package ch.heigvd.iict.daa.labo3

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import ch.heigvd.iict.daa.labo3.databinding.ActivityMainBinding
import java.util.Calendar
import android.app.DatePickerDialog
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Format de date pour afficher dans editBirthdate
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinners()
        setupRadioGroupListener()
        binding.btnAnnuler.setOnClickListener { clearForm() }
        binding.imageButtonCake.setOnClickListener {
            datePicker()
        }
    }

    private fun datePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                binding.editBirthdate.setText(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun setupRadioGroupListener() {
        // Logique pour gérer le RadioGroup et afficher/masquer les sections
        binding.personType.setOnCheckedChangeListener { _, checkedId ->
            val constraintLayout = binding.constraintLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            when (checkedId) {
                binding.radioButtonStudent.id -> {
                    binding.groupStudent.visibility = View.VISIBLE
                    binding.groupWorkers.visibility = View.GONE
                    binding.textViewSpecificWorkerData.visibility = View.GONE
                    binding.textViewSpecificStudentsData.visibility = View.VISIBLE
                    constraintSet.connect(
                        binding.textViewAdditionalData.id, ConstraintSet.TOP,
                        binding.groupStudent.id, ConstraintSet.BOTTOM, 16
                    )
                }
                binding.radioButtonWorker.id -> {
                    binding.groupWorkers.visibility = View.VISIBLE
                    binding.groupStudent.visibility = View.GONE
                    binding.textViewSpecificWorkerData.visibility = View.VISIBLE
                    binding.textViewSpecificStudentsData.visibility = View.GONE
                    constraintSet.connect(
                        binding.textViewAdditionalData.id, ConstraintSet.TOP,
                        binding.groupWorkers.id, ConstraintSet.BOTTOM, 16
                    )
                }
            }
            constraintSet.applyTo(constraintLayout)
        }
    }

    private fun clearForm() {
        binding.editName.text.clear()
        binding.editFirstname.text.clear()
        binding.editBirthdate.text.clear()
        binding.editNationality.setSelection(0)
        binding.personType.clearCheck()
        binding.editSchool.text.clear()
        binding.editGraduateyear.text.clear()
        binding.editEmail.text.clear()
        binding.editComment.text.clear()
        binding.groupStudent.visibility = View.GONE
        binding.groupWorkers.visibility = View.GONE
    }

    private fun initSpinners() {
        binding.editNationality.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.nationality_empty)) + resources.getStringArray(R.array.nationalities).toList()
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.editSector.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.sectors_empty)) + resources.getStringArray(R.array.sectors).toList()
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }
}

