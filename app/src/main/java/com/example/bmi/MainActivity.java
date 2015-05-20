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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private Button button_calc;
	private Spinner spin_height;
	private EditText field_weight;

    private int mHeight;

    private static final String PREF = "BMI_PREFER";
    private static final String PREF_HEIGHT = "PREF_HEIGHT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViews();
        setListeners();
        restorePrefer();
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
    	field_weight = (EditText) findViewById(R.id.weight);
        spin_height = (Spinner) findViewById(R.id.hight_spinner);

        ArrayAdapter<CharSequence> adapter_height = ArrayAdapter.createFromResource(this,
                R.array.height,android.R.layout.simple_spinner_item);
        adapter_height.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_height.setAdapter(adapter_height);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences(PREF,0);
        pref.edit().putInt(PREF_HEIGHT, (spin_height.getSelectedItemPosition() + 160)).commit();

    }

    private void setListeners(){
    	button_calc.setOnClickListener(calcBMI);
        spin_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHeight = position+160;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void restorePrefer(){
        SharedPreferences pref = getSharedPreferences(PREF,0);
        int height  = pref.getInt(PREF_HEIGHT, 0);
        if (height != 0) {
            spin_height.setSelection(height-160);
            field_weight.requestFocus();
        }

    }
    
    private Button.OnClickListener calcBMI = new Button.OnClickListener(){

		@Override
		public void onClick(View arg0) {
			

			double BMI = 0;
			if("".equals(field_weight.getText().toString().trim())){
				Toast.makeText(MainActivity.this, R.string.input_err, Toast.LENGTH_SHORT).show();
                field_weight.requestFocus();
			}else{

				double weight = Double.parseDouble(field_weight.getText().toString());
				try{
					if( mHeight != 0 && weight != 0){
                        double height = mHeight;
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
