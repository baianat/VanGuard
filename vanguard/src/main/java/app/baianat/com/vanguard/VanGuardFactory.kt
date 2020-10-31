package app.baianat.com.vanguard

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

internal class VanGuardFactory {

    companion object {
        fun create(
            lifecycleOwner: LifecycleOwner,
            tag: String,
            validatorManager: MutableMap<String, VanGuard>
        ): VanGuard? {
//            return if (validatorManager.containsKey(tag) && validatorManager[tag] != null) {
//                validatorManager[tag]!!.apply {
//                    lifecycleOwner.lifecycle.also {
//                        it.removeObserver(this)
//                        it.addObserver(this)
//                    }
//                }
//            } else {

                when (lifecycleOwner) {
                    is Fragment -> {
                        return VanGuard((lifecycleOwner).activity as Activity).also {
                            lifecycleOwner.lifecycle.addObserver(it)
                            validatorManager[tag] = it

                        }
                    }
                    is Activity -> {
                        return VanGuard((lifecycleOwner) as Activity).also {
                            lifecycleOwner.lifecycle.addObserver(it)
                            validatorManager[tag] = it

                        }
                    }
                    else -> {
                        return null
                    }
                }

//            }
        }

    }


}