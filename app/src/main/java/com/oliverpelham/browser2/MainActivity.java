package com.oliverpelham.browser2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.oliverpelham.browser2.data.Bookmark;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "debug";
    private WebView webView;
    private EditText searchBar;
    private String currentURL;
    private String homeUrl = "https://www.google.com";
    private BookmarkAdaptor adaptor;
    private List<Bookmark> bookmarkList = new ArrayList<>();
    private Button returnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        webView = findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());
        searchBar = (EditText) findViewById(R.id.editSearch);


        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String target = searchBar.getText().toString();
                    if(target.contains("http://") || target.contains("https://")){

                    } else {
                        target = "http://"+target;
                    }
                    currentURL = target;
                    webView.loadUrl(currentURL);
                    hideKeyBoard();

                    searchBar.setText("");
                }
                return false;
            }
        });

//        webView.loadUrl(homeUrl);
//        if(savedInstanceState != null){
//            webView.restoreState(savedInstanceState.getBundle("webViewKey"));
//
//            //testing
//            if(webView.restoreState(savedInstanceState.getBundle("webViewKey")) != null){
//                Toast.makeText(this, "savedinstance not null", Toast.LENGTH_SHORT ).show();
//            }
//
//        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                String target = searchBar.getText().toString();
                if(target.contains("http://") || target.contains("https://")){

                } else {
                    target = "http://"+target;
                }
                currentURL = target;
                webView.loadUrl(currentURL);
                searchBar.setText("");
                hideKeyBoard();
                return true;
            case R.id.action_forward:
                if(webView.canGoForward()){
                    webView.goForward();
                }
                return true;
            case R.id.action_refresh:
                webView.loadUrl(webView.getUrl());
                searchBar.setText("");
                hideKeyBoard();
                return true;
            case R.id.action_home:
                webView.loadUrl(homeUrl);
                searchBar.setText("");
                hideKeyBoard();
                return true;
            case R.id.settings_setbookmark:
                //TODO link to bookmarks
                bookmarkList.add(new Bookmark("Bookmark", webView.getUrl()));
                Toast.makeText(this, "Bookmark added", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.settings_sethome:
                this.homeUrl = webView.getUrl();
                Toast.makeText(this, "Home updated", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_bookmarks:
                //TODO open bookmarks
                setUpScreen();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        webView.saveState(bundle);

        outState.putBundle("webViewKey", bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);



        if(savedInstanceState != null) {
            webView.restoreState(
                    savedInstanceState.getBundle("webViewKey"));

            if(savedInstanceState.getBundle("webViewKey") == null){
//                setContentView(R.layout.activity_main);
                webView = findViewById(R.id.webview);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(homeUrl);
                searchBar.setText("");
            }

        }else {
//            setContentView(R.layout.activity_main);
            webView = findViewById(R.id.webview);
            webView.setWebViewClient(new WebViewClient());

            Toast.makeText(this, "home urrrrrrrl", Toast.LENGTH_SHORT ).show();
            webView.loadUrl(homeUrl);
            searchBar.setText("");
            Log.d(TAG, "onRestoreInstanceState: loaded home URL");

        }
    }

    private void setUpScreen() {
        setContentView(R.layout.bookmarks_layout);
        ListView listView = (ListView)findViewById(R.id.bookmark_listview);

        adaptor = new BookmarkAdaptor(this, bookmarkList);

        listView.setAdapter(adaptor);

        returnButton = findViewById(R.id.returnbutton);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUpMainScreen();
                webView.loadUrl(bookmarkList.get(position).getUrl());
                searchBar.setText("");

            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpMainScreen();
                webView.loadUrl(currentURL);
                searchBar.setText("");
            }
        });


    }

    private void setUpMainScreen() {
        currentURL = webView.getUrl();
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        webView = findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());
        searchBar = (EditText) findViewById(R.id.editSearch);

    }

    private void hideKeyBoard(){
        if(getCurrentFocus() == null){
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }



}
