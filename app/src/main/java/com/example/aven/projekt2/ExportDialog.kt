package com.example.aven.projekt2

import android.app.DialogFragment
import android.content.Context
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.dialog_layout.*

/**
 * Created by Aven on 2018-05-29.
 */
class ExportDialog: DialogFragment() {
    val TAG: String = "DialogExportFragment"

    var adapter: MiniBlockListAdapter? = null

    interface OnExportDialogInputListener{
        fun onInput(nameInput: String)
    }
    var listener: OnExportDialogInputListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.dialog_export_layout, container, false)

        adapter = MiniBlockListAdapter(DatabaseManager(view.context).getAllBlocks(), view.context)

        view.findViewById<Button>(R.id.buttonNo).setOnClickListener {
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.buttonYes).setOnClickListener {
            var name = nameEdit.text.toString()
            listener?.onInput(name)

            dialog.dismiss()
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            listener = activity as OnExportDialogInputListener
        }catch (e: ClassCastException){
            e.printStackTrace()
        }
    }
}