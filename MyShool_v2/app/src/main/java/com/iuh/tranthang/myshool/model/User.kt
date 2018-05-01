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
    private var toCongTac: String = ""
    private var chucVu: String = ""
    private var url: String = ""
    private var coefficient: String = ""
    private var action: Boolean = true

    constructor(id: String, fullname: String, permission: String, numberphone: String,
                address: String, email: String, birthday: String, toCongTac: String, chucVu: String, url: String,
                action: Boolean, coefficient: String) {
        this.address = address
        this.email = email
        this.fullname = fullname
        this.numberphone = numberphone
        this.permission = permission
        this.uid = id
        this.birthday = birthday
        this.toCongTac = toCongTac
        this.chucVu = chucVu
        this.url = url
        this.action = action
        this.coefficient = coefficient
    }

    constructor()

    public fun getChucVu(): String {
        return chucVu
    }

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

    public fun setPermission(permission: String) {
        this.permission = permission
    }

    public fun getUrl(): String {
        return url
    }

    public fun getToCongTac(): String {
        return toCongTac
    }

    public fun getCoefficient(): String {
        return coefficient
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

    public fun getNumberphone(): String {
        return numberphone
    }

    public fun setEmail(email: String) {
        this.email = email
    }

    public fun setBirthday(birthday: String) {
        this.birthday = birthday
    }

    public fun setChucVu(chucVu: String) {
        this.chucVu = chucVu
    }

    public fun setUrl(url: String) {
        this.url = url
    }

    public fun isAction(): Boolean {
        return action
    }

    public fun setAction(ac: Boolean) {
        this.action = ac
    }

    public fun setToCongTac(toCongTac: String) {
        this.toCongTac = toCongTac
    }

    public fun setCoefficient(coefficient: String) {
        this.coefficient = coefficient
    }

    fun toMap(): HashMap<String, String> {
        val result = HashMap<String, String>()
        result.put(Parameter.comp_fullname, fullname)
        result.put(Parameter.comp_address, address)
        result.put(Parameter.comp_birthday, birthday)
        result.put(Parameter.comp_numberphone, numberphone)
        result.put(Parameter.comp_Permission, permission)
        result.put(Parameter.comp_email, email)
        result.put(Parameter.comp_toCongTac, toCongTac)
        result.put(Parameter.comp_chucVu, chucVu)
        result.put(Parameter.comp_url, url)
        result.put(Parameter.comp_action, action.toString())
        result.put(Parameter.comp_UId, uid)
        return result
    }


}