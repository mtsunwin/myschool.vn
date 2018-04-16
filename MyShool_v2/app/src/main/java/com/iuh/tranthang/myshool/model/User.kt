package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/11/2018.
 */
class User {
    private var uid: String = ""
    private var fullname: String = ""
    private var permission: String = ""
    private var numberphone: String = ""
    private var address: String = ""
    private var birthday: String = ""
    private var email: String = ""

    constructor(id: String, fullname: String, permission: String, numberphone: String,
                address: String, email: String, birthday: String) {
        this.address = address
        this.email = email
        this.fullname = fullname
        this.numberphone = numberphone
        this.permission = permission
        this.uid = id
        this.birthday = birthday
    }

    constructor()

    public fun getUid(): String {
        return uid
    }

    public fun getBirthday(): String {
        return birthday
    }

    public fun getEmail(): String {
        return email
    }

    public fun getAddress(): String {
        return address
    }

    public fun getFullname(): String {
        return fullname
    }

    public fun getPermission(): String {
        return permission
    }

    public fun getNumberphone(): String {
        return numberphone
    }

    public fun setUid(id: String) {
        this.uid = id
    }

    public fun setAddress(address: String) {
        this.address = address
    }

    public fun setFullname(fullname: String) {
        this.fullname = fullname
    }

    public fun setNumberphone(numberphone: String) {
        this.numberphone = numberphone
    }

    public fun setEmail(email: String) {
        this.email = email
    }

    public fun setBirthday(birthday: String) {
        this.birthday = birthday
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("fullname", fullname)
        result.put("address", address)
        result.put("birthday", birthday)
        result.put("numberphone", numberphone)
        result.put("permission", permission)
        result.put("email", email)
        return result
    }
}