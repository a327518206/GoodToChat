package com.xiaoluogo.bottomnavigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigation navigation = (BottomNavigation) findViewById(R.id.bottom_navigation);
        final TextView text = (TextView) findViewById(R.id.text);
        navigation.setClickedPosition(0);
        navigation.setBottomTitle("news","service","setting","hot","me");
        navigation.setOnBtnClickListener(new BottomNavigation.OnBtnClickListener() {
            @Override
            public void onClick(int position) {
                text.setText(position+"");
            }
        });
    }
}
