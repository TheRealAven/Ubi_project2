package com.example.aven.projekt2

import android.content.Context
import android.databinding.ObservableArrayList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * Created by Aven on 2018-05-26.
 */
class ProjectListAdapter: ArrayAdapter<Project> {

    private val dataSet: ObservableArrayList<Project>
    var mContext: Context

    // View lookup cache
    private class ViewHolder {
        internal var txtName: TextView? = null
    }

    constructor(data: ObservableArrayList<Project>, context: Context):super(context, R.layout.project_element_layout, data) {
        this.dataSet = data
        this.mContext = context
        var sharedPref = mContext.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE)
        var sort: Boolean = true
        sharedPref.getBoolean("pref_active", sort)
        if(sort){
            dataSet.sortWith(compareBy { it.name })
        }
    }

    private var lastPosition = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        val dataModel = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        val viewHolder: ViewHolder // view lookup cache stored in tag


        var sharedPref = mContext.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE)
        var sort: Boolean = true
        sharedPref.getBoolean("pref_active", sort)

        if(sort){
            dataSet.sortWith(compareBy { it.name })
        }

        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.project_element_layout, parent, false)
            viewHolder.txtName = convertView!!.findViewById(R.id.nameEdit)

            convertView!!.setTag(viewHolder)
        } else {
            viewHolder = convertView!!.tag as ViewHolder
        }

        viewHolder.txtName!!.setText(dataModel.name)

        lastPosition = position

        // Return the completed view to render on screen
        return convertView
    }

}