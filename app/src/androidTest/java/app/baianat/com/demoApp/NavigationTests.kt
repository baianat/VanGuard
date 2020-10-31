package app.baianat.com.demoApp

import android.content.Intent
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.baianat.com.demoApp.activities.NativeActivity
import app.baianat.com.demoApp.utils.CustomMatcher.Companion.hasNoError
import app.baianat.com.demoApp.utils.CustomMatcher.Companion.withThisError
import app.baianat.com.espresso1.clickButton
import app.baianat.com.espresso1.trueViewOfId
import app.baianat.com.espresso1.viewOfId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.platform.app.InstrumentationRegistry
import app.baianat.com.espresso1.sneak
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import androidx.test.uiautomator.UiDevice


@RunWith(AndroidJUnit4::class)

class NavigationTests {

    @Rule
    @JvmField
    val rule = IntentsTestRule(MainActivity::class.java)



    private lateinit var act: NativeActivity

    @Before
    fun prep() {
        rule.activity.runOnUiThread {
            act = NativeActivity()
        }
    }

    @Test
    fun test_validation_consistency() {

        val intent = Intent(rule.activity, NativeActivity::class.java)
        rule.activity.apply {
            startActivity(intent)
        }

        viewOfId(R.id.emailEditText).perform(
            ViewActions.scrollTo(), typeText("zzzzzzzz"),
            closeSoftKeyboard()
        )
        viewOfId(R.id.phoneEditText).perform(typeText("6666"))

        viewOfId(R.id.emailEditText).check(matches(withThisError("email")))


        val intent2 = Intent(rule.activity, act::class.java)

        rule.activity.apply {
            startActivity(intent2)
        }

        viewOfId(R.id.emailEditText).perform(
            ViewActions.scrollTo(), typeText("Oma"),
            closeSoftKeyboard()
        )


        viewOfId(R.id.emailEditText).check(matches(withThisError("email")))

        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        mDevice.pressBack()


        viewOfId(R.id.emailEditText).perform(
            ViewActions.scrollTo(), typeText("O"), clearText(), typeText("o@odsdsd.d"),
            closeSoftKeyboard()
        )

        viewOfId(R.id.emailEditText).check(matches((hasNoError())))

    }

}