package vanson.dev.instagramclone.controllers.profilesettings

import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.utilites.AuthManager

class ProfileSettingViewModel(
    private val authManger: AuthManager,
    onFailureListener: OnFailureListener
) : BaseViewModel(onFailureListener), AuthManager by authManger

