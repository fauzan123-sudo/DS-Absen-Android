package com.infinity.dsmabsen.helper

import io.paperdb.Paper

class CacheManager {

    //keys
    private var nipKey = "passKey"
    private var phoneKey = "phoneKey"

    private var pass: String = ""
    private var phone: String = ""

    init {
        pass = Paper.book().read(nipKey, "").toString()
//        phone = Paper.book().read(phoneKey, "")

    }

    //------------ Clear Cache Value ------------//

    fun clearPass() {
        pass = ""
        Paper.book().delete(nipKey)
    }

    fun clearPhone() {
        phone = ""
        Paper.book().delete(phoneKey)
    }

    fun clearAll() {
        pass = ""
        phone = ""

        Paper.book().destroy()
    }

    //------------ Get Cache Value ------------//

    fun getPass(): String = pass
    fun getPhone(): String = phone


    //------------ Set Cache Value ------------//

    fun setNip(pass: String) {
        this.pass = pass
        Paper.book().write(nipKey, pass)
    }

    fun setPhone(phone: String) {
        this.phone = phone
        Paper.book().write(phoneKey, phone)
    }
}