package com.izettle.wrench.oss

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.Html

class OssDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
                .setMessage(Html.fromHtml("<a href='http://www.google.com'>http://www.google.com</>"))
                .setPositiveButton("dismiss") { _, _ ->
                    dismiss()
                }
                .create()
    }
}