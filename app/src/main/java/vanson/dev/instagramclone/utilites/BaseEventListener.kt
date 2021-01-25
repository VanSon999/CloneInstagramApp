package vanson.dev.instagramclone.utilites

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

abstract class BaseEventListener : LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState =
            Lifecycle.State.STARTED //specify that: Only work when app at started state
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}