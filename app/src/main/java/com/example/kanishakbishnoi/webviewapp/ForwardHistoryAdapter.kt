package com.example.kanishakbishnoi.webviewapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import kotlinx.android.synthetic.main.web_item_history.view.*

/**
 * Created by kanishak bishnoi on 27-10-2017.
 */
class ForwardHistoryAdapter(val dialog:HistoryDialogFragment,val webview: WebView): RecyclerView.Adapter<ForwardHistoryAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val webHistory =webview.copyBackForwardList()
         holder.title.text=webHistory.getItemAtIndex(webHistory.currentIndex+position +1).title
        holder.favicon.setImageBitmap(webHistory.getItemAtIndex(webHistory.currentIndex+position +1).favicon)
        holder.itemView.setOnClickListener{
            for(i in webHistory.currentIndex +1 until webHistory.size){
                if(holder.title.text.equals(webHistory.getItemAtIndex(i))){
                    webview.goBackOrForward(i-webHistory.currentIndex)
                    dialog.dismiss()
                    break
                }
            }
        }

    }

    override fun getItemCount() =webview.copyBackForwardList().size - webview.copyBackForwardList().currentIndex-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val layouInflater= LayoutInflater.from(parent.context)
        val  webHistoryView = layouInflater.inflate(R.layout.web_item_history,parent,false)
        return ViewHolder(webHistoryView)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = itemView.title
        val favicon = itemView.favicon

    }
    
}