package app.baianat.com.demoApp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.baianat.com.vanguard.VanGuard
import app.baianat.com.vanguard.rules.WidgetValidationRule
import app.baianat.com.vanguard.rules.*
import app.baianat.com.demoApp.R
import app.baianat.com.vanguard.validationCase.CasesFactory
import kotlinx.android.synthetic.main.activity_widgets.*


class WidgetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widgets)

        defineViewListeners()

        setupValidation()

    }

    private val validator = VanGuard.of(this)

    private fun defineViewListeners() {
        val country = arrayOf("please select one", "India", "USA", "China", "Japan", "Other")
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, country)
        spinner.adapter = aa


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ageTv.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        checkBox.setOnCheckedChangeListener { _, _ ->
            Toast.makeText(this, "CheckBox is clicked", Toast.LENGTH_LONG).show()
        }

        sendMailsSwitch.setOnCheckedChangeListener { _, _ ->
            Toast.makeText(this, "Switch is clicked", Toast.LENGTH_LONG).show()
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view?.isPressed == false) return

                Toast.makeText(
                    this@WidgetsActivity, "Spinner is clicked", Toast.LENGTH_LONG
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            Toast.makeText(
                this@WidgetsActivity,
                "radioGroup is clicked",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupValidation() {
        val wasy = CasesFactory
            .watch(checkBox)
            .forRule(IsChecked())
            .doOnValidationStatusChange { status, _ ->
                if (status) {
                    checkBoxAux.visibility = View.GONE
                } else {
                    checkBoxAux.visibility = View.VISIBLE
                    checkBoxAux.text = "this field must be checked"
                }
            }
            .create()

        val acceptTermsValidationCase = CasesFactory.watch(checkBox)
            .forRule(IsChecked())
            .doOnValidationStatusChange { status, _ ->
                if (status) {
                    checkBoxAux.visibility = View.GONE
                } else {
                    checkBoxAux.visibility = View.VISIBLE
                    checkBoxAux.text = "this field must be checked"
                }
            }
            .create()

        val sendEmailValidationCase = CasesFactory
            .watch(sendMailsSwitch)
            .forRule(IsChecked())
            .doOnValidationStatusChange { status, _ ->
                if (status) {
                    switchAux.visibility = View.GONE
                } else {
                    switchAux.visibility = View.VISIBLE
                    switchAux.text = "this field must be checked"
                }
            }
            .create()

        val radioGroupCase = CasesFactory
            .watch(radioGroup)
            .forRule(IsThisOneChecked(R.id.femaleBtn))
            .doOnValidationStatusChange { status, view ->
                if (status) {
                    radioGroupAux.visibility = View.GONE
                } else {
                    radioGroupAux.visibility = View.VISIBLE
                    radioGroupAux.text = "Must be female"
                }
            }
            .create()

        val spinnerCase = CasesFactory
            .watch(spinner)
            .forRule(
                IsPositionIncludedIn(validPositionsSet = arrayOf(3, 4, 5))
            )
            .doOnValidationStatusChange { status, view ->
                if (status) {
                    spinnerAux.visibility = View.GONE
                } else {
                    spinnerAux.visibility = View.VISIBLE
                    spinnerAux.text = "Please select a valid country"
                }
            }
            .create()


        val seekBarCase = CasesFactory
            .watch(seekBar)
            .forRule(IsProgressAboveOrEqual(referenceProgress = 25))
            .doOnValidationStatusChange { isValid, view ->
                if (isValid) {
                    seekBarAux.visibility = View.GONE
                } else {
                    seekBarAux.visibility = View.VISIBLE
                    seekBarAux.text = "Age must be 25+"
                }
            }
            .create()



        validator
            ?.validate(
                acceptTermsValidationCase,
                spinnerCase,
                seekBarCase,
                radioGroupCase,
                sendEmailValidationCase
            )?.doOnFormStatusChange {
                submitBtn.isEnabled = it
            }

        checkBtn.setOnClickListener {
            validator?.doIfFormIsValid(highlightErrors = true, whatToDo = {


            })

        }

        submitBtn.setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
            Toast.makeText(this, "All ValidState", Toast.LENGTH_SHORT).show()
        }
    }


}
