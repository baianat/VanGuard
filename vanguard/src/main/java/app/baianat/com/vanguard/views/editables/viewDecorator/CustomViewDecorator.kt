package app.baianat.com.vanguard.views.editables.viewDecorator

import android.text.TextWatcher
import android.view.View
import app.baianat.com.vanguard.views.GuardView
import app.baianat.com.vanguard.utils.print
import kotlinx.android.synthetic.main.guard_view.view.*

class CustomViewDecorator(var view: GuardView, var animateWhilePending: Boolean = true) :
    ViewDecorator {
    override var identifier: String
        get() = view.tag as String
        set(value) {}

    override fun isStatic(): Boolean {
        return !view.isEditable
    }

    private var init = false
    override var hasTextInitially: Boolean
        get() = init
        set(value) {
            init = value
        }

    override var hasFocus: Boolean
        get() = view.editText.hasFocus()
        set(value) {}

    override fun invalidateStaticField(errorMsg: String) {
        if (!isStatic()) {
            return
        }

        persistErrorColorWhenDeFocused()
        setError(errorMsg)

    }


    override fun setHelper(helperMsg: String) {
        view.setHelperMsg(helperMsg)
    }

    override fun clearHelper() {
        view.clearHelperMsg()
    }

    override fun attachListeners(watcher: TextWatcher, focus: View.OnFocusChangeListener) {
        view.startWatching(watcher, focus)
    }

    override fun detachListeners(watcher: TextWatcher) {
        view.stopWatching(watcher)
    }


    override fun setError(errorMsg: String) {
        view.setErrorMsg(errorMsg)
        view.showErrorIcon()
    }

    override fun clearError() {
        view.clearErrorMsg()
        view.hideErrorIcon()
        view.restoreStrokeColor()
    }

    override fun showServerLoader() {
        if (animateWhilePending) {
            view.showLoader()
        }
    }

    override fun hideServerLoader() {
        view.hideLoader()
    }

    override fun getText(): String {
        return view.text
    }

    fun hideValidationIcon() {
        view.hideValidIcon()
    }

    fun confirmValidationWithIcon() {
        view.showValidIcon()
    }

    fun persistErrorColorWhenFocused() {
        "persistErrorColorFocused for ${view.tag}".print()

        view.refreshChanges()
        view.keepErrorState()
    }

    fun persistErrorColorWhenDeFocused() {
        "persistErrorColorWhenDeFocused for ${view.tag}".print()

        view.changeStrokeColorToError()
//        view.unfocusedLoader()

    }

    fun serverFailedValidation(){
        view.unfocusedLoader()
    }

}