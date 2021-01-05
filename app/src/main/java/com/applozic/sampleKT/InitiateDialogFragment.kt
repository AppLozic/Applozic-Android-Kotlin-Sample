package com.applozic.sampleKT

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity

class InitiateDialogFragment : DialogFragment(),
    DialogInterface.OnClickListener {
    private var inputEditText: EditText? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        inputEditText = EditText(activity)
        return AlertDialog.Builder(activity!!).setTitle(R.string.initiate_chat_info)
            .setMessage(R.string.enter_user_id_info)
            .setPositiveButton(R.string.start, this).setNegativeButton(R.string.cancel, null)
            .setView(inputEditText).create()
    }

    override fun onClick(dialog: DialogInterface, position: Int) {
        when (position) {
            -1 -> {
                val editTextValue = inputEditText!!.text.toString()
                if (TextUtils.isEmpty(editTextValue)) {
                    Toast.makeText(activity, R.string.empty_user_id_info, Toast.LENGTH_SHORT).show()
                    return
                }
                dialog.dismiss()
                val intent = Intent(activity, ConversationActivity::class.java)
                intent.putExtra(ConversationUIService.USER_ID, editTextValue)
                intent.putExtra(ConversationUIService.TAKE_ORDER, true)
                startActivity(intent)
            }
            -2 -> dialog.dismiss()
        }
    }
}