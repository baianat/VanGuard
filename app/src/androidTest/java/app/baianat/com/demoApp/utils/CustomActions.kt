package app.baianat.com.demoApp.utils

import android.view.View
import android.widget.*
import androidx.test.espresso.UiController
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.ViewActions.actionWithAssertions


fun setProgress(progress: Int): ViewAction {

    return object : ViewAction {
        override fun perform(uiController: UiController, view: View) {
            val seekBar = view as? SeekBar
            seekBar?.progress = progress
        }

        override fun getDescription(): String {
            return "Set a progress on a SeekBar"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(SeekBar::class.java)
        }
    }

}

fun setSelection(postion: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Set position of a spinner"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(Spinner::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? Spinner)?.apply {
                setSelection(postion)
            }
        }

    }
}

fun check(): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Check a Compound Button"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(CompoundButton::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? CompoundButton)?.apply {
                if (!isChecked) {
                    this.performClick()
                }
            }
        }

    }
}

fun unCheck(): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Uncheck a Compound Button"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(CompoundButton::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? CompoundButton)?.apply {
                if (isChecked) {
                    this.performClick()
                }
            }
        }

    }
}


fun setSelectedPosition(position: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Set selected position of a spinner"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(Spinner::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? Spinner)?.apply {
                setSelection(position)
            }
        }

    }
}


fun setText(text: String): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Set text on a text field"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(EditText::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? EditText)?.apply {
                setText(text)
            }
        }

    }
}
