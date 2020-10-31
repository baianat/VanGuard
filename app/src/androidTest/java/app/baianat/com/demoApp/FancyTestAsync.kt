package app.baianat.com.demoApp

import android.content.Intent
import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import app.baianat.com.demoApp.activities.FancyActivity
import app.baianat.com.demoApp.utils.setText
import app.baianat.com.espresso1.sneak
import app.baianat.com.espresso1.subViewOfId
import app.baianat.com.espresso1.viewOfId
import app.baianat.com.vanguard.views.GuardView
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FancyTestAsync {


    @Rule
    @JvmField
    var rule: ActivityTestRule<FancyActivity> =
        object : ActivityTestRule<FancyActivity>(FancyActivity::class.java) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation()
                    .targetContext
                val intent = Intent(targetContext, FancyActivity::class.java)
                intent.putExtra("SYNC", false)
                return intent
            }
        }

    private fun validInputs() {
        editText(R.id.birthdateInput).perform(ViewActions.scrollTo(), setText("2020/12/12"))
        editText(R.id.nameInput).perform(ViewActions.scrollTo(), ViewActions.typeText("Omar"))
        editText(R.id.emailInput).perform(
            ViewActions.scrollTo(),
            ViewActions.typeText("Omar@a.com")
        )
        editText(R.id.phoneInput).perform(
            ViewActions.scrollTo(), ViewActions.typeText("123523232323"),
            ViewActions.closeSoftKeyboard()
        )

    }

      @Test
    fun test_form_status_listener() {

        Espresso.onView(ViewMatchers.withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
        validInputs()
        Espresso.onView(ViewMatchers.withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        editText(R.id.nameInput).perform(scrollTo(), clearText())
        Espresso.onView(ViewMatchers.withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        editText(R.id.phoneInput).perform(ViewActions.scrollTo(), ViewActions.replaceText(""))
        Espresso.onView(ViewMatchers.withText("SUBMIT")).perform(ViewActions.scrollTo())
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))

    }


    fun editText(id: Int) = subViewOfId(R.id.editText, id)
    fun auxText(id: Int) = subViewOfId(R.id.auxTextView, id)
    fun sign(id: Int) = subViewOfId(R.id.outlineIndicator, id)
    fun <T : View> trueViewOfId(rule: ActivityTestRule<*>, @IdRes id: Int) =
        rule.activity.findViewById<T>(id)


    @Before
    fun registerIdlingRes() {
        IdlingRegistry.getInstance().register(rule.activity.mIdlingRes);

    }


    @After
    fun unRegisterIdlingRes() {
        IdlingRegistry.getInstance().unregister(rule.activity.mIdlingRes);
    }

}