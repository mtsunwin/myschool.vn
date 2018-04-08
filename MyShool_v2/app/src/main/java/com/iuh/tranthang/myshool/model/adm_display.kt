package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/6/2018.
 */
open class adm_display {
    private var name: String = ""
    private var img: Int = 0

    constructor(name: String, img: Int) : super() {
        this.name = name
        this.img = img
    }

    public fun getName(): String {
        return name
    }

    public fun setName(name: String) {
        this.name = name
    }

    public fun getImg(): Int {
        return img
    }

    public fun setImg(img: Int) {
        this.img = img
    }

    constructor()

}