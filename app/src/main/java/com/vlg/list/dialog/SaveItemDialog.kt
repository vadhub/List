package com.vlg.list.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.vlg.list.R

class SaveItemDialog (private val save: (newName: String, isList: Boolean) -> Unit) : DialogFragment() {

    private var name: String = ""

    fun setName(name: String) {
        this.name = name
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_save_item, null)
        val nameText: EditText = view.findViewById(R.id.nameItemEditText)
        val checkbox: CheckBox = view.findViewById(R.id.isList)
        nameText.setText(name)
        val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
        builder.setView(view).setPositiveButton("save", ({ dialog, which ->
            save.invoke(nameText.text.toString(), checkbox.isChecked)
            dialog.dismiss()
        }))
        return builder.create()
    }
}