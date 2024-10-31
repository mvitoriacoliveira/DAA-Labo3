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
            // Utilisez le nouvel ID du ConstraintLayout
            val constraintLayout = binding.constraintLayout // Utilise maintenant l'ID "constraint_layout"
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            when (checkedId) {
                binding.radioButtonStudent.id -> {
                    // Afficher les champs supplémentaires pour les étudiants
                    binding.groupStudent.visibility = View.VISIBLE
                    // Placer "Données complémentaires" sous "Données spécifiques aux étudiants"
                    constraintSet.connect(
                        binding.textViewAdditionalData.id, ConstraintSet.TOP,
                        binding.groupStudent.id, ConstraintSet.BOTTOM, 16
                    )
                }
                binding.radioButtonWorker.id -> {
                    // Masquer les champs supplémentaires pour les étudiants
                    binding.groupStudent.visibility = View.GONE
                    // Placer "Données complémentaires" sous le RadioGroup
                    constraintSet.connect(
                        binding.textViewAdditionalData.id, ConstraintSet.TOP,
                        binding.personType.id, ConstraintSet.BOTTOM, 16
                    )
                }
            }
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
    }

    // Méthode pour initialiser les spinners
    private fun initSpinners() {
        binding.editNationality.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.nationality_empty)) + resources.getStringArray(R.array.nationalities).toList()
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }
}
