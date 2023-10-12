package ru.smak.ui

//реализуем интерфейс
class ConsoleUI : ServerUI {

    override fun showMessage(msg: String){
        println(msg)
    }

}