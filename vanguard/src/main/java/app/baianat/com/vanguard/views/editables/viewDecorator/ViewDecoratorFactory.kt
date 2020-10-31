package app.baianat.com.vanguard.views.editables.viewDecorator

import android.view.View
import android.widget.TextView
import app.baianat.com.vanguard.views.GuardView
import app.baianat.com.vanguard.views.editables.TextCaseArgs

class ViewDecoratorFactory {

    companion object {
        fun create(textCaseArgs: TextCaseArgs<View>): ViewDecorator {

            return when (val view = textCaseArgs.caseArgs.viewToWatch) {
                is TextView -> NativeViewDecorator(view, textCaseArgs.animateOnPending)
                else -> CustomViewDecorator(view as GuardView, textCaseArgs.animateOnPending)
            }
        }

    }
}