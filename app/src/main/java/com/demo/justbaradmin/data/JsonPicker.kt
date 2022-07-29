package com.demo.justbaradmin.data

import android.Manifest
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import com.demo.architecture.BaseFragment
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.files.FileSelectionEntryPoint
import com.demo.architecture.files.anytype.SelectFileResultContract
import com.demo.justbaradmin.R
import com.demo.justbaradmin.domain.Cocktail
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.aartikov.sesame.property.PropertyHost
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class JsonPicker @Inject constructor(
    private val fragment: BaseFragment,
    private val onLoadFinished: (List<Cocktail>) -> Unit
) : FileSelectionEntryPoint, PropertyHost {
    override val propertyHostScope = CoroutineScope(Dispatchers.IO)
    private var json: String? = null

    fun load() {
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            filePicker.launch(null)
        } else readStoragePermission.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    private val filePicker =
        fragment.registerForActivityResult(SelectFileResultContract()) {
            onFileSelected(it)
        }

    private val readStoragePermission =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            val anyNotGranted = granted.values.contains(false)
            if (!anyNotGranted) filePicker.launch(null)
            else {
                fragment.makeAlert(
                    AppDialogContainer(
                        fragment.getString(R.string.dialog_permission_title),
                        fragment.getString(R.string.dialog_permission_denied),
                        positiveBtnCallback = { }
                    )
                )
            }
        }

    override fun onFileSelected(uri: List<Uri?>?) {
        if (!uri.isNullOrEmpty()) {
            uri[0].let { itUri ->
                json = readTextFile(itUri!!)
                json?.let { itJson ->
                    fragment.makeToast(fragment.getString(TOAST_COMPLETED_LOAD))
                    onLoadFinished(getListFromJson())
                }
            }
        }
    }

    private fun getListFromJson(): List<Cocktail> {
        return mutableListOf<Cocktail>().apply {
            val jsonObj = JSONObject(json!!)
            for (key in jsonObj.keys()) {
                add(Gson().fromJson(jsonObj.getJSONObject(key).toString(), Cocktail::class.java))
            }
        }
    }

    private fun readTextFile(uri: Uri): String? {
        var reader: BufferedReader? = null
        val builder = StringBuilder()
        try {
            reader = BufferedReader(
                InputStreamReader(
                    fragment.requireActivity().contentResolver.openInputStream(uri)
                )
            )
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
        } catch (e: Exception) {
            fragment.makeToast(fragment.getString(TOAST_ERROR_LOAD))
        } finally {
            reader?.let {
                try {
                    it.close()
                } catch (e: Exception) {
                }
            }
        }
        return if (builder.toString().isBlank() || builder.toString().isEmpty()) null
        else builder.toString()
    }

    private fun isPermissionGranted(permission: String): Boolean {
        fragment.context?.let {
            return PermissionChecker.checkSelfPermission(
                it,
                permission
            ) == PermissionChecker.PERMISSION_GRANTED
        }
        return false
    }

    companion object {
        private const val TOAST_ERROR_LOAD = R.string.toast_json_error
        private const val TOAST_COMPLETED_LOAD = R.string.toast_json_success
    }
}
