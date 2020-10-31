package app.baianat.com.demoApp

import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import app.baianat.com.demoApp.activities.FancyActivity
import app.baianat.com.demoApp.activities.WidgetsActivity
import app.baianat.com.demoApp.utils.CustomMatcher
import app.baianat.com.demoApp.utils.setText
import app.baianat.com.espresso1.*
import app.baianat.com.vanguard.views.GuardView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)

class FancyTestsSync {

    @Rule
    @JvmField
    var rule: ActivityTestRule<FancyActivity> =
        object : ActivityTestRule<FancyActivity>(FancyActivity::class.java) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation()
                    .targetContext
                val intent = Intent(targetContext, FancyActivity::class.java)
                intent.putExtra("SYNC", true)
                return intent
            }
        }


    private fun validInputs() {
        editText(R.id.birthdateInput).perform(ViewActions.scrollTo(), setText("2020/12/12"))
        editText(R.id.nameInput).perform(ViewActions.scrollTo(), typeText("Omar"))
        editText(R.id.emailInput).perform(ViewActions.scrollTo(), typeText("Omar@a.com"))
        //    editText(R.id.passInput).perform(ViewActions.scrollTo(), typeText("1235678"))
        editText(R.id.phoneInput).perform(
            ViewActions.scrollTo(), typeText("1235"),
            closeSoftKeyboard()
        )

    }

    /**
     * Assert formStatus listener works properly, based on the status of a BUTTON
     */
       @Test
    fun test_form_status_listener() {
        Espresso.onView(withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(matches(not(isEnabled())))
        validInputs()
        Espresso.onView(withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(matches(isEnabled()))
        editText(R.id.phoneInput).perform(ViewActions.scrollTo(), ViewActions.replaceText(""))
        Espresso.onView(withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(matches(not(isEnabled())))
    }

    @Test
    fun test_errors_highlight_forcefully() {
        clickButton(R.id.checkBtn)

        auxText(R.id.phoneInput).check(matches(withText("Please Enter valid Phone Number!")))
        sign(R.id.phoneInput).check(matches(isDisplayed()))

        auxText(R.id.birthdateInput).check(matches(isDisplayed()))
        sign(R.id.birthdateInput).check(matches(isDisplayed()))

        validInputs()
        Espresso.onView(withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(matches(isEnabled()))
    }

    @Test
    fun test_name() {
        editText(R.id.nameInput).perform(
            typeText("O")
        )
        sign(R.id.nameInput).check(matches((not(isDisplayed()))))
        editText(R.id.nameInput).perform(
            typeText("O")
            , closeSoftKeyboard()
        )
        sneak()
        sign(R.id.nameInput).check(matches(((isDisplayed()))))

    }

    @Test
    fun test_form_status_with_populated_inputs() {

        rule.runOnUiThread {
            trueViewOfId<GuardView>(rule, R.id.phoneInput).text = "2312323"
            trueViewOfId<GuardView>(rule, R.id.birthdateInput).text = "2020/12/12"
        }

        Espresso.onView(withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(matches(isEnabled()))
    }


    fun editText(id: Int) = subViewOfId(R.id.editText, id)
    fun auxText(id: Int) = subViewOfId(R.id.auxTextView, id)
    fun sign(id: Int) = subViewOfId(R.id.outlineIndicator, id)
    fun <T : View> trueViewOfId(rule: ActivityTestRule<*>, @IdRes id: Int) =
        rule.activity.findViewById<T>(id)

}