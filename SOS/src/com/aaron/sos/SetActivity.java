package com.aaron.sos;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class SetActivity extends ActionBarActivity implements OnClickListener{
    private Model mModel;
    EditText editPhone;
    EditText editeMessage;
    Button button_ok;
    ImageButton button_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        mModel = Util.load(this);
        editPhone = (EditText) findViewById(R.id.set_phone);
        editPhone.setText(mModel.getPhone());
        editeMessage = (EditText) findViewById(R.id.set_message);
        editeMessage.setText(mModel.getMessage());
        button_ok = (Button)findViewById(R.id.ok);
        button_contact = (ImageButton) findViewById(R.id.contact_button);
        button_ok.setOnClickListener(this);
        button_contact.setOnClickListener(this);

    }
    
    
    public void onClick(View arg0) {
    	switch (arg0.getId() ) {
		case R.id.ok:
			  mModel.setPhone(editPhone.getText().toString());
              mModel.setMessage(editeMessage.getText().toString());
              try {
                  Util.save(mModel,SetActivity.this);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              finish();
			break;
		case R.id.contact_button:
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 1); 
			break;
		default:
			break;
		}
    	
    };
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data);  
        switch(requestCode)  
        {  
      
            case (1) :  
            {  
      
            if (resultCode == Activity.RESULT_OK)  
            {  
      
            Uri contactData = data.getData();  
      
            Cursor c = managedQuery(contactData, null, null, null, null);  
      
            c.moveToFirst();  
      
            String phoneNum=this.getContactPhone(c);  
            editPhone.setText(phoneNum);  
              
            }  
              
            break;  
              
            }  
      
        }  
      
    }  

    private String getContactPhone(Cursor cursor){
    	int phoneColum = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
    	int phoneNum = cursor.getInt(phoneColum);
    	String phoneResult ="";
    	
    	if(phoneNum>0){
    		int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
    		String contactId = cursor.getString(idColumn);
    		
    		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
    													null, 
    													ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactId,
    													null, null);
    		if(phones.moveToFirst()){
    			for(;!phones.isAfterLast();phones.moveToNext()){
    				int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
    				int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
    				int phone_type = phones.getInt(typeindex);
    				String phoneNumber = phones.getString(index);
    				
    				phoneResult = phoneNumber;
    				
    			}
    			if(!phones.isClosed()){
    				phones.close();
    			}
    		}
    	}
    	return phoneResult;
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
        // automatically handle clicks on the Home/Up button_ok, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
