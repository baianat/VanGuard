package app.baianat.com.demoApp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.idling.CountingIdlingResource
import app.baianat.com.demoApp.R
import app.baianat.com.vanguard.rules.TextValidationRule
import app.baianat.com.vanguard.VanGuard
import app.baianat.com.vanguard.rules.*
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.CasesFactory
import app.baianat.com.vanguard.validationCase.Factory
import app.baianat.com.vanguard.validationCase.ForEditable
import app.baianat.com.vanguard.validationCase.ValidationCase
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.activity_fancy.*

class FancyActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        birthdateInput.text = "$year/$monthOfYear/$dayOfMonth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fancy)

        birthdateInput.setOnClickListener {
            SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this)
                .showTitle(true)
                .showDaySpinner(true)
                .spinnerTheme(R.style.NumberPickerStyle)
                .defaultDate(2019, 7, 1)
                .maxDate(2020, 0, 1)
                .minDate(1990, 0, 1)
                .build()
                .show()
        }

        val isSync = intent.getBooleanExtra("SYNC", false)
        if (isSync) {
            syncValidation()
        } else {
            asyncValidation()
        }

    }

    var nameValidation: ValidationCase<*, *>? = null
    var phoneValidation: ValidationCase<*, *>? = null

    private fun syncValidation() {


        nameValidation = CasesFactory
            .watch(nameInput)
            .forRule(IsNotEmpty("Name Field Can't Be Empty", "Name is optional by the way"))
            .doOnValidationStatusChange { isValid, _ ->
                "Is name valid  $isValid ".print()
            }
            .notRequired()
            .create()

        phoneValidation = CasesFactory
            .watch(phoneInput)
            .forRule(IsPhone("Please Enter valid Phone Number!", "Ex: 01057485952"))
            .create()

        setupOtherCases()
    }

    var mIdlingRes = CountingIdlingResource("fancyMock")

    private fun asyncValidation() {


        nameValidation = CasesFactory
            .watch(nameInput)
            .forRule(IsNotEmpty("Name can't be empty"))

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


        phoneValidation = CasesFactory
            .watch(phoneInput)
            .forRule(IsPhone("Please Enter ValidState Phone Number!", "Ex: 01057485952"))
            .doOnLocalValidationSuccess(true) { textToValidate, validationCase ->
                mIdlingRes.increment()
                Handler().postDelayed({
                    if (textToValidate.length > 8) {
                        validationCase.confirmValidation()
                    } else {
                        validationCase.failValidation("User of this phone already exists")
                    }
                    mIdlingRes.decrement()
                }, 1000)
            }
            .create()
//        nameInput.text = "Omar"
//        phoneInput.text = "3234343434"

        setupOtherCases()

    }

    var validator = VanGuard.of(this)

    private fun setupOtherCases() {


        val emailValidation = CasesFactory
            .watch(emailInput)
            .watch(emailInput)
            .forRule(IsEmail("Please Enter ValidState E-mail!", "Ex: this@this.com"))
            .notRequired()
            .create()


        val passwordValidation = CasesFactory
            .watch(passInput)
            .forRule(object : TextValidationRule() {
                override fun validate(valueToValidate: String): Boolean {
                    return valueToValidate.isEmpty()
                }

                override fun getHelperMessage(): String? {
                    return "should be empty"
                }

                override fun getErrorMessage(): String? {
                    return "if not empty, error"
                }
            })
            .create()

        val birthdateValidation = CasesFactory
            .watch(birthdateInput)
            .forRule(
                IsTimeAfter(
                    referenceTime = "1995/7/1",
                    pattern = "yyyy/MM/dd",
                    errorMsg = "Birth Date Can't be before 1995/7/1",
                    helperMsg = "Users should be +24 ys old"
                )
            )
            .asStatic()
            .create()

        validator?.validate(
            nameValidation,
            emailValidation,
            passwordValidation,
            phoneValidation,
            birthdateValidation
        )


        checkBtn.setOnClickListener {

            if (validator?.isFormValid() == true) {
                startActivity(Intent(this@FancyActivity, NativeActivity::class.java))

            } else {
                validator?.highLightErrors()
            }
//            validator?.doIfFormIsValid(true) {
//                //                Toast.makeText(it.context, "All ValidState", Toast.LENGTH_SHORT).show()
//
//                startActivity(Intent(this@FancyActivity, NativeActivity::class.java))
//
//            }
        }

        submitBtn.setOnClickListener {
            Toast.makeText(it.context, "All ValidState", Toast.LENGTH_SHORT).show()
        }


        validator?.doOnFormStatusChange {
            submitBtn.isEnabled = it
        }
    }
}
