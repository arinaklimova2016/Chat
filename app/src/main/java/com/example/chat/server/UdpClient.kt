package com.example.chat.server

import com.example.chat.constants.Constants.UPD_PORT
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException

class UdpClient {
    suspend fun getServerIp(): String {
        val socket = DatagramSocket()
        var ip = ""
        var count = 100

        while (ip.isEmpty()) {

            if (count == 0) {
                throw Exception("Error")
            }
            count--

            try {
                val message = "Hello World".toByteArray()
                var packet = DatagramPacket(
                    message,
                    message.size,
                    InetAddress.getByName("255.255.255.255"),
                    UPD_PORT
                )
                socket.soTimeout = 5000
                socket.send(packet)

                val buffer = ByteArray(256)
                packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                ip = packet.address.hostAddress

            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        socket.close()
        return ip
    }
}