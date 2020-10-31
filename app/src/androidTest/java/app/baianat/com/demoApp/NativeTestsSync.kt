package app.baianat.com.demoApp

import android.widget.EditText
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import app.baianat.com.demoApp.activities.NativeActivity
import app.baianat.com.demoApp.utils.CustomMatcher.Companion.hasNoError
import app.baianat.com.demoApp.utils.CustomMatcher.Companion.withThisError
import app.baianat.com.espresso1.*
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry


@RunWith(AndroidJUnit4::class)
class NativeTestsSync {


    @Rule
    @JvmField
    var rule: ActivityTestRule<NativeActivity> =
        object : ActivityTestRule<NativeActivity>(NativeActivity::class.java) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation()
                    .targetContext
                val result = Intent(targetContext, NativeActivity::class.java)
                result.putExtra("SYNC", true)
                return result
            }
        }

    private fun validInputs() {
        trueViewOfId<TextView>(rule, R.id.timeTextView).text = "13:12"
        viewOfId(R.id.nameEditText).perform(scrollTo(), typeText("Omar"))
        viewOfId(R.id.emailEditText).perform(scrollTo(), typeText("Omar@a.com"))
        viewOfId(R.id.phoneEditText).perform(scrollTo(), typeText("1235"))
        viewOfId(R.id.postalCodeEditText).perform(scrollTo(), typeText("12353"))

    }

    /**
     * Assert formStatus listener works properly, based on the status of a BUTTON
     */
      @Test
    fun test_form_status_listener() {
        onView(withText("SUBMIT")).perform(scrollTo())
            .check(matches(not(ViewMatchers.isEnabled())))
        validInputs()
        onView(withText("SUBMIT")).perform(scrollTo())
            .check(matches(ViewMatchers.isEnabled()))
        viewOfId(R.id.postalCodeEditText).perform(scrollTo(), replaceText(""))
        onView(withText("SUBMIT")).perform(scrollTo())
            .check(matches(not(ViewMatchers.isEnabled())))
    }


      @Test
    fun test_errors_highlight_forcefully() {
        clickButton(R.id.checkBtn)

        viewOfId(R.id.timeTextView).check(matches(withThisError("Time can't be before 12:00")))
        viewOfId(R.id.postalCodeEditText).check(matches(withThisError("Please enter a valid postal code")))
        viewOfId(R.id.phoneEditText).check(matches(withThisError("Please enter a valid phone number")))

        viewOfId(R.id.nameEditText).perform(typeText("Omar"))
        viewOfId(R.id.checkBtn).perform(scrollTo())
        clickButton(R.id.checkBtn)
        viewOfId(R.id.nameEditText).check(matches(hasNoError()))

        viewOfId(R.id.postalCodeEditText).perform(typeText("218899"), closeSoftKeyboard())
        clickButton(R.id.checkBtn)
        viewOfId(R.id.postalCodeEditText).check(matches(hasNoError()))

        viewOfId(R.id.phoneEditText).perform(typeText("0129898"), closeSoftKeyboard())
        clickButton(R.id.checkBtn)
        viewOfId(R.id.phoneEditText).check(matches(hasNoError()))

        rule.runOnUiThread {
            trueViewOfId<TextView>(rule, R.id.timeTextView).text = "13:12"
        }
        viewOfId(R.id.timeTextView).check(matches(hasNoError()))
        onView(withText("SUBMIT")).perform(scrollTo())
            .check(matches(ViewMatchers.isEnabled()))

    }

     @Test
    fun notRequired() {
        viewOfId(R.id.nameEditText).perform(clearText(), closeSoftKeyboard())
        sneak()
        viewOfId(R.id.nameEditText).check(matches(hasNoError()))

    }

    @Test
    fun test_form_status_with_populated_inputs() {

        rule.runOnUiThread {
            trueViewOfId<TextView>(rule, R.id.timeTextView).text = "12:12"
            trueViewOfId<EditText>(rule, R.id.nameEditText).setText("Omar")
            trueViewOfId<EditText>(rule, R.id.emailEditText).setText("Omar@a.com")
        }

        viewOfId(R.id.phoneEditText).perform(scrollTo(), typeText("1235"))
        viewOfId(R.id.postalCodeEditText).perform(scrollTo(), typeText("123587"))

        onView(withText("SUBMIT")).perform(scrollTo())
            .check(matches(ViewMatchers.isEnabled()))

        viewOfId(R.id.nameEditText).perform(
            scrollTo(),
            replaceText(""),
            closeSoftKeyboard()
        )
        onView(withText("SUBMIT")).perform(scrollTo())
            .check(matches(ViewMatchers.isEnabled()))

        viewOfId(R.id.postalCodeEditText).perform(scrollTo(), replaceText(""), closeSoftKeyboard())

        onView(withText("SUBMIT")).perform(scrollTo())
            .check(matches(not(ViewMatchers.isEnabled())))

    }


}