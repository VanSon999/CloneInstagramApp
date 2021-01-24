package vanson.dev.instagramclone.repository.common

import androidx.lifecycle.*
import com.google.firebase.database.DatabaseReference

fun <A, B> LiveData<A>.mapCustom(f: (A) -> B): LiveData<B> = Transformations.map(this, f)

fun DatabaseReference.liveData() = FirebaseLiveData(this)

fun <T> LiveData<T>.observeFirstNotNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            value?.let {
                observer(value!!)
                removeObserver(this)
            }
        }
    })
}


fun <A, B> zipLiveData(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

/**
 * This is merely an extension function for [zipLiveData].
 *
 * @see zipLiveData
 * @author Mitchell Skaggs
 */
fun <A, B> LiveData<A>.zip(b: LiveData<B>): LiveData<Pair<A, B>> = zipLiveData(this, b)
