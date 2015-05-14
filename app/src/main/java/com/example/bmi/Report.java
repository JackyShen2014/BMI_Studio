package com.example.bmi;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;


public class Report extends ActionBarActivity {

    private TextView view_result;
    private TextView view_suggest;
    private Button button_back;
    private Button button_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        findViews();
        setListeners();
        showResults();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
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

    public void findViews(){
        view_result = (TextView)findViewById(R.id.result);
        view_suggest = (TextView)findViewById(R.id.suggest);
        button_back = (Button)findViewById(R.id.but_back);
        button_info = (Button)findViewById(R.id.but_info);
    }

    public void setListeners(){
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close this activity
                Report.this.finish();
            }
        });

        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsDialog();
            }
        });

    }

    public void showResults(){

        DecimalFormat nf = new DecimalFormat("0.0");

        Bundle bundle = this.getIntent().getExtras();
        double result = bundle.getDouble("KEY_RESULT");

        //present result
        view_result.setText(getText(R.string.bmi_result)+nf.format(result));

        //Give health advise
        if(result>25){
            showNotification(result);
            view_suggest.setText(R.string.advice_heavy);
        }else if(result<20){
            view_suggest.setText(R.string.advice_light);
        }else{
            view_suggest.setText(R.string.advice_average);
        }


    }

    public void showNotification(double result){
        NotificationManager barManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Notification barMsg = new Notification(R.drawable.ic_launcher,"哦！您过肥了！",System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,
                new Intent(this,MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        barMsg.setLatestEventInfo(Report.this,
                "您的BMI值过高",
                "通知监督人",
                contentIntent);

        barManager.notify(0,barMsg);
    }


    private void openOptionsDialog(){
        new AlertDialog.Builder(Report.this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_msg)
                .setPositiveButton(R.string.ok_lable,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // go to map
                                Uri uri = Uri.parse(getString(R.string.contact_map));
                                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(R.string.homepage_label,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Uri uri = Uri.parse(getString(R.string.homepage_url));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                .show();
    }
}
