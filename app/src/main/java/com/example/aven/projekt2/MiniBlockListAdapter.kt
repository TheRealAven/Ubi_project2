package com.example.aven.projekt2

import android.databinding.adapters.TextViewBindingAdapter.setText
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


/**
 * Created by Aven on 2018-05-21.
 */
class MiniBlockListAdapter: ArrayAdapter<MiniBlock> {

    private val dataSet: ArrayList<MiniBlock>
    var mContext: Context

    // View lookup cache
    private class ViewHolder {
        internal var txtName: TextView? = null
        internal var txtId: TextView? = null
    }

    constructor(data: ArrayList<MiniBlock>, context: Context):super(context, R.layout.miniblock_layout, data) {
        this.dataSet = data
        this.mContext = context
    }

    private var lastPosition = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        val dataModel = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        val viewHolder: ViewHolder // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.miniblock_layout, parent, false)
            viewHolder.txtName = convertView!!.findViewById(R.id.miniblockName)
            viewHolder.txtId = convertView!!.findViewById(R.id.miniblockId)
            convertView!!.setTag(viewHolder)
        } else {
            viewHolder = convertView!!.tag as ViewHolder
        }

        lastPosition = position

        viewHolder.txtName!!.setText("Nazwa: ${dataModel!!.nazwa}")
        viewHolder.txtId!!.setText("Nazwa: ${dataModel!!.id}")


        // Return the completed view to render on screen
        return convertView
    }

}