package com.visa.say2pay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText inputMoney = (EditText) findViewById(R.id.input_value);
        //final TextView output = (TextView) findViewById(R.id.output);
        Button submit = (Button) findViewById(R.id.submit);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = inputMoney.getText().toString();
                Log.d("MainActivity", amount);
                Intent intent = new Intent(MainActivity.this,
                        AWS.class);
                intent.putExtra("AMOUNT", amount);
                startActivity(intent);
            }
        });
    }


}
