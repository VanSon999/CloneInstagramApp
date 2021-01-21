package vanson.dev.instagramclone.utilites

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks

fun Task<Void>.toUnit(): Task<Unit> = onSuccessTask { Tasks.forResult(Unit) }

fun <T> task(block: (TaskCompletionSource<T>) -> Unit): Task<T> { //this is it when addOnCompleteListener!!! -> use to save result or exception of task
    val taskSource = TaskCompletionSource<T>()
    block(taskSource) // implement task
    return taskSource.task // this is equal mFirebase.database.child("Feed_Posts").child(mUser.uid!!).updateChildren(postsMap).addOnCompleteListener ...
}