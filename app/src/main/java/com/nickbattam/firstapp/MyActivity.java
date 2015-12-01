package com.nickbattam.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Date;
import java.util.List;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.nickbattam.firstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        runDBExample();
    }

    private void runDBExample() {

        DBHandler db = new DBHandler(this);

        Log.d("Insert: ", "Inserting ..");
        db.addBlood(new Blood(11, new Date(), 5.7));
        db.addBlood(new Blood(13, new Date(2015, 10, 4, 14, 2), 15.7));
        db.addBlood(new Blood(14, new Date(2015, 5, 16, 3, 28), 3.5));
        db.addBlood(new Blood(16, new Date(2015, 3, 25, 11, 16), 7.1));

        Log.d("Reading: ", "Reading all bloods...");
        List<Blood> bloods = db.getAllBloods();

        for (Blood blood : bloods) {
            String log = "Id: " + blood.getId() +
                    " ,value: " + Double.toString(blood.getValue()) +
                    " ,date: " + blood.getDatetime().toString();
// Writing bloods to log
            Log.d("Bloods : ", log);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
