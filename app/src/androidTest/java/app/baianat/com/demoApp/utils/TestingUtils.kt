package app.baianat.com.espresso1

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNot.not

fun viewOfId(@IdRes id: Int) = Espresso.onView(ViewMatchers.withId(id))
fun <T : View> trueViewOfId(rule: ActivityTestRule<*>, @IdRes id: Int) =
    rule.activity.findViewById<T>(id)


fun viewOfText(@StringRes stringId: Int) = Espresso.onView(ViewMatchers.withText(stringId))
fun viewOfText(text: String) = Espresso.onView(ViewMatchers.withText(text))


fun checkToastIsVisible(toastMessage: String, activity: AppCompatActivity) =
    Espresso.onView(ViewMatchers.withText(toastMessage))
        .inRoot(
            // to ensure the view is not one of the window decor view
            RootMatchers.withDecorView(CoreMatchers.not(activity.window.decorView))
        )
        .check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )


fun checkToastIsVisible(@StringRes toastMessageID: Int, activity: AppCompatActivity) =
    checkToastIsVisible(activity.getString(toastMessageID), activity)

fun clickButton(@IdRes id: Int) =
    Espresso.onView(ViewMatchers.withId(id)).perform(scrollTo(), click())

fun checkViewIsVisible(@IdRes id: Int) =
    Espresso.onView(ViewMatchers.withId(id)).check(matches(ViewMatchers.isDisplayed()))

fun checkViewIsNotVisible(@IdRes id: Int) =
    Espresso.onView(ViewMatchers.withId(id)).check(matches(not(ViewMatchers.isDisplayed())))


fun checkViewIsVisible(text: String) =
    Espresso.onView(ViewMatchers.withText(text)).check(matches(ViewMatchers.isDisplayed()))

fun checkViewNotExist(@IdRes id: Int) =
    Espresso.onView(ViewMatchers.withId(id)).check(doesNotExist())

fun subViewOfId(@IdRes id: Int, @IdRes parentId: Int) = Espresso.onView(
    CoreMatchers.allOf(
        ViewMatchers.withId(id),
        ViewMatchers.isDescendantOfA(ViewMatchers.withId(parentId))
    )
)

fun sneak(times: Int = 2) {
    Thread.sleep(times.toLong() * 1000)
}