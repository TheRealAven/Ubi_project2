package com.example.aven.projekt2

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.URL
import android.R.attr.entries



/**
 * Created by Aven on 2018-05-14.
 */
class XMLParser {
    private val ns: String?=null

    fun parseFromLink(url: String): ArrayList<Block>{
        var effectList: ArrayList<Block> = ArrayList()
        val parser: XmlPullParser = Xml.newPullParser()
        val inputS: InputStream = URL(url).openStream()

        try{
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputS, null)
            parser.nextTag()

            parser.require(XmlPullParser.START_TAG, ns, "INVENTORY")
            while (parser.next() !== XmlPullParser.END_TAG) {
                if (parser.eventType !== XmlPullParser.START_TAG) {
                    continue
                }
                val name = parser.name
                // Starts by looking for the entry tag
                if (name == "ITEM") {
                    effectList.add(readItem(parser))
                } else {
                    skip(parser)
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            inputS.close()
        }

        return effectList
    }

    fun readItem(parser: XmlPullParser): Block{
        parser.require(XmlPullParser.START_TAG, ns, "ITEM")

        var itemType: String = ""
        var itemId: String = ""
        var itemColor: Int = 0
        var itemNum: Int = 0

        while (parser.next() !== XmlPullParser.END_TAG) {
            if (parser.eventType !== XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name

            when(name){
                "ITEMTYPE" -> {
                    parser.require(XmlPullParser.START_TAG, ns, "ITEMTYPE")
                    if(parser.next()==XmlPullParser.TEXT){
                        itemType=parser.text
                        parser.nextTag()
                    }
                    parser.require(XmlPullParser.END_TAG, ns, "ITEMTYPE")
                }
                "ITEMID" -> {
                    parser.require(XmlPullParser.START_TAG, ns, "ITEMID")
                    if(parser.next()==XmlPullParser.TEXT){
                        itemId=parser.text
                        parser.nextTag()
                    }
                    parser.require(XmlPullParser.END_TAG, ns, "ITEMID")
                }
                "QTY" -> {
                    parser.require(XmlPullParser.START_TAG, ns, "QTY")
                    if(parser.next()==XmlPullParser.TEXT){
                        itemNum=parser.text.toInt()
                        parser.nextTag()
                    }
                    parser.require(XmlPullParser.END_TAG, ns, "QTY")
                }
                "COLOR" -> {
                    parser.require(XmlPullParser.START_TAG, ns, "COLOR")
                    if(parser.next()==XmlPullParser.TEXT){
                        itemColor=parser.text.toInt()
                        parser.nextTag()
                    }
                    parser.require(XmlPullParser.END_TAG, ns, "COLOR")
                }
                else -> skip(parser)
            }
        }

        return Block("","", itemColor, itemId, itemType, 0, itemNum)
    }

    fun skip(parser: XmlPullParser) {
        var depth: Int = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }

        }
    }
}