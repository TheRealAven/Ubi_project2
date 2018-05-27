package com.example.aven.projekt2

import android.graphics.Bitmap

/**
 * Created by Aven on 2018-05-14.
 */
class Block {
    var maxNumber: Int
    var actualNumber: Int
    val blockIdCode: String
    val colorCode: Int
    val colorName: String
    val name: String
    val typeName: String
    val blockTypeCode: String
    val imgCode: String

    var stateString: String
    var done: Boolean
    var bitmap: Bitmap? = null


    constructor(name: String, typeName: String, colorCode: Int, blockIdCode: String, blockTypeCode: String, actualNumber: Int, maxNumber: Int, colorName: String, imgCode: String) {
        this.blockIdCode = blockIdCode
        this.actualNumber = actualNumber
        this.maxNumber = maxNumber
        this.colorCode = colorCode
        this.colorName = colorName
        this.name = name
        this.typeName = typeName
        this.blockTypeCode = blockTypeCode
        this.stateString = "$actualNumber / $maxNumber"
        this.done = actualNumber==maxNumber
        this.imgCode = imgCode
    }

    fun addToCurrent(){
        if(actualNumber<maxNumber) {
            this.actualNumber++
            this.stateString = "$actualNumber / $maxNumber"
            this.done = actualNumber==maxNumber
        }
    }

    fun subFromCurrent(){
        if(actualNumber>0) {
            this.actualNumber--
            this.stateString = "$actualNumber / $maxNumber"
            this.done = actualNumber==maxNumber
        }
    }
}