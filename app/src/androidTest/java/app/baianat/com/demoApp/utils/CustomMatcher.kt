package app.baianat.com.demoApp.utils

import android.view.View
import android.widget.TextView
import app.baianat.com.vanguard.utils.print
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class CustomMatcher {


    companion object {
        fun withTextSize(textSize: Float): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    description?.appendText("this is my text")
                }

                override fun matchesSafely(item: View?): Boolean {

                    item?.apply {

                        if (item !is TextView) return false

                        "print ${item.text}".print()
                        val pixels = item.textSize
                        val actualSize =
                            pixels / item.getResources().getDisplayMetrics().scaledDensity
                        return java.lang.Float.compare(actualSize, textSize) == 0

                    } ?: apply {
                        return false
                    }
                    return false
                }

            }
        }

        fun withThisError(error: String): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    description?.appendText("View with error -> $error")
                }

                override fun matchesSafely(item: View?): Boolean {

                    item?.apply {

                        when {
                            item is TextInputEditText && item.parent.parent is TextInputLayout -> {
                                (item.parent.parent as? TextInputLayout)?.apply {
                                    return this.error?.toString()?.contains(error,true) == true
                                }
                            }
                            item is TextView -> {
                                return (this as TextView).error?.toString()?.contains(error,true) == true

                            }

                        }


                    } ?: apply {
                        return false
                    }
                    return false
                }

            }
        }


        fun hasNoError(): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    description?.appendText("View with empty error")
                }

                override fun matchesSafely(item: View?): Boolean {

                    item?.apply {

                        when {
                            item is TextInputEditText && item.parent.parent is TextInputLayout -> {
                                (item.parent.parent as? TextInputLayout)?.apply {
                                    return this.error.isNullOrBlank()
                                }
                            }
                            item is TextView -> {
                                return (this as TextView).error.isNullOrBlank()
                            }

                        }


                    } ?: apply {
                        return false
                    }
                    return false
                }

            }
        }


    }

}