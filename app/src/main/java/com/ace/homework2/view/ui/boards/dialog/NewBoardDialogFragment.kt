package com.ace.homework2.view.ui.boards.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.ace.homework2.R
import com.ace.homework2.model.Category
import com.ace.homework2.view.ui.boards.BoardsFragment.Companion.hashMap
import com.ace.homework2.view.ui.boards.OnDialogResult

class NewBoardDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        val dialogView = activity?.layoutInflater?.inflate(R.layout.dialog_new_board, null)
        val editText = dialogView?.findViewById<EditText>(R.id.etNameBoard)
        val spinner = dialogView?.findViewById<Spinner>(R.id.spinnerCat)
        var categories: MutableList<String> = mutableListOf()
        hashMap.forEach {
            categories.add(it.key.displayName)
        }

        val adapter = ArrayAdapter(
            context!!, android.R.layout.simple_spinner_item, categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter
        builder
            .setView(dialogView).setTitle(getString(R.string.create_board))
            .setPositiveButton(getString(R.string.create_btn)) { _, _ ->
                val selected = spinner.selectedItem.toString()
                var category: Category? = null
                hashMap.forEach {
                    if (it.key.displayName == selected) {
                        category = it.key
                    }
                }
                category?.let {
                    (targetFragment as? OnDialogResult)?.onNewBoardAdded(
                        editText?.text.toString(), it
                    )
                }
            }
            .setNegativeButton(getString(R.string.cancel_btn)) { dialog, _ -> dialog.cancel() }
        return builder.create()
    }

}
