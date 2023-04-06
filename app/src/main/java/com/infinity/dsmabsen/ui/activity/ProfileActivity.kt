package com.infinity.dsmabsen.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    override fun getViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

    }

}