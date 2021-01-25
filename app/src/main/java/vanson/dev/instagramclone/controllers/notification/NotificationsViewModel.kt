package vanson.dev.instagramclone.controllers.notification

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.models.Notification
import vanson.dev.instagramclone.repository.NotificationsRepository

class NotificationsViewModel(
    private val notificationsRepo: NotificationsRepository,
    onFailureListener: OnFailureListener
) : BaseViewModel(onFailureListener) {

    lateinit var notifications: LiveData<List<Notification>>
    private lateinit var uid: String
    fun init(uid: String) {
        if(!this::uid.isInitialized){
            this.uid = uid
            notifications = notificationsRepo.getNotifications(uid)
        }
    }

    fun setNotificationsRead(notifications: List<Notification>) {
        val ids = notifications.filter { !it.read }.map { it.id }
        if(ids.isNotEmpty()){
            notificationsRepo.setNotificationsRead(uid, ids, true)
                .addOnFailureListener(onFailureListener)
        }
    }
}
