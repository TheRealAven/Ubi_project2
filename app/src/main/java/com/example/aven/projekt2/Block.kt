package com.example.aven.projekt2

/**
 * Created by Aven on 2018-05-14.
 */
class Block {
    var maxNumber: Int
    var actualNumber: Int
    val blockIdCode: String
    val colorCode: Int
    val name: String
    val typeName: String
    val blockTypeCode: String

    var stateString: String
    var stateNumber: Int
    var done: Boolean


    constructor(name: String, typeName: String, colorCode: Int, blockIdCode: String, blockTypeCode: String, actualNumber: Int, maxNumber: Int) {
        this.blockIdCode = blockIdCode
        this.actualNumber = actualNumber
        this.maxNumber = maxNumber
        this.colorCode = colorCode
        this.name = name
        this.typeName = typeName
        this.blockTypeCode = blockTypeCode
        this.stateNumber = actualNumber/maxNumber
        this.stateString = "$actualNumber / $maxNumber"
        this.done = actualNumber==maxNumber
    }

    fun addToCurrent(){
        if(actualNumber<maxNumber) {
            this.actualNumber++
            this.stateNumber = actualNumber / maxNumber
            this.stateString = "$actualNumber / $maxNumber"
            this.done = actualNumber==maxNumber
        }
    }

    fun subFromCurrent(){
        if(actualNumber>0) {
            this.actualNumber--
            this.stateNumber = actualNumber / maxNumber
            this.stateString = "$actualNumber / $maxNumber"
            this.done = actualNumber==maxNumber
        }
    }
}