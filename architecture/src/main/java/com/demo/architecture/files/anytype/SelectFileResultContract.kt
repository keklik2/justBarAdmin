package com.demo.architecture.files.anytype

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class SelectFileResultContract : ActivityResultContract<Unit?, List<Uri?>?>() {

    override fun createIntent(context: Context, data: Unit?): Intent =
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            setTypeAndNormalize("text/plain")
        }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri?>? =
        if (resultCode == Activity.RESULT_OK && intent != null) mutableListOf(intent.data)
        else null
}
