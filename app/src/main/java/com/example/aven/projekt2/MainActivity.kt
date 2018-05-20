package com.example.aven.projekt2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.os.StrictMode



class MainActivity : AppCompatActivity() {
    var testList: ArrayList<Block> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        testList=XMLParser().parseFromLink("http://fcds.cs.put.poznan.pl/MyWeb/BL/615.xml")
        if(testList[0]!=null) {
            var testLay: FactoryBlock = FactoryBlock(this, null, testList[0])
            listView.addView(testLay)
        }else{
            print("AAAARGH")
        }
    }
}
