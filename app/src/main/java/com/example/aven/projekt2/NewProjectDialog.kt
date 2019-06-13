package com.example.aven.projekt2

import android.app.DialogFragment
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_layout.*

/**
 * Created by Aven on 2018-05-25.
 */
class NewProjectDialog: DialogFragment() {
    val TAG: String = "DialogFragment"

    interface OnDialogInputListener{
        fun onInput(nameInput: String)
    }
    var listener: OnDialogInputListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.dialog_layout, container, false)

        var sharedPref = this.activity.getSharedPreferences("APP_SETTINGS", MODE_PRIVATE)

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.okButton).setOnClickListener {
            var name = nameEdit.text.toString()
            listener?.onInput(name)

            dialog.dismiss()
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            listener = activity as OnDialogInputListener
        }catch (e: ClassCastException){
            e.printStackTrace()
        }
    }
}