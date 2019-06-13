package com.example.aven.projekt2

import android.graphics.Bitmap

/**
 * Created by Aven on 2018-05-14.
 */
class Block {
    constructor(nazwa: String?, opis: String?, koszt: String?, tradycja: String?, ctime: String?, dtime: String?, plevel: Int?, id: Int?) {
        this.nazwa = nazwa ?: ""
        this.opis = opis ?: ""
        this.koszt = koszt ?: ""
        this.tradycja = tradycja ?: ""
        this.ctime = ctime ?: ""
        this.dtime = dtime ?: ""
        this.plevel = plevel ?: 0
        this.id = id ?: 0
    }

    constructor(){}

    var nazwa: String = ""
    var opis: String = ""
    var koszt: String = ""
    var tradycja: String = ""
    var ctime: String = ""
    var dtime: String = ""
    var plevel: Int = 0
    var id: Int = 0
}