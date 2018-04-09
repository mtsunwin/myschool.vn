package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/7/2018.
 */
class adm_item : adm_display {
    private lateinit var nameParent: String
    private var imgParent: Int = 0
    private var listDisplay: ArrayList<adm_display>

    constructor(name: String, listDisplay: ArrayList<adm_display>, img: Int) : super() {
        this.nameParent = name
        this.listDisplay = listDisplay
        this.imgParent = img
    }

    public fun getNameParent(): String {
        return nameParent;
    }

    public fun getInt(): Int {
        return imgParent;
    }

    public fun setNameParent(name: String) {
        this.nameParent = name
    }

    public fun setImgParent(img: Int) {
        this.imgParent = img
    }

    public fun getListDisplay(): ArrayList<adm_display> {
        return listDisplay
    }

    public fun setListDisplay(listDisplay: ArrayList<adm_display>) {
        this.listDisplay = listDisplay
    }
}