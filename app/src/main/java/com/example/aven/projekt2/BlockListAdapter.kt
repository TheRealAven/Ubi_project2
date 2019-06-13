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
class BlockListAdapter: ArrayAdapter<Block> {

    private val dataSet: ArrayList<Block>
    var mContext: Context

    // View lookup cache
    private class ViewHolder {
        internal var txtName: TextView? = null
        internal var txtOpis: TextView? = null
        internal var txtPlevel: TextView? = null
        internal var txtTra: TextView? = null
        internal var txtIng: TextView? = null
        internal var txtCTime: TextView? = null
        internal var txtDTime: TextView? = null
    }

    constructor(data: ArrayList<Block>, context: Context):super(context, R.layout.factory_block_layout, data) {
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

        var sharedPref = mContext.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE)
        var sort: Boolean = true
        sharedPref.getBoolean("pref_block_done", sort)

        if(sort){
            dataSet.sortWith(compareBy { it.nazwa })
        }


        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.factory_block_layout, parent, false)
            viewHolder.txtName = convertView!!.findViewById(R.id.blockName)
            viewHolder.txtCTime = convertView!!.findViewById(R.id.ctime)
            viewHolder.txtDTime = convertView!!.findViewById(R.id.dtime)
            viewHolder.txtPlevel = convertView!!.findViewById(R.id.plevel)
            viewHolder.txtIng = convertView!!.findViewById(R.id.ing)
            viewHolder.txtOpis = convertView!!.findViewById(R.id.desc)
            viewHolder.txtTra = convertView!!.findViewById(R.id.tra)
            convertView!!.setTag(viewHolder)
        } else {
            viewHolder = convertView!!.tag as ViewHolder
        }

        lastPosition = position

        viewHolder.txtName!!.setText("Nazwa: ${dataModel!!.nazwa}")
        viewHolder.txtCTime!!.setText("Czas rzucania: ${dataModel!!.ctime}")
        viewHolder.txtDTime!!.setText("Czas trwania: ${dataModel!!.dtime}")
        viewHolder.txtOpis!!.setText("Opis: ${dataModel!!.opis}")
        viewHolder.txtTra!!.setText("Tradycja: ${dataModel!!.tradycja}")
        viewHolder.txtIng!!.setText("Składniki czaru: ${dataModel!!.koszt}")
        viewHolder.txtPlevel!!.setText("Poziom mocy: ${dataModel!!.plevel}")


        // Return the completed view to render on screen
        return convertView
    }

}