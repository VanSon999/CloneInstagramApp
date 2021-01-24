package vanson.dev.instagramclone.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import vanson.dev.instagramclone.models.Notification
import vanson.dev.instagramclone.repository.common.liveData
import vanson.dev.instagramclone.repository.common.mapCustom
import vanson.dev.instagramclone.repository.firebase.common.database
import vanson.dev.instagramclone.utilites.toUnit

interface NotificationsRepository {
    fun createNotifications(uid: String, notification: Notification): Task<Unit>

    fun getNotifications(uid: String): LiveData<List<Notification>>

    fun setNotificationsRead(uid: String, ids: List<String>, read: Boolean): Task<Unit>
}