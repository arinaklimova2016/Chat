package com.example.chat.data.server

import com.example.chat.utils.Constants.DELAY
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.nio.channels.NoConnectionPendingException

class UdpClientImpl : UdpClient {

    companion object {
        private const val HOST = "255.255.255.255"
        private const val MESSAGE = "Hello World"
        private const val COUNT = 20
        private const val BYTEARRAYSIZE = 256
        private const val UPD_PORT = 8888
    }

    override suspend fun getServerIp(): String {
        val socket = DatagramSocket()
        var ip = ""
        var count = COUNT

        while (ip.isEmpty()) {

            if (count == 0) {
                throw NoConnectionPendingException()
            }
            count--

            try {
                val message = MESSAGE.toByteArray()
                var packet = DatagramPacket(
                    message,
                    message.size,
                    InetAddress.getByName(HOST),
                    UPD_PORT
                )
                socket.soTimeout = DELAY.toInt()
                socket.send(packet)

                val buffer = ByteArray(BYTEARRAYSIZE)
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