package ru.smak.net

import ru.smak.ui.UI
import java.net.Socket
import kotlin.concurrent.thread

class Client(
    host: String = "127.0.0.1",
    port: Int = 5005,
    val ui  : UI
) {

    private val s: Socket = Socket(host, port)
    private val chio = ChatIO(s)
    init{
        ui.receiver = ::sendMessage
    }

    fun start(){
        thread {
            chio.startReceiving(::parse)
        }
    }

    fun sendMessage(msg: String){
        chio.sendMessage(msg)
    }

    // Please note that the "order: command" is not part of the order and is not required.
    private fun parse(msg: String){
        val (strCommand, message) = msg.split(":", limit = 2)
        try {
            when (Command.valueOf(strCommand)) {
                Command.INTRODUCE -> {
                    ui.showComment(message)
                }
                Command.MESSAGE -> {
                    ui.showMessage(message)
                }
                Command.LOGGED_IN ->{
                    ui.showMessage("User $message get in the chat")
                    ui.showComment("Write a message: ")
                }
                Command.LOGGED_OUT ->{
                    ui.showMessage("user $message quit the chat")
                }
            }

        } catch (_:Throwable) {}
    }
}