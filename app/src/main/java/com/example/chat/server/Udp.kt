package com.example.chat.server

import com.example.chat.constants.Constants.UPD_PORT
import io.reactivex.Single
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException

class Udp {

    private val port = UPD_PORT

    fun getServerIp(): String {
        val socket = DatagramSocket()
        var ip = ""

        try {
            val message = "Hello World".toByteArray()
            var packet = DatagramPacket(
                message,
                message.size,
                InetAddress.getByName("255.255.255.255"),
                port
            )
            socket.soTimeout = 15000
            socket.send(packet)

            val buffer = ByteArray(256)
            packet = DatagramPacket(buffer, buffer.size)
            socket.receive(packet)
            ip = packet.address.hostAddress

        } catch (e: SocketException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        socket.close()
        return ip
    }

}