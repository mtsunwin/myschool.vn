package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/6/2018.
 */
open class adm_display {
    private var name: String = ""
    private var img: Int = 0
    private var id: Int = 0

    constructor(name: String, img: Int, id: Int) : super() {
        this.name = name
        this.img = img
        this.id = id
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

    public fun getId(): Int {
        return id
    }

    public fun setId(id: Int) {
        this.id = id
    }

    constructor()

}