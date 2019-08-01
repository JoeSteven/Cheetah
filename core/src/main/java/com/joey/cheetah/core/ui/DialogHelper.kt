package com.joey.cheetah.core.ui

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * Description:
 * author:Joey
 * date:2018/11/21
 */
object DialogHelper {

    interface DialogGenerator{
        fun show(
                context: Context,
                title: String,
                content: String,
                negativeButtonName: String?,
                positiveButtonName: String,
                confirm: (dialog: DialogInterface) -> Boolean ,
                cancel: () -> Boolean
        )
    }

    var defaultDialogGenerator: DialogGenerator = object :DialogGenerator{
        override fun show(context: Context, title: String, content: String, negativeButtonName: String?, positiveButtonName: String, confirm: (dialog: DialogInterface) -> Boolean, cancel: () -> Boolean) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(content)
            builder.setPositiveButton(positiveButtonName) { dialog, _ ->
                if (confirm(dialog)) {
                    dialog.dismiss()
                }
            }

            if (negativeButtonName != null) {
                builder.setNegativeButton(negativeButtonName) { dialog, _ -> if (cancel()) dialog.dismiss() }
            }
            builder.setCancelable(negativeButtonName != null)
            builder.show()
        }
    }

    fun confirmDialog(
        context: Context,
        title: String,
        content: String,
        confirm: (dialog: DialogInterface) -> Boolean = { true },
        cancel: () -> Boolean = { true }
    ) {
        commonDialog(context, title, content, confirm = confirm, cancel =  cancel)
    }

    fun notifyDialog(
        context: Context,
        title: String,
        content: String,
        confirm: (dialog: DialogInterface) -> Boolean = { true }
    ) {
        commonDialog(context, title, content, null, confirm = confirm)
    }

    fun commonDialog(
        context: Context,
        title: String,
        content: String,
        negativeButtonName: String? = "取消",
        positiveButtonName: String = "确定",
        confirm: (dialog: DialogInterface) -> Boolean = { true },
        cancel: () -> Boolean = { true }
    ) {
        defaultDialogGenerator.show(context, title, content, negativeButtonName, positiveButtonName, confirm, cancel)
    }
}