package ort.techtown.share_calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddActivity extends AppCompatActivity {

    ImageButton btn_addInfo;
    EditText edt_toDo;
    TextView tv_addDay;
    CheckBox cb_isSecret;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn_addInfo = (ImageButton) findViewById(R.id.btn_addInfo);
        edt_toDo = (EditText) findViewById(R.id.edt_addTodo);
        tv_addDay = (TextView) findViewById(R.id.tv_addDay);
        cb_isSecret = (CheckBox) findViewById(R.id.cb_isSecret);
        tv_addDay.setText(CalendarUtil.today.toString());

        int pYear = CalendarUtil.today.getYear();
        int pMonth = CalendarUtil.today.getMonthValue();
        int pDay = CalendarUtil.today.getDayOfMonth();

        Log.d("AddActivity_day", String.valueOf(pYear)+String.valueOf(pMonth)+String.valueOf(pDay) );

        tv_addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                Log.d("AddActivity_day", String.valueOf(year)+String.valueOf(month)+String.valueOf(day) );
                                month = month + 1;
                                String date;
                                if(month < 10){
                                    date = year + "-0" + month + "-" + day;
                                }else{
                                    date = year + "-" + month + "-" + day;
                                }
                                tv_addDay.setText(date);
                            }
                        },pYear,pMonth-1,pDay );
                //달력에서 0이 1월이니까 지금 달 하려면 -1 해야 해당 달이 나온다
                datePickerDialog.show(); // 달력 호출
            }
        });

        btn_addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toDo = edt_toDo.getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate pDate = LocalDate.parse(tv_addDay.getText().toString(),formatter);
                boolean isSecret = cb_isSecret.isChecked(); //체크 되어있으면 비공개

                //upload 하면 되지롱

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_left_in,R.anim.anim_left_out);
    }
}