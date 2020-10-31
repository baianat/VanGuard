package app.baianat.com.demoApp.forDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import app.baianat.com.demoApp.R
import app.baianat.com.vanguard.VanGuard
import app.baianat.com.vanguard.rules.IsChecked
import app.baianat.com.vanguard.rules.IsEmail
import app.baianat.com.vanguard.rules.IsLengthGreaterThan
import app.baianat.com.vanguard.rules.IsTimeAfter
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.CasesFactory
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_demo1.*

class DemoActivity1 : AppCompatActivity() {

    val validator = VanGuard.of(activity = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo1)

//        textView4.setOnClickListener {
//                SpinnerDatePickerDialogBuilder()
//                    .context(this)
//                    .callback(this)
//                    .showTitle(true)
//                    .showDaySpinner(true)
//                    .spinnerTheme(R.style.NumberPickerStyle)
//                    .defaultDate(2019, 7, 1)
//                    .maxDate(2020, 0, 1)
//                    .minDate(1990, 0, 1)
//                    .build()
//                    .show()
//
//        }
//
//        textView4.text = "12/12/2019"


        val emailValidationCase = CasesFactory
            .watch(editText3)
            .forRule(IsEmail(errorMsg = "Please enter a valid e-mail!"))
            .doOnLocalValidationSuccess { textToFurtherValidate, validationCase ->
                Handler().postDelayed({
                    validationCase.confirmValidation()
                },2000)
            }
            .create()

        VanGuard.of(this)?.validate(emailValidationCase)
            ?.doOnFormStatusChange { isFormValid ->
                submitBtn.isEnabled = isFormValid
            }

//        val emailValidationCase = CasesFactory.ForEditable()
//            .watch(emailEditText)
//            .forRule(IsEmail(errorMsg = "Please enter a valid e-mail!"))
//            .create()
//
//        val passwordValidationCase = CasesFactory.ForEditable()
//            .watch(passwordEditText)
//            .forRule(IsLengthGreaterThan(length = 5,errorMsg = "Password can't be less than 6 chars!"))
//            .create()
//
//        val notificationValidationCase = CasesFactory.ForCheckable()
//            .watch(notificationsCheckBox)
//            .forRule(IsChecked())
//            .create()

//        VanGuard.of(this)
//            ?.validate(emailValidationCase, passwordValidationCase, notificationValidationCase)

//            ?.doOnFormStatusChange { isFormValid ->
//                submitBtn.isEnabled = isFormValid
//            }

    }
}
