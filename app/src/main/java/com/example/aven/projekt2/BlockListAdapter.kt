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
        internal var txtState: TextView? = null
        internal var status: ProgressBar? = null
        internal var addB: Button? = null
        internal var subB: Button? = null
        internal var imageV: ImageView? = null
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
            dataSet.sortWith(compareBy { it.done })
        }


        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.factory_block_layout, parent, false)
            viewHolder.txtName = convertView!!.findViewById(R.id.blockName)
            viewHolder.txtState = convertView!!.findViewById(R.id.numberOfBlocks)
            viewHolder.addB = convertView!!.findViewById(R.id.addNumber)
            viewHolder.subB = convertView!!.findViewById(R.id.subNumber)
            viewHolder.status = convertView!!.findViewById(R.id.progressBar)
            viewHolder.imageV = convertView!!.findViewById(R.id.blockImageView)
            convertView!!.setTag(viewHolder)
        } else {
            viewHolder = convertView!!.tag as ViewHolder
        }

        lastPosition = position

        viewHolder.txtName!!.setText(dataModel!!.name)
        viewHolder.txtState!!.setText(dataModel!!.stateString)
        viewHolder.status!!.max = dataModel!!.maxNumber
        viewHolder.status!!.progress = dataModel!!.actualNumber
        var urlString = "https://www.lego.com/service/bricks/5/2/${dataModel!!.imgCode}"
        if(dataModel!!.bitmap==null) {
            var imageLoader = ImageAsyncDownloader(viewHolder.imageV!!, dataModel!!)
            imageLoader.execute(urlString)
        }else{
            viewHolder.imageV!!.setImageBitmap(dataModel!!.bitmap)
        }

        viewHolder.addB!!.setOnClickListener {
            dataModel!!.addToCurrent()
            viewHolder.status!!.max = dataModel!!.maxNumber
            viewHolder.status!!.progress = dataModel!!.actualNumber
            viewHolder.txtState!!.setText(dataModel!!.stateString)
        }
        viewHolder.subB!!.setOnClickListener {
            dataModel!!.subFromCurrent()
            viewHolder.status!!.max = dataModel!!.maxNumber
            viewHolder.status!!.progress = dataModel!!.actualNumber
            viewHolder.txtState!!.setText(dataModel!!.stateString)
        }
        // Return the completed view to render on screen
        return convertView
    }

}