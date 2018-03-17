package com.example.gufan.myscrollview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gufan.myscrollview.ui.MyScrollView;
import com.example.gufan.myscrollview.utils.MyUtils;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyScrollView myScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        LayoutInflater inflater = getLayoutInflater();
        myScrollView = findViewById(R.id.myScrollView);
        final int ScreenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        final int ScreenHeight = MyUtils.getScreenMetrics(this).heightPixels;
        for (int i = 0; i < 3 ; i++) {
            ViewGroup childView = (ViewGroup)inflater.inflate(R.layout.content_layout,myScrollView,false);
            childView.getLayoutParams().width = ScreenWidth;
            TextView tv = childView.findViewById(R.id.title);
            tv.setText("page "+i);
            childView.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            creatList(childView);
            myScrollView.addView(childView);
        }
    }

    private void creatList(ViewGroup childView){
        ListView listView = childView.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.content_list_layout,
                R.id.name,datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "this is "+position, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
