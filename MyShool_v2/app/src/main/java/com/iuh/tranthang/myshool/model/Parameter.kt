package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/12/2018.
 */
class Parameter {

    private var dbNodeUser: String? = ""
    private var dbNodeInfor: String? = ""

    init {
        this.dbNodeUser = "Users"
        this.dbNodeInfor = "Infor"
    }

    fun getDbNodeUser(): String? {
        return dbNodeUser
    }

    fun getdbNodeInfor(): String? {
        return dbNodeUser
    }
}