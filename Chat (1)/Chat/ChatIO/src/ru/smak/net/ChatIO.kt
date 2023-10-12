package ru.smak.net

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ChatIO(socket: Socket) {
    private val br = BufferedReader(InputStreamReader(socket.getInputStream()))
    private val pw = PrintWriter(socket.getOutputStream())
    private var isActive = true
    // принимает сообщения от сервера/клиента
    fun startReceiving(parse: (String)->Unit){
        isActive = true
        while (isActive){
            //когда сообщение есть, вызывает метод parse
            parse(br.readLine())
        }
    }

    // отправляет в сокет сообщение, которое идет на сервер/клиент
    fun sendMessage(msg: String){
        pw.println(msg)
        pw.flush()
    }

    fun stop(){
        isActive = false
    }
}