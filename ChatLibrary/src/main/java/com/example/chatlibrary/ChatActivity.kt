package com.example.chatlibrary

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var webSocketClient: WebSocketClient
    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activtiy_chat) // Исправлено имя layout-файла

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<Button>(R.id.sendButton)

        adapter = MessageAdapter(messageList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        initWebSocket()

        sendButton.setOnClickListener {
            val message = editText.text.toString()
            if (message.isNotEmpty()) {
                webSocketClient.send(message)
                addMessage(Message(message, true))
                editText.text.clear()
            }
        }
    }

    private fun initWebSocket() {
        val uri = URI("wss://echo.websocket.org")
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {}

            override fun onMessage(message: String?) {
                runOnUiThread {
                    val text = if (message == "203 = 0xcb") {
                        "📡 Predefined message received!"
                    } else message ?: ""
                    addMessage(Message(text, false))
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {}
            override fun onError(ex: Exception?) {}
        }
        webSocketClient.connect()
    }

    private fun addMessage(message: Message) {
        messageList.add(message)
        adapter.notifyItemInserted(messageList.size - 1)
        findViewById<RecyclerView>(R.id.recyclerView).scrollToPosition(messageList.size - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::webSocketClient.isInitialized) {
            webSocketClient.close()
        }
    }
}
