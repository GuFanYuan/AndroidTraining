package com.example.gufan.databasedemo;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private EditText edt_Name,edt_Age,edt_Numbering;

    private RadioGroup genderRadioGp;
    private RadioButton RadioBtnBoy;
    private ListView stduList;
    private String genderStr = "男";

    private SQLiteDatabase db;
    SQLiteOpenHelper helper;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void createDB(){
        String path = Environment.getExternalStorageDirectory() + "/stu.db";
        helper = new SQLiteOpenHelper(this, path, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase Sqlitedb) {
                String sql = "create table info_tb (_id integer primary key autoincrement," +
                        "name varhcar(20)," +
                        "age integer, "+
                        "gender varhcar(4) )";
                Sqlitedb.execSQL(sql);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Toast.makeText(MainActivity.this, "update the db", Toast.LENGTH_SHORT).show();
            }
        };
        db = helper.getReadableDatabase();
    }

    private void init(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }else{
            createDB();
        }
        edt_Name = findViewById(R.id.edt_name);
        edt_Age = findViewById(R.id.edt_age);
        edt_Numbering = findViewById(R.id.edt_Numbering);
        genderRadioGp = findViewById(R.id.radioGender);
        RadioBtnBoy = findViewById(R.id.radioBtn_boy);
        genderRadioGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioBtn_boy){
                    genderStr = "男";
                }else{
                    genderStr = "女";
                }
            }
        });
        stduList = findViewById(R.id.stdu_list);
    }

    public void operate(View view){

        String nameStr = String.valueOf(edt_Name.getText());
        String ageStr = String.valueOf(edt_Age.getText());
        String idStr = String.valueOf(edt_Numbering.getText());

        switch (view.getId()){
            case R.id.btn_add:
                ContentValues values = new ContentValues();

                values.put("name",nameStr);
                values.put("age",ageStr);
                values.put("gender",genderStr);

                long id = db.insert("info_tb",null,values);
                Toast.makeText(this,"添加成功，新学员学号是：" + id,Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_del:
                int count = db.delete("info_tb","_id=?",new String[]{idStr});
                if(count > 0) {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update:
                ContentValues values2 = new ContentValues();
                //update info_tb set 列1=xx , 列2=xxx where 列名 = 值
                values2.put("name",nameStr);
                values2.put("age",ageStr);
                values2.put("gender",genderStr);
                int count2 = db.update("info_tb",values2,"_id=?",new String[]{idStr});
                if(count2 > 0) {
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_serach:
                Cursor c = db.query("info_tb",null,null,null,null,null,null);
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this, R.layout.item,c,
                        new String[]{"_id","name","age","gender"},
                        new int[]{R.id.tv_id,R.id.tv_name,R.id.tv_age,R.id.tv_gender});
                stduList.setAdapter(adapter);
                break;
        }
        edt_Name.setText("");
        edt_Age.setText("");
        edt_Numbering.setText("");
        RadioBtnBoy.setChecked(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                createDB();
            } else
            {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
