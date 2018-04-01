package com.example.gufan.itourism;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gufan.itourism.fragment.FindFragment;
import com.example.gufan.itourism.fragment.MainFragment;
import com.example.gufan.itourism.fragment.MeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private FragmentTransaction mFragmentTransaction;

    private LinearLayout mMenuMain;
    private LinearLayout mMenuFind;
    private LinearLayout mMenuMe;

    private MainFragment mainFragment = new MainFragment();
    private FindFragment findFragment = new FindFragment();
    private MeFragment   meFragment   = new MeFragment();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_main:
                getSupportFragmentManager().beginTransaction()
                        .hide(findFragment)
                        .hide(meFragment)
                        .show(mainFragment)
                        .commit();
                break;
            case R.id.menu_find:
                getSupportFragmentManager().beginTransaction()
                        .hide(mainFragment)
                        .hide(meFragment)
                        .show(findFragment)
                        .commit();
                break;
            case R.id.menu_me:
                getSupportFragmentManager().beginTransaction()
                        .hide(mainFragment)
                        .hide(findFragment)
                        .show(meFragment)
                        .commit();
                break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content,mainFragment)
                .add(R.id.container_content,findFragment)
                .hide(findFragment)
                .add(R.id.container_content,meFragment)
                .hide(meFragment)
                .commit();
    }

    public void initView(){
        mMenuMain = findViewById(R.id.menu_main);
        mMenuFind = findViewById(R.id.menu_find);
        mMenuMe = findViewById(R.id.menu_me);

        mMenuMain.setOnClickListener(this);
        mMenuFind.setOnClickListener(this);
        mMenuMe.setOnClickListener(this);
    }
}
