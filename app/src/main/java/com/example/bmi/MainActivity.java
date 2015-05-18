package com.example.bmi;

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private Button button_calc;
	private EditText field_height;
	private EditText field_weight;

    private static final String PREF = "BMI_PREFER";
    private static final String PREF_HIGHT = "PREF_HIGHT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViews();
        SharedPreferences pref = getSharedPreferences(PREF,0);
        String hight  = pref.getString(PREF_HIGHT,"");
        if (!"".equals(hight)) {
            field_height.setText(hight);
            field_weight.requestFocus();
        }
        setListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    private void findViews(){
    	button_calc = (Button) findViewById(R.id.calButton);
    	field_height = (EditText) findViewById(R.id.height);
    	field_weight = (EditText) findViewById(R.id.weightText);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences(PREF,0);
        pref.edit().putString(PREF_HIGHT,field_height.getText().toString()).commit();

    }

    private void setListeners(){
    	button_calc.setOnClickListener(calcBMI);
    }
    
    private Button.OnClickListener calcBMI = new Button.OnClickListener(){

		@Override
		public void onClick(View arg0) {
			

			double BMI = 0;
			if("".equals(field_height.getText().toString().trim()) || 
					"".equals(field_weight.getText().toString().trim())){
				Toast.makeText(MainActivity.this, R.string.input_err, Toast.LENGTH_SHORT)
				.show();				
			}else{
				double height = Double.parseDouble(field_height.getText().toString());
				double weight = Double.parseDouble(field_weight.getText().toString());
				try{
					if( height != 0 && weight != 0){
						height = height/100;
						BMI = weight/(height*height);

                        Intent i = new Intent(MainActivity.this,Report.class);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("KEY_RESULT",BMI);
                        i.putExtras(bundle);
                        startActivity(i);

					}else{
						Toast.makeText(MainActivity.this, R.string.input_err2, Toast.LENGTH_SHORT)
						.show();	
					}							
				}catch(Exception e){
					e.printStackTrace();
				}		
			}	
		}   	
    };


 
}
