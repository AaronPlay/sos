package com.example.aaron.sos;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;


public class MyActivity extends ActionBarActivity {
    private ImageButton button;
    private Model mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mModel = Util.load(this);
        if(mModel == null){
            mModel = new Model("110","");

            try {
                Util.save(mModel,this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Model t = Util.load(this);

        }

        final MediaPlayer player  =    MediaPlayer.create(this,R.raw.alarm);
        player.setLooping(true);
        button = (ImageButton) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            int choice = 1;

            @Override
            public void onClick(View v) {
                if(choice == 1){
                    button.setBackgroundResource(R.drawable.d);
                    SmsManager smsManager = SmsManager.getDefault();
                   smsManager.sendTextMessage(mModel.getPhone(), null,mModel.getMessage(),null,null);
                   Toast.makeText(getApplicationContext(),"had sent message to"+mModel.getPhone(),Toast.LENGTH_SHORT).show();

                   player.start();
                    choice =2;
                }else {
                    button.setBackgroundResource(R.drawable.s);
                    player.pause();
                    choice=1;
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mModel = Util.load(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SetActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
