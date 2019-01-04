package com.example.hoong.coiple;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Hashtable;

public class Register extends AppCompatActivity {
    int i=0, Month = 0, Day = 0 ,  Year= 0;
    EditText edId,edPw,edcoupleId;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Button Birth;
    Hashtable<String, String> user = new Hashtable<String, String>();
    String stId,stPw,uid,stcoupleid,sex;
    FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
     DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //xml선언
        edId=(EditText)findViewById(R.id.idText1);
        edPw=(EditText)findViewById(R.id.passwordText1);
        edcoupleId=(EditText)findViewById(R.id.coupleId) ;
        final RadioGroup rg = (RadioGroup)findViewById(R.id.genderGroup);
        RadioButton womon=(RadioButton)findViewById(R.id.genderWomon);
        RadioButton man=(RadioButton)findViewById(R.id.genderMan);
        final Button regitButton=(Button)findViewById(R.id.registerButton2) ;
        mAuth = FirebaseAuth.getInstance();
        Birth = (Button)findViewById(R.id.BirthButton);
        Birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                this.DialogDatePicker();
            }
            //캘린더 다이얼로그
            private void DialogDatePicker(){
                Calendar c = Calendar.getInstance();
                int cyear = c.get(Calendar.YEAR);
                int cmonth = c.get(Calendar.MONTH);
                int cday = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            // onDateSet method
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date_selected = String.valueOf(monthOfYear+1)+
                                        " /"+String.valueOf(dayOfMonth)+" /"+String.valueOf(year);
                                Day = dayOfMonth;
                                Month = monthOfYear;
                                Year = year;
                                Birth.setText(date_selected);


                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(Register.this,  mDateSetListener,
                        cyear, cmonth, cday);


                alert.show();
            }

        });



        //데이터베이스 선언 및 사용

        regitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스트링으로 변환
               stId=edId.getText().toString();
               stPw=edPw.getText().toString();
               stcoupleid=edcoupleId.getText().toString();


                //유효성 검사
                if(stId.length()==0) {
                    Toast.makeText(Register.this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(stPw.length()==0){
                    Toast.makeText(Register.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (Day==0){
                    Toast.makeText(Register.this, "생년월일을 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
               if(stcoupleid.length()!=0){


            }
                else if(stcoupleid.length()==0){
                    stcoupleid="";

                }

                        switch (rg.getCheckedRadioButtonId()){
                            case R.id.genderMan:
                                sex="남";
                                break;
                            case R.id.genderWomon:
                                sex="여";
                                break;
                                default:
                                    sex="여";
                                    break;

                        }




                //헤시테이블에 데이터 저장


                Toast.makeText(getApplicationContext(),stId+stPw+uid,Toast.LENGTH_LONG).show();
                regiserUser(stId,stPw);



            }
        });


    }
    public void regiserUser(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser users = mAuth.getCurrentUser();
                            myRef = database.getReference("user").child(users.getUid());
                            user.put("ID",stId);
                            user.put("PASSWORD",stPw);
                            user.put("UID",users.getUid());
                            user.put("Sex",sex);
                            user.put("photo","");
                            user.put("CoupleId",stcoupleid);
                            user.put("BirthY",String.valueOf(Year));
                            user.put("BirthM",String.valueOf(Month));
                            user.put("BirthD",String.valueOf(Day));
                            //데이터 헤시테이블 데이터 베이스로 이동
                            myRef.setValue(user);
                            //updateUI(user);
                            Intent LoginIntent = new Intent(Register.this, LoginActivity.class);
                            Register.this.startActivity(LoginIntent);

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                        }

                        // ...
                    }
                });
    }
}
