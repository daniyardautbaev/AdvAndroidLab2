package com.example.chatlibrary

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val text: TextView = itemView.findViewById(R.id.messageText)

    fun bind(message: Message) {
        text.text = message.text
        text.textAlignment = if (message.isSent)
            View.TEXT_ALIGNMENT_TEXT_END
        else
            View.TEXT_ALIGNMENT_TEXT_START
    }
}
