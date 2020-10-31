package app.baianat.com.demoApp

import android.content.Intent
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import app.baianat.com.demoApp.activities.WidgetsActivity
import app.baianat.com.demoApp.utils.*
import app.baianat.com.espresso1.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)

class WidgetsTests {

    @Rule
    @JvmField
    var rule: ActivityTestRule<WidgetsActivity> =
        object : ActivityTestRule<WidgetsActivity>(WidgetsActivity::class.java) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation()
                    .targetContext
                val intent = Intent(targetContext, WidgetsActivity::class.java)
                return intent
            }
        }


     // @Test
    fun testCheckables() {

        viewOfId(R.id.checkBox).perform(check())
        checkViewIsNotVisible(R.id.checkBoxAux)
        checkToastIsVisible("CheckBox is clicked", rule.activity)

        viewOfId(R.id.checkBox).perform(unCheck())
        checkViewIsVisible(R.id.checkBoxAux)
        checkToastIsVisible("CheckBox is clicked", rule.activity)

        clickButton(R.id.checkBtn)
        checkViewIsVisible(R.id.checkBoxAux)
        checkToastIsVisible("CheckBox is clicked", rule.activity)


    }


     // @Test
    fun testSeekBar() {

        viewOfId(R.id.seekBar).perform(setProgress(22))
        viewOfId(R.id.ageTv).check(matches(withText("22")))
        checkViewIsVisible(R.id.seekBarAux)
        viewOfId(R.id.seekBar).perform(setProgress(29))
        checkViewIsNotVisible(R.id.seekBarAux)
        viewOfId(R.id.ageTv).check(matches(withText("29")))

    }


     @Test
    fun testSpinner() {

        viewOfId(R.id.spinner).perform(setSelectedPosition(1))
        checkViewIsVisible(R.id.spinnerAux)
     //   checkToastIsVisible("Spinner is clicked", rule.activity)

        viewOfId(R.id.spinner).perform(setSelectedPosition(4))
        checkViewIsNotVisible(R.id.spinnerAux)
//        checkToastIsVisible("Spinner is clicked", rule.activity)

    }

   // @Test
    fun testRadioGroup() {
        viewOfId(R.id.femaleBtn).perform(click())
        checkViewIsNotVisible(R.id.radioGroupAux)
        checkToastIsVisible("radioGroup is clicked", rule.activity)

        viewOfId(R.id.maleBtn).perform(click())
        checkViewIsVisible(R.id.radioGroupAux)
        checkToastIsVisible("radioGroup is clicked", rule.activity)

    }

   //@Test
    fun testListener() {
        viewOfId(R.id.checkBox).perform(check())
        viewOfId(R.id.sendMailsSwitch).perform(check())
        viewOfId(R.id.seekBar).perform(setProgress(29))
        viewOfId(R.id.spinner).perform(setSelectedPosition(4))
        viewOfId(R.id.femaleBtn).perform(click())

        viewOfId(R.id.submitBtn).check(matches(isEnabled()))


    }
}