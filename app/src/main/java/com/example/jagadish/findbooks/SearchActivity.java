package com.example.jagadish.findbooks;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.parse.*;
import java.util.*;
import android.util.Log;
//import com.example.android.bluetoothlegatt;

public class SearchActivity extends Activity {
    private EditText search=null;
    private Button searchbtn;
    private TextView res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //search = (EditText)findViewById(R.id.editText3);
        //res = (TextView)findViewById(R.id.textView7);
        //searchbtn = (Button)findViewById(R.id.button2);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void search(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BooksDB");
        query.whereEqualTo("BookName", search.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> catList, ParseException e) {
                ArrayList<String> names = null;
                ArrayList<String> bklocation = null;
                if (e == null) {
                    String obj =  catList.toString();
                    //String obj1 =  String.valueOf(obj);
                    //String obj2 = catList.get("BookName").toString();
                    names = new ArrayList<String>();
                    bklocation = new ArrayList<String>();
                    for (ParseObject name : catList)
                    {
                        String bookName = name.getString("Category");
                        String bkplace = name.getString("BookLocation");
                        names.add(bookName);
                        bklocation.add(bkplace);
                    }

                    CharSequence[] cs = names.toArray(new CharSequence[names.size()]);
                    CharSequence[] bl = bklocation.toArray(new CharSequence[bklocation.size()]);
                    //res.setText(Arrays.toString(cs)+Arrays.toString(bl));
                    Log.d("score", "Retrieved " + obj + names + cs + bl + " Category");
                    Toast.makeText(getApplicationContext(),Arrays.toString(cs),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"null",
                            Toast.LENGTH_SHORT).show();
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        //Intent intent = new Intent(SearchActivity.this, DeviceScanActivity.class);
        //SearchActivity.this.startActivity(intent);
    }

    public void scan(View view) {
       Intent intent = new Intent(SearchActivity.this, ScanActivity.class);
       SearchActivity.this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
