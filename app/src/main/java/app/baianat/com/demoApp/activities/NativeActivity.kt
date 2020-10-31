package app.baianat.com.demoApp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import app.baianat.com.vanguard.VanGuard
import app.baianat.com.vanguard.rules.*
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.ValidationCase
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_native.*
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.appcompat.app.AppCompatActivity
import app.baianat.com.demoApp.R
import app.baianat.com.vanguard.validationCase.CasesFactory


class NativeActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        timeTextView.text = "$hourOfDay:$minute"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native)

        timeTextView.setOnClickListener {
            TimePickerDialog.newInstance(this, true).apply {
                show(supportFragmentManager, "TimePickerDialog")
            }
        }

        val isSync = intent.getBooleanExtra("SYNC", false)
        if (isSync) {
            syncValidation()
        } else {
            asyncValidation()
        }


    }

    private var nameValidation: ValidationCase<*, *>? = null

    private fun syncValidation() {

        nameValidation = CasesFactory
            .watch(nameEditText)
            .forRule(IsNotEmpty("Name can't be empty"))
            .doOnValidationStatusChange { isValid, _ ->
                "Is name valid  $isValid ".print()
            }
            .notRequired()
            .create()

        setupOtherCases()
    }

    var mIdlingRes = CountingIdlingResource("nativeMock")

    private fun asyncValidation() {

        nameValidation = CasesFactory
            .watch(nameEditText)
            .forRule(IsNotEmpty("Name can't be empty","Better starts with Upper case letter"))

            .doOnLocalValidationSuccess(true) { textToFurtherValidate, validationCase ->

                mIdlingRes.increment()
                Handler().postDelayed({
                    if (textToFurtherValidate == "Omar") {
                        validationCase.confirmValidation()
                    } else {
                        validationCase.failValidation("Not Omar")
                    }
                    mIdlingRes.decrement()

                }, 3000)
            }
            .notRequired()
            .doOnValidationStatusChange { isValid, view ->
                "Is name valid  $isValid ".print()
            }
            .create()

        setupOtherCases()

    }

    private val validator = VanGuard.of(this)

    private fun setupOtherCases() {

        val emailValidation = CasesFactory
            .watch(emailEditText)
            .forRule(IsEmail("Please enter a valid email","Ex:a@b.com"))
            .notRequired()
            .doOnValidationStatusChange { isValid, view ->
                "is email valid $isValid".print()
            }
            .create()

        val phoneValidation = CasesFactory
            .watch(phoneEditText)
            .forRule(IsPhone("Please enter a valid phone number", "Should start with +2"))
            .doOnValidationStatusChange { isValid, view ->
                "is phone valid $isValid ".print()
            }
            .create()


        val postalCodeValidation = CasesFactory
            .watch(postalCodeEditText)
            .forRule(IsLengthGreaterThan(4, "Please enter a valid postal code","Must be 5 digits"))
            .doOnValidationStatusChange { isValid, view ->
                "is postal code valid $isValid ".print()
            }
            .create()

        val timeValidation = CasesFactory
            .watch(timeTextView)
            .asStatic()
            .forRule(IsTimeAfter("12:00", "HH:mm", "Time can't be before 12:00"))
            .doOnValidationStatusChange { isValid, view ->
                "is time valid  $isValid ".print()
            }
            .create()

        submitBtn.setOnClickListener {
            startActivity(Intent(this@NativeActivity, FancyActivity::class.java))
            Toast.makeText(this@NativeActivity, "All ValidState", Toast.LENGTH_SHORT).show()
        }

        checkBtn.setOnClickListener {

            if (validator?.isFormValid()==true){
                Toast.makeText(this@NativeActivity, "All ValidState", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@NativeActivity, FancyActivity::class.java))

            }else{
                validator?.highLightErrors()
            }
//            validator?.doIfFormIsValid(true) {
//                Toast.makeText(this@NativeActivity, "All ValidState", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@NativeActivity, FancyActivity::class.java))
//            }
        }

        validator?.validate(
            nameValidation,
            emailValidation,
            phoneValidation,
            postalCodeValidation,
            timeValidation
        )

        validator?.doOnFormStatusChange {
            submitBtn.isEnabled = it
        }

    }


}
