package ch.heigvd.iict.daa.labo3

import Student
import Worker
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import ch.heigvd.iict.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.iict.daa.labo3.models.Student
import ch.heigvd.iict.daa.labo3.models.Worker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinners()
        setupRadioGroupListener()
        binding.btnAnnuler.setOnClickListener { clearForm() }
        binding.btnOk.setOnClickListener { onSaveButtonClick() }
        binding.imageButtonCake.setOnClickListener { datePicker() }
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
        binding.personType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioButtonStudent.id -> {
                    binding.groupStudent.visibility = View.VISIBLE
                    binding.groupWorkers.visibility = View.GONE
                    initializeViewWithPerson(Person.exampleStudent)
                }
                binding.radioButtonWorker.id -> {
                    binding.groupWorkers.visibility = View.VISIBLE
                    binding.groupStudent.visibility = View.GONE
                    initializeViewWithPerson(Person.exampleWorker)
                }
            }
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
        binding.editCompany.text.clear()
        binding.editSector.setSelection(0)
        binding.editExperience.text.clear()

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

    private fun createStudentFromForm(): Student {
        with(binding) {
            return Student(
                editName.text.toString(),
                editFirstname.text.toString(),
                getCalendarFromForm(),
                editNationality.selectedItem.toString(),
                editSchool.text.toString(),
                editGraduateyear.text.toString().toInt(),
                editEmail.text.toString(),
                editComment.text.toString()
            )
        }
    }

    private fun createWorkerFromForm(): Worker {
        with(binding) {
            return Worker(
                editName.text.toString(),
                editFirstname.text.toString(),
                getCalendarFromForm(),
                editNationality.selectedItem.toString(),
                editCompany.text.toString(),
                editSector.selectedItem.toString(),
                editExperience.text.toString().toInt(),
                editEmail.text.toString(),
                editComment.text.toString()
            )
        }
    }

    private fun initializeViewWithPerson(person: Person) {
        // Fill in the common fields
        binding.editName.setText(person.name)
        binding.editFirstname.setText(person.firstName)
        binding.editBirthdate.setText(dateFormatter.format(person.birthDay.time))
        binding.editNationality.setSelection(getNationalityIndex(person.nationality))
        binding.editEmail.setText(person.email)
        binding.editComment.setText(person.remark)

        // Check the type of the person and initialize accordingly
        when (person) {
            is Student -> {
                // Set the view to Student mode
                binding.radioButtonStudent.isChecked = true
                binding.groupStudent.visibility = View.VISIBLE
                binding.groupWorkers.visibility = View.GONE

                // Fill in student-specific fields
                binding.editSchool.setText(person.university)
                binding.editGraduateyear.setText(person.graduationYear.toString())
            }
            is Worker -> {
                // Set the view to Worker mode
                binding.radioButtonWorker.isChecked = true
                binding.groupWorkers.visibility = View.VISIBLE
                binding.groupStudent.visibility = View.GONE

                // Fill in worker-specific fields
                binding.editCompany.setText(person.company)
                binding.editSector.setSelection(getSectorIndex(person.sector))
                binding.editExperience.setText(person.experienceYear.toString())
            }
        }
    }

    private fun getIndexFromArray(array: Array<String>, item: String): Int {
        return ((array.indexOf(item).takeIf { it >= 0 } ?: 0) + 1)
    }

    // Get index of nationality in the spinner
    private fun getNationalityIndex(nationality: String): Int {
        val nationalitiesArray = resources.getStringArray(R.array.nationalities)
        return getIndexFromArray(nationalitiesArray, nationality)
    }

    // Get index of sector in the spinner
    private fun getSectorIndex(sector: String): Int {
        val sectorsArray = resources.getStringArray(R.array.sectors)
        return getIndexFromArray(sectorsArray, sector)
    }


    private fun getCalendarFromForm(): Calendar {
        return Calendar.getInstance().also {
            it.time = dateFormatter.parse(binding.editBirthdate.text.toString()) ?: Date()
        }
    }

    private fun onSaveButtonClick() {
        if (binding.radioButtonStudent.isChecked) {
            // Check if nationality is selected
            val nationality = binding.editNationality.selectedItemPosition
            if (nationality == 0) {
                Toast.makeText(this, "Veuillez sélectionner une nationalité", Toast.LENGTH_SHORT).show()
                return
            }

            val student = createStudentFromForm()
            Toast.makeText(this, "Étudiant créé : $student", Toast.LENGTH_SHORT).show()
        } else if (binding.radioButtonWorker.isChecked) {
            // Check if nationality and sector are selected
            val nationality = binding.editNationality.selectedItemPosition
            val sector = binding.editSector.selectedItemPosition
            if (nationality == 0 || sector == 0) {
                Toast.makeText(this, "Veuillez sélectionner une nationalité et un secteur", Toast.LENGTH_SHORT).show()
                return
            }

            val worker = createWorkerFromForm()
            Toast.makeText(this, "Travailleur créé : $worker", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Veuillez sélectionner un type de personne", Toast.LENGTH_SHORT).show()
        }
    }
}
