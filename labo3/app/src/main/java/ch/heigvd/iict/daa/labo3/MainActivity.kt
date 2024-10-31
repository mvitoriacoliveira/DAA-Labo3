package ch.heigvd.iict.daa.labo3

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import ch.heigvd.iict.daa.labo3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation de ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisation des spinners
        initSpinners()

        // Logique pour afficher ou masquer les sections en fonction du type de personne
        setupRadioGroupListener()

        // Logique pour effacer le formulaire
        binding.btnAnnuler.setOnClickListener {
            clearForm()
        }
    }

    // Méthode pour gérer la sélection dans le RadioGroup
    private fun setupRadioGroupListener() {
        binding.personType.setOnCheckedChangeListener { _, checkedId ->
            val constraintLayout = binding.constraintLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            when (checkedId) {
                binding.radioButtonStudent.id -> {
                    // Afficher les champs pour étudiants et masquer ceux des travailleurs
                    binding.groupStudent.visibility = View.VISIBLE
                    binding.groupWorkers.visibility = View.GONE
                    binding.textViewSpecificWorkerData.visibility = View.GONE
                    binding.textViewSpecificStudentsData.visibility = View.VISIBLE

                    // Positionner "Données complémentaires" sous les champs des étudiants
                    constraintSet.connect(
                        binding.textViewAdditionalData.id, ConstraintSet.TOP,
                        binding.groupStudent.id, ConstraintSet.BOTTOM, 16
                    )
                }
                binding.radioButtonWorker.id -> {
                    // Afficher les champs pour travailleurs et masquer ceux des étudiants
                    binding.groupWorkers.visibility = View.VISIBLE
                    binding.groupStudent.visibility = View.GONE
                    binding.textViewSpecificWorkerData.visibility = View.VISIBLE
                    binding.textViewSpecificStudentsData.visibility = View.GONE

                    // Positionner "Données complémentaires" sous les champs des travailleurs
                    constraintSet.connect(
                        binding.textViewAdditionalData.id, ConstraintSet.TOP,
                        binding.groupWorkers.id, ConstraintSet.BOTTOM, 16
                    )
                }
            }

            // Appliquer les nouvelles contraintes
            constraintSet.applyTo(constraintLayout)
        }
    }




    // Méthode pour effacer le formulaire
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

    // Méthode pour initialiser les spinners
    private fun initSpinners() {
        // Initialisation du Spinner pour les nationalités
        binding.editNationality.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.nationality_empty)) + resources.getStringArray(R.array.nationalities).toList()
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Initialisation du Spinner pour les secteurs
        binding.editSector.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.sectors_empty)) + resources.getStringArray(R.array.sectors).toList()
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }
}
