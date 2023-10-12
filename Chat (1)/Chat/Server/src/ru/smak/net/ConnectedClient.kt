
package ru.smak.net

import ru.smak.ui.ServerUI
import java.net.Socket
import kotlin.concurrent.thread

class ConnectedClient(val client: Socket, val servUI: ServerUI) {

    private val chio = ChatIO(client)
    private var name: String? = null
    companion object{
        private val clients = mutableListOf<ConnectedClient>()
    }
    init{
        clients.add(this)
        send(Command.INTRODUCE, "Write your message:")
    }

    private fun send(cmd: Command, data: String) {
        val d = "${cmd.name}:$data"
        chio.sendMessage(d)
    }

    private fun broadcast(cmd: Command, data: String) {
        clients.forEach{client -> client.name?.let{client.send(cmd, data)}}
    }

    private fun parse(msg: String){
        val my_msg = msg.trim()
        if (name == null) {
            if (clients.find{it.name == msg} == null) {
                name = msg
                name?.also{
                    broadcast(Command.LOGGED_IN, it)
                    servUI.showMessage("$name get in the server")
                }
            } else {
                send(Command.INTRODUCE, "This is name is already taken, put another one")
            }
        }
        else if(my_msg != "" && my_msg[0] == '#'){
            val data = my_msg.substring(1).split(' ', limit = 2)
            val name = data[0]
            var message = my_msg.substring(1 + name.length + 1)
            val find_user = clients.find{it.name == name}
            if (find_user == null) {
               // message = "Пользователя с именем $name не существует"
                //clients.find { it.name == this.name }
                  //  ?.send(Command.MESSAGE, message)
            }
            else {
                clients.find { it.name == name }
                    ?.send(Command.MESSAGE, "Private message from ${this.name} Вам: $message")
                clients.find { it.name == this.name }
                    ?.send(Command.MESSAGE, "You have sent a private message to $name: $message")
                servUI.showMessage("$name + $message")
            }
        }else {
            broadcast(Command.MESSAGE, "$name: $msg")
            servUI.showMessage("$name: $msg")
        }
    }

    fun start(){
        thread {
            try {
                chio.startReceiving {
                    try {
                        parse(it)
                    } catch (e: Exception) {
                        clients.remove(this)
                        name?.let { broadcast(Command.LOGGED_OUT, it) }
                    }
                }
            } catch (e: Exception) {
                clients.remove(this)
                name?.let { broadcast(Command.LOGGED_OUT, it) }
            }
        }
    }
}