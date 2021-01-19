package vanson.dev.instagramclone.controllers.profilesettings

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile_settings.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard

class ProfileSettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        setupAuthGuard {
            val viewModel = initViewModel<ProfileSettingViewModel>()
            sign_out_text.setOnClickListener { viewModel.signOut()}
            back_image.setOnClickListener { finish() }
        }
    }
}
