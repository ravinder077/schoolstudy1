package com.tuespotsolutions.schoolstudy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TestFragment extends Fragment {

    WebView webView2;
    private com.wang.avi.AVLoadingIndicatorView avi;
    private RelativeLayout progress_layout;
    private Handler handler;
    private Runnable runnable = null;

    private LinearLayout no_internet_layout,error_internet_layout;
    private Button retry_btn,exit_btn;

    public static String URL ;
    public static String title = "Subject List";
    public static String webPage_Must_contain = "tuespotsolutions.com";
    public static String Downloadable_Type = ".docx"; // if more types then use in condition with || operator like ' ||url.endsWith(".jpg") ' in  'shouldOverrideUrlLoading' method
    public static String HIDE_ELEMENT_BY_ID="nav";



    private Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }


    private void webViewGoBack(){
        webView2.goBack();
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        URL=getActivity().getString(R.string.testurl);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.webviewmain, container, false);
        webView2 = view.findViewById(R.id.webview_main);
        avi = view.findViewById(R.id.avi);
        retry_btn = view.findViewById(R.id.retry_btn);
        exit_btn = view.findViewById(R.id.exit_btn);
        error_internet_layout = view.findViewById(R.id.error_layout);
        no_internet_layout = view.findViewById(R.id.no_internet_layout);
        progress_layout = view.findViewById(R.id.progress_layout);
        startAnim();
        webViewLoad();

        webView2.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView2.canGoBack()) {
                    Log.e("HomeFragment","back key and webview canGoBack");
                    handler1.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }

        });

        return view;
    }


    /*---------------------------https://github.com/81813780/AVLoadingIndicatorView
      OR
      https://github.com/wasabeef/awesome-android-ui/blob/master/pages/Progress.md === for more progress dialogs
      */
    void startAnim(){
        //avi.show();
        // or
        avi.smoothToShow();
    }

    void stopAnim(){
        //avi.hide();
        // or
        avi.smoothToHide();
        progress_layout.setVisibility(View.GONE);
    }
    //--------------------------------------------------------------------------------

    @Override
    public void onDestroy() {
//        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onStop() {
//        handler.removeCallbacksAndMessages(null);
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onPause() {
//        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    public boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    @SuppressLint("JavascriptInterface")
    public void webViewLoad() {

        startAnim();
        progress_layout.setVisibility(View.VISIBLE);
        no_internet_layout.setVisibility(View.GONE);
        webView2.setVisibility(View.VISIBLE);

        boolean isConnected = checkInternet();
        if (isConnected) {

            //REMOTE RESOURCE
            //webView.loadUrl(URL);
            // webView.setWebViewClient(new MyWebViewClient());

            // LOCAL RESOURCE
            // webView.loadUrl("file:///android_asset/index.html");

            // Enable Javascript
            WebSettings webSettings = webView2.getSettings();


            webView2.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView2.getSettings().setSupportMultipleWindows(true);
            webView2.setWebChromeClient(new WebChromeClient());
            webView2.setHorizontalScrollBarEnabled(false);
            webView2.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView2.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);


            webView2.getSettings().setJavaScriptEnabled(true);
            webView2.getSettings().setLoadWithOverviewMode(true);
            webView2.getSettings().setUseWideViewPort(true);
           // webView2.loadUrl("http://docs.google.com/gview?embedded=true&url=http://www.stagecoachbus.com/PdfUploads/Timetable_28768_5.pdf");
            // Force links and redirects to open in the WebView instead of in a browser
            webView2.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {

                    Toast.makeText(getActivity(), "Oh no! " + description,
                            Toast.LENGTH_SHORT).show();
                    System.err.println("view : " + view + "\nerrorCode : " + errorCode + "\n description : " + description + "\nfailingUrl : " + failingUrl);
                    errorHandle();
                }

                @Override
                //Implement shouldOverrideUrlLoading//
                public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                    if(checkInternet()){


                       if(url.contains(".pdf"))
                       {

                       }
                        return urlShouldOveride(view,url);
                    }
                    else{
                       handle_layout();
                       return true; //return true will not process the web url and stay as it is
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    stopAnim();
                    progress_layout.setVisibility(View.GONE);
                    System.err.println("Page Finished");
                    //webView.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");




                    webView2.loadUrl("javascript:var con = document.getElementById('" + HIDE_ELEMENT_BY_ID + "'); " +
                            "con.parentNode.removeChild(con); ");



             /*
                webView.loadUrl("javascript:var con = document.getElementsByClassName('navbar navbar-default'); " +
                                "con.parentNode.removeChild(con); ");*/

             /*
                webView.loadUrl("javascript:var con = document.getElementsByTagName('nav'); " +
                                "con.parentNode.removeChild(con); ");*/


                    super.onPageFinished(view, url);

                }
            });

            webView2.addJavascriptInterface(this, "MyAdvanceWebView");
            webView2.loadUrl(URL);

        }else{
            handle_layout();
        }
    }

    private void errorHandle() {
        error_internet_layout.setVisibility(View.VISIBLE);
        no_internet_layout.setVisibility(View.GONE);
        webView2.setVisibility(View.GONE);

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "What to do?", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handle_layout(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                stopAnim();
                progress_layout.setVisibility(View.GONE);
                no_internet_layout.setVisibility(View.VISIBLE);
                webView2.setVisibility(View.GONE);
            }
        }, 3000);

        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewLoad();
            }
        });
    }

    private boolean urlShouldOveride(WebView view,String url){
        System.err.println("shouldOverrideUrlLoading has url : " + url);
        //Check whether the URL contains a whitelisted domain. In this example, we’re checking
        //whether the URL contains the “example.com” string//

        if (url.startsWith("mailto:")) {
            MailTo mailTo = MailTo.parse(url);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo.getTo()});
            intent.putExtra(Intent.EXTRA_TEXT, mailTo.getBody());
            intent.putExtra(Intent.EXTRA_SUBJECT, mailTo.getSubject());
            intent.putExtra(Intent.EXTRA_CC, mailTo.getCc());
            intent.setType("message/rfc822");
            startActivity(intent);
            return true;
        } else if (url.startsWith("tel:")) {
            String replaceString = url.replace("tel:", "");
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", replaceString, null));
            startActivity(intent);
            return true;
        }
        else if(url.endsWith(".pdf"))

        {
            webViewGoBack();

            String fileName = url.substring( url.lastIndexOf('/')+1, url.length() );

            Intent intent=new Intent(getActivity(), MainActivityPDF.class);
            intent.putExtra("url",fileName);
            startActivity(intent);
        }


             /*   else if(url.endsWith(".pdf")||url.endsWith(".doc")||url.endsWith(".txt")||url.endsWith(".docx")||url.endsWith(".ppt")){
                    CharSequence options[] = new CharSequence[] {"Download"};
                    final String url1 = url;

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("Select your option:");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on options[which]
                            if(which==0){
                               // downloadFile();
                            }
                        }
                    });
                    builder.setNegativeButton(getString(R.string.cancel), null);
                    builder.show();
                    return false;
                }

              /*  else if(url.endsWith(Downloadable_Type)){
                   // downloadFile();
                    return false;
                }  */


        if (Uri.parse(url).getHost().endsWith(webPage_Must_contain)) {
            //If the URL does contain the “example.com” string, then the shouldOverrideUrlLoading method
            //will return ‘false” and the URL will be loaded inside your WebView//
            // webView.loadUrl("about:blank");
            // webView.loadUrl(URL);
                    /*String s="<head><meta name='viewport' content='target-densityDpi=device-dpi'/></head>";
                    webView.loadDataWithBaseURL(null,s+htmlContent,"text/html" , "utf-8",null);
                    */
            // webView.loadUrl("javascript:document.getElementsByClassName('navbar navbar-default')[0].style.display=\"none\";");

            webView2.loadUrl("javascript:document.getElementsByTagName('body')[0].style.margin = '0px'");
            return false;
        }
        //If the URL doesn’t contain this string, then it’ll return “true.” At this point, we’ll
        //launch the user’s preferred browser, by firing off an Intent//
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

}
