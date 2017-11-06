package com.example.kanishakbishnoi.webviewapp


import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.browser_toolbar.*


class MainActivity : AppCompatActivity(), HistoryDialogFragment.WebHistory {
    override fun getWebView(): WebView {
        return webView
    }

    /*  override fun webpageSelected(webTitle: String) {
          val webHistory = webView.copyBackForwardList()
          for (i in 0 until webHistory.size) {
              if (webHistory.getItemAtIndex(i).title.equals(webTitle)){
                  webView.goBackOrForward(i-webHistory.currentIndex)
                          break
              }
          }
      }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uriText.setOnEditorActionListener { textView, i, keyEvent ->
            if( i == EditorInfo.IME_ACTION_SEND) {
                loadWebpage()
                true
            } else false
        }
        backButton.setOnClickListener{
            if(webView.canGoBack()) webView.goBack()
        }
        backButton.setOnLongClickListener{
            getHistoryDialog(getBackHistory(),true)
            true
        }
        forwardButton.setOnClickListener{
            if(webView.canGoForward()) webView.goForward()
        }
        forwardButton.setOnLongClickListener(){
            getHistoryDialog(getForwardList(),false)
            true
        }
    }

    fun getForwardList():ArrayList<String>{
        val webforwardhistory = webView.copyBackForwardList()
        val historyList =ArrayList<String>()

        for(i in 0 until webforwardhistory.currentIndex-1) {
            historyList.add(webforwardhistory.getItemAtIndex
            (webforwardhistory.currentIndex+i+1).title)
        }
                    return historyList
    }


    fun getHistoryDialog(historyList :ArrayList<String>,backAdapter:Boolean){
        val historyDialogFragment= HistoryDialogFragment()
        val bundle = Bundle()
        bundle.putBoolean(historyDialogFragment.selectBackAdapter,backAdapter)
        historyDialogFragment.arguments = bundle
        historyDialogFragment.show(supportFragmentManager,"HistoryDialog")
    }


    fun getBackHistory():ArrayList<String>{
        val webbackHistory=webView.copyBackForwardList()
        val historyList =ArrayList<String>()

        for(i in 0 until webbackHistory.currentIndex)  //does not include the index of last page
            historyList.add(webbackHistory.getItemAtIndex(i).title)
        historyList.reverse()
        return historyList
    }

    @Throws(UnsupportedOperationException::class)
    fun buildURI(authority:String):Uri {
        val builder=Uri.Builder()
        builder.scheme("https")
                .authority(authority)
        return builder.build()
    }

    fun loadWebpage(){
        //enable java script
        //keep browsing links webview
        webView.settings.javaScriptEnabled = true
        PageLoadStatus()
        updateProgress()
       try {
           val url= buildURI(uriText.text.toString())
           webView.loadUrl(url.toString())
       } catch (e:UnsupportedOperationException){
           e.printStackTrace()
       }
    }
    fun updateProgress(){
        webView.webChromeClient=object:WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pageLoadProgressBar.progress=newProgress
            }
        }
    }
    fun PageLoadStatus(){
        webView.webViewClient = object:WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {

                pageLoadProgressBar.visibility=View.GONE

            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                pageLoadProgressBar.visibility= View.VISIBLE
                pageLoadProgressBar.progress=0
            }
        }
    }
}

