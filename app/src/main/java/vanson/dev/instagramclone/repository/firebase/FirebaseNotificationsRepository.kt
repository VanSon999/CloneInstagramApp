package vanson.dev.instagramclone.repository.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import vanson.dev.instagramclone.models.Notification
import vanson.dev.instagramclone.repository.NotificationsRepository
import vanson.dev.instagramclone.repository.common.liveData
import vanson.dev.instagramclone.repository.common.mapCustom
import vanson.dev.instagramclone.repository.firebase.common.database
import vanson.dev.instagramclone.utilites.toUnit

class FirebaseNotificationsRepository : NotificationsRepository {
    override fun createNotifications(uid: String, notification: Notification): Task<Unit> =
        notificationsRef(uid).push().setValue(notification).toUnit()


    override fun getNotifications(uid: String): LiveData<List<Notification>> =
        notificationsRef(uid).liveData().mapCustom { dataSnapshot ->
            dataSnapshot.children.map{it.asNotification()!!}
        }

    override fun setNotificationsRead(uid: String, ids: List<String>, read: Boolean): Task<Unit> {
        val updatesMap = ids.map { "$it/read" to read }.toMap()
        return notificationsRef(uid).updateChildren(updatesMap).toUnit()
    }

    private fun notificationsRef(uid: String) =
        database.child("Notifications").child(uid)

    private fun DataSnapshot.asNotification() =
        getValue(Notification::class.java)?.copy(id = key.toString())
}