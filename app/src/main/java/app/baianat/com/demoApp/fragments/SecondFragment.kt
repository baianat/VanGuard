package app.baianat.com.demoApp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.baianat.com.vanguard.*
import app.baianat.com.vanguard.rules.IsEmail
import app.baianat.com.vanguard.rules.IsNotEmpty
import app.baianat.com.vanguard.rules.IsPhone
import app.baianat.com.vanguard.rules.IsLengthGreaterThan
import kotlinx.android.synthetic.main.fragment_second.*
import app.baianat.com.demoApp.R


class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setupValidation()

    }

//    private fun setupValidation() {
//
//
//        val nameValidation = ValidationCase.ForDynamicText()
//            .watch(nameEditText)
//            .forRule(IsNotEmpty("Name can't be empty"))
//            .create()
//
//
//        val emailValidation = ValidationCase.ForDynamicText()
//            .watch(emailEditText)
//            .forRule(IsEmail("Please enter a valid email"))
//            .create()
//
//
//        val phoneValidation = ValidationCase.ForDynamicText()
//            .watch(phoneLayout)
//            .forRule(IsPhone("Please enter a valid phone number"))
//            .create()
//
//
//        val ageValidation = ValidationCase.ForDynamicText()
//            .watch(ageEditText)
//            .forRule(IsLengthGreaterThan(5, "please correct it"))
//            .create()
//
//        VanGuard.of(this)
//            .validate(
//                nameValidation,
//                emailValidation,
//                phoneValidation,
//                ageValidation
//            )
//
//        checkBtn.setOnClickListener {
//            VanGuard.of(this).doIfFormIsValid(true) {
//                Toast.makeText(it.context, "All ValidState", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        submitBtn.setOnClickListener {
//            Toast.makeText(it.context, "All ValidState", Toast.LENGTH_SHORT).show()
//        }
//
//
//    }

}
