package com.example.aven.projekt2

import android.content.Context
import android.databinding.ObservableArrayList

/**
 * Created by Aven on 2018-05-21.
 */
class Project(n: String, id: Int) {
    var listOfNeededBlocks: ObservableArrayList<Block> = ObservableArrayList()
    val name: String = n
    val projectId: Int = id

    fun addBlock(b: Block){
        listOfNeededBlocks.add(b)
    }
}