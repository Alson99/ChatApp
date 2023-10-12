package ru.smak.net

import ru.smak.ui.ConsoleUI
import java.net.ServerSocket
import kotlin.concurrent.thread

class Server(port: Int = 5005) {

    private val ss: ServerSocket = ServerSocket(port)
    private var isActive = true

    fun start(){
        isActive = true
        thread {
            val ui = ConsoleUI()
            while (isActive) {
                ConnectedClient(ss.accept(), ui).start()
            }
        }
    }

    fun stop(){
        isActive = false
        ss.close()
    }
}