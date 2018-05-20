package com.example.aven.projekt2

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.factory_block_layout.view.*


/**
 * Created by Aven on 2018-05-20.
 */
class FactoryBlock : RelativeLayout {
    var activeBlock: Block

    constructor(context: Context?, attrs: AttributeSet?, givenBlock: Block) : super(context, attrs) {
        this.activeBlock=givenBlock
        progressBar.max=givenBlock.maxNumber
        progressBar.progress=givenBlock.actualNumber
        blockName.text=givenBlock.name
        numberOfBlocks.text=givenBlock.stateString
        addNumber.setOnClickListener {
            activeBlock.addToCurrent()
            progressBar.progress=activeBlock.actualNumber
            numberOfBlocks.text=activeBlock.stateString
        }
        subNumber.setOnClickListener{
            activeBlock.subFromCurrent()
            progressBar.progress=activeBlock.actualNumber
            numberOfBlocks.text=activeBlock.stateString
        }
    }

}