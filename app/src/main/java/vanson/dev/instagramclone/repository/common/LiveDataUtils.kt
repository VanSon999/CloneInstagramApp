package vanson.dev.instagramclone.repository.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DatabaseReference

fun <A, B> LiveData<A>.mapCustom(f: (A) -> B): LiveData<B> = Transformations.map(this, f)

fun DatabaseReference.liveData() = FirebaseLiveData(this)