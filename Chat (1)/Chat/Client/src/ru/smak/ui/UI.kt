package ru.smak.ui

interface UI {
    fun showMessage(msg: String)
    fun showComment(cmt: String)

    var receiver: ((String)->Unit)?
}