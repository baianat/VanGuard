package app.baianat.com.vanguard.views.editables.viewDecorator

import android.text.TextWatcher
import android.view.View

interface ViewDecorator {

    fun getText(): String

    fun isStatic(): Boolean
    fun invalidateStaticField(errorMsg: String)

    fun setHelper(helperMsg: String)
    fun clearHelper()


    fun setError(errorMsg: String)
    fun clearError()

    fun showServerLoader()
    fun hideServerLoader()

    fun attachListeners(watcher: TextWatcher, focus: View.OnFocusChangeListener)
    fun detachListeners(watcher: TextWatcher)

    var hasTextInitially: Boolean
    var hasFocus:Boolean

    var identifier:String

}