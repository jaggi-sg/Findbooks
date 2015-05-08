package com.example.jagadish.findbooks;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.Window;

import com.parse.*;

public class MainActivity extends ActionBarActivity {
    private EditText username = null;
    private EditText password = null;
    private TextView attempts;
    private Button login;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.ic_launcher);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "KYqc5B0h1s4RpaaEF6w5OBO7ryBDeKz5LAiPTUus", "UPGQw0y1abtcaocb1cFPPIbiim6sjyPymQuIsK8i");
        //username = (EditText) findViewById(R.id.editText1);
       // password = (EditText) findViewById(R.id.editText2);
        //attempts = (TextView)findViewById(R.id.textView5);
        //attempts.setText(Integer.toString(counter));
        login = (Button) findViewById(R.id.button1);
    }

    public void login(View view) {
      /*  if (username.getText().toString().equalsIgnoreCase("admin") &&
                password.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(), "Redirecting...",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            MainActivity.this.startActivity(intent);
            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "bar");
            testObject.saveInBackground();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Credentials",
                    Toast.LENGTH_SHORT).show();
            //attempts.setBackgroundColor(Color.RED);
            counter--;
            // attempts.setText(Integer.toString(counter));
            if (counter == 0) {
                login.setEnabled(false);
            }

        } */

        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        MainActivity.this.startActivity(intent);
        Toast.makeText(getApplicationContext(), "Starting..",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
