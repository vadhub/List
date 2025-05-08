package com.vlg.list.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.vlg.list.R

class SaveGroupDialog(
    private val save: (newName: String) -> Unit,
    private val existingName: String = "",
    private val title: String = "Save Group"
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_save_group, null)
        val nameText: EditText = view.findViewById(R.id.nameGroupEditText)
        nameText.setText(existingName)

        val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            .setView(view)
            .setTitle(title)
            .setPositiveButton("Save") { dialog, _ ->
                save.invoke(nameText.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }
}