package com.example.aaron.sos;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;


public class SetActivity extends ActionBarActivity {
    private Model mModel;
    EditText editPhone;
    EditText editeMessage;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        mModel = Util.load(this);
        editPhone = (EditText) findViewById(R.id.set_phone);
        editPhone.setText(mModel.getPhone());
        editeMessage = (EditText) findViewById(R.id.set_message);
        editeMessage.setText(mModel.getMessage());
        button = (Button)findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.setPhone(editPhone.getText().toString());
                mModel.setMessage(editeMessage.getText().toString());
                try {
                    Util.save(mModel,SetActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
