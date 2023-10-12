package ru.smak

import MainFrame
import ru.smak.net.Client

fun main() {

    MainFrame().also {
        Client(ui = it).start()
        it.isVisible = true
    }
}