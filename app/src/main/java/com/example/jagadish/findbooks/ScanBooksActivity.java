/*
*
Author: Jagadish Shivanna
References: https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
**On click of a category from scanned sensors list, displays books of that category**
*
*/
package com.example.jagadish.findbooks;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.*;

//Activity for displaying books from selected scanned categories
public class ScanBooksActivity extends ActionBarActivity {

    private final static String TAG = ScanBooksActivity.class.getSimpleName();
    private EditText catName = null;
    private EditText catAdd = null;
    private EditText res = null;
    public ArrayList<String> books = new ArrayList<String>();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_books);
        catName = (EditText) findViewById(R.id.cat_name);
        catAdd = (EditText) findViewById(R.id.cat_address);
        //res = (EditText) findViewById(R.id.cat_display);
        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        String address = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        catName.setText(name);
        //catAdd.setText(address);

        ListView lv = (ListView) findViewById(R.id.booksList);
        //lv.setAdapter(new ArrayAdapter<String>(this, R.layout.listitem_category, R.id.book_name, actressArray));

        onClickCategory(name, address);
    }

    //Action on click of categories scanned
    public void onClickCategory(String name, String address) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BooksDB");
        query.whereEqualTo("Address", address);
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> catList, ParseException e) {
                ArrayList<String> names = null;
                ArrayList<String> bklocation = null;
                Set<String> bkL = null;

                if (e == null) {
                    String obj = catList.toString();
                    names = new ArrayList<String>();
                    bklocation = new ArrayList<String>();
                    bkL = new HashSet<String>();

                    for (int i = 0; i < catList.size(); i++) {
                        //Object object = objects.get(i);
                        String c = catList.get(i).getString("BookName");
                        String blc = catList.get(i).getString("BookLocation");
                        books.add(c);
                       // bklocation.add(blc);
                    }

                    for (ParseObject name : catList) {
                        String bookName = name.getString("BookName");
                        String bkplace = name.getString("BookLocation");
                        names.add(bookName);
                        bklocation.add(bkplace);
                    }

                    for(String records:bklocation) {
                        bkL.add(records);
                    }
                    //Display booklocation of category selection
                    String[] array = bkL.toArray(new String[0]);
                    for(String s : array)
                        catAdd.setText(s);

                    CharSequence[] cs = names.toArray(new CharSequence[names.size()]);
                    CharSequence[] bl = bklocation.toArray(new CharSequence[bklocation.size()]);
                    CharSequence[] bkLo = bkL.toArray(new CharSequence[bkL.size()]);

                    //catAdd.setText((bkL));
                    Log.d(TAG, "blc:" + "1" + bl);
                    Log.d(TAG, "blc:" + "2" + bklocation);
                    Log.d("score", "Retrieved " + obj + names + cs + bl + " Category");

                    //Toast.makeText(getApplicationContext(), Arrays.toString(cs),
                      //      Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "null",
                            Toast.LENGTH_SHORT).show();
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        //Display List of books on selection of scanned category
        ListView lv = (ListView) findViewById(R.id.booksList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitem_category, R.id.book_name, books);
        //lv.setAdapter(new ArrayAdapter<String>(this, R.layout.listitem_category, R.id.book_name, books));
        lv.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan_books, menu);
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
