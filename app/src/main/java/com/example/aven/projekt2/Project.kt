package com.example.aven.projekt2

import android.content.Context
import android.databinding.ObservableArrayList

/**
 * Created by Aven on 2018-05-21.
 */
class Project(n: String, id: Int, active: Int) {
    var listOfNeededBlocks: ObservableArrayList<Block> = ObservableArrayList()
    val name: String = n
    val projectId: Int = id
    var active: Boolean = active>0

    fun makeProjectFromLink(url: String, c: Context){
        listOfNeededBlocks= XMLParser(c).parseFromLink(url) as ObservableArrayList<Block>
    }

    fun refreshProjectStatus(){
        var howSIt = false
        for(block in listOfNeededBlocks){
            if(block.actualNumber<block.maxNumber)
                howSIt = true
        }
        active = howSIt
    }

    fun endProject(){
        active = false
    }

    fun restartProject(){
        active = true
    }

    fun getState(): String{
        return if (active) "Active" else "Inactive"
    }
}