package vanson.dev.instagramclone.controllers.profilesettings

import androidx.lifecycle.ViewModel
import vanson.dev.instagramclone.utilites.AuthManager

class ProfileSettingViewModel(private val authManger: AuthManager): ViewModel(), AuthManager by authManger

