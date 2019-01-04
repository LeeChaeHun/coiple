package com.example.hoong.coiple;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

public class First extends AppCompatActivity {


    final int REQ_CODE_SELECT_IMAGE=100;
    int i=0,DateYear = 0 , DateMonth = 0 , DateDay = 0;
    private StorageReference mStorageRef;
    Bitmap image_bitmap;
    ProgressBar Pbar1,Pbar2;
    Button Manbirth;
    String uid,id,Cuid,stPhoto,stPhoto2,MyCouple;
    String TAG = getClass().getSimpleName();
    RecyclerView mRecyvlerView;
    LinearLayoutManager mLayoutManager;
    ArrayList<RecyclerItem> mItems;
    RecyclerView.Adapter mAdapter;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        SharedPreferences pref = getSharedPreferences("test", Activity.MODE_PRIVATE);
        final ImageButton ManImage = (ImageButton)findViewById(R.id.ManImage);
        final ImageButton WomonImage = (ImageButton)findViewById(R.id.WomonImage);
        SharedPreferences prefs =getSharedPreferences("test",Activity.MODE_PRIVATE);
        String DateDay1 = prefs.getString("Date", "1"); //키값, 디폴트값
        final String ManBirth1 = prefs.getString("ManBirth","1");
        String WomonBirth1=prefs.getString("WomonBirth","1");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Pbar1 = (ProgressBar)findViewById(R.id.progressBar1);
        Pbar2 = (ProgressBar)findViewById(R.id.progressBar2);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database.getReference();
        Manbirth = (Button)findViewById(R.id.ManBirth);





        mRecyvlerView = (RecyclerView)findViewById(R.id.re);
        mRecyvlerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyvlerView.setLayoutManager(mLayoutManager);
        mItems = new ArrayList<>();
        mAdapter = new RecyclerAdapter(mItems);
        mRecyvlerView.setAdapter(mAdapter);

        DateYear=pref.getInt("Datey",1);
        DateMonth=pref.getInt("Datem",1);
        DateDay=pref.getInt("Dated",1);
        Calendar cal = new GregorianCalendar(
                DateYear,DateMonth,DateDay
        );
        cal.add(Calendar.DATE,49);
        final SimpleDateFormat cusDate = new SimpleDateFormat("yyyyMMdd");

        Log.d(TAG,cusDate.format(cal.getTime()));


        mItems.add(new RecyclerItem(cusDate.format(cal.getTime())));





        //D-day 계산



                //사진 파이어베이스에서 가져오기
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyCouple = dataSnapshot.child("user").child(uid).child("CoupleId").getValue().toString();
            Toast.makeText(First.this,"1 "+MyCouple,Toast.LENGTH_LONG);
                Query q = myref.child("user").orderByChild("ID").equalTo(MyCouple);
                Log.d(TAG,MyCouple+q.toString());
                if (!MyCouple.isEmpty()) {
                //커플 사진 불러오기

                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d(TAG,"+++++++++"+dataSnapshot.getValue().toString().substring(1,29));
                        Cuid=dataSnapshot.getValue().toString().substring(1,29);
                        Log.d(TAG,     dataSnapshot.child(Cuid).child("photo").getValue().toString());
                        stPhoto2 = dataSnapshot.child(Cuid).child("photo").getValue().toString();

                        if(TextUtils.isEmpty(stPhoto2)){

                        }else {
                            Pbar2.setVisibility(View.VISIBLE);
                            Picasso.with(First.this).load(stPhoto2).fit().centerInside().into(WomonImage, new Callback.EmptyCallback() {
                                @Override
                                public void onSuccess() {
                                    // Index 0 is the image view.
                                    Pbar2.setVisibility(View.GONE);

                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });}
                else{
                    //커플이 없을때 사진 불러오기
                    SharedPreferences pref1 = getSharedPreferences("image", MODE_PRIVATE);
                    String image2 =  pref1.getString("imagestrings2", "1");
                    Bitmap bitmap2 = StringToBitMap(image2);
                    RoundedBitmapDrawable result2 = RoundedBitmapDrawableFactory.create(getResources(), bitmap2);
                    result2.setCircular(true);
                    WomonImage.setImageDrawable(result2);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Read from the database

        //해쉬테이블 이용
        myref.child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again

                stPhoto = dataSnapshot.child("photo").getValue().toString();

                if(TextUtils.isEmpty(stPhoto)){

                }else {
                    Pbar1.setVisibility(View.VISIBLE);
                    Picasso.with(First.this).load(stPhoto).fit().centerInside().into(ManImage, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            // Index 0 is the image view.
                            Pbar1.setVisibility(View.GONE);

                        }
                    });
                }

                }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
        //해쉬 맵 이용 (한가지 테이블만 추가)
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue().toString();
                stPhoto = dataSnapshot.child(uid).child("photo").getValue().toString();
               Log.d(TAG,MyCouple);
                if(TextUtils.isEmpty(stPhoto)){

                }else{
                    Pbar1.setVisibility(View.VISIBLE);
                    Picasso.with(First.this).load(stPhoto).fit().centerInside().into(ManImage, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            // Index 0 is the image view.
                            Pbar1.setVisibility(View.GONE);


                        }
                    });
                }


            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //커플 데이터 베이스 받아오기



        final Button Date1= (Button)findViewById(R.id.date);
        //사귀는 날짜 온클릭 메소드
        Date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { this.DialogDatePicker();
                //캘린더 다이얼로그

            }
            private void DialogDatePicker(){
                Calendar c = Calendar.getInstance();
                int cyear = c.get(Calendar.YEAR);
                int cmonth = c.get(Calendar.MONTH);
                int cday = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog.OnDateSetListener mDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            // onDateSet method
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date_selected =String.valueOf(year)+ String.valueOf(monthOfYear+1)
                                        +String.valueOf(dayOfMonth);

                                Manbirth.setText(date_selected);




                                SharedPreferences pref =getSharedPreferences("test", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("Datey",year );
                                editor.putInt("Datem",monthOfYear );
                                editor.putInt("Dated",dayOfMonth );
                                DateYear=year;
                                DateMonth=monthOfYear;
                                DateDay=dayOfMonth;

                                editor.commit();
                                Calendar cal2 = new GregorianCalendar(
                                        DateYear,DateMonth,DateDay
                                );
                                cal2.add(Calendar.DATE,49);
                                final SimpleDateFormat cusDate = new SimpleDateFormat("yyyyMMdd");

                                mItems.add(new RecyclerItem(cusDate.format(cal2.getTime())));








                            }
                        };
                DatePickerDialog alert = new DatePickerDialog(First.this,  mDateSetListener,
                        cyear, cmonth, cday);



                alert.show();
            }
        });




        ManImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=1;

// 사진 갤러리 호출
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });
        WomonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=2;

// 사진 갤러리 호출
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });}

    private Bitmap StringToBitMap(String encodedString) {
        try{

            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);

            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bitmap;

        }catch(Exception e){

            e.getMessage();

            return null;

        }
    }




    //사진 저장

    private void setRoundedprofileImage(Bitmap bitmap){
        RoundedBitmapDrawable result = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        result.setCircular(true);

        if(i==2){
            ImageButton WomonImage = (ImageButton) findViewById(R.id.WomonImage);
            WomonImage.setImageDrawable(result);

            String image = BitmapToString(bitmap);
            SharedPreferences pref = getSharedPreferences("image", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("imagestrings2", image);
            editor.commit();


        }

    }

    private String BitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();

        String temp= Base64.encodeToString(b, Base64.DEFAULT);

        return temp;
    }




// 선택된 이미지 가져오기

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    setRoundedprofileImage(image_bitmap);
                    if(i==1) {
                        uploadImage();
                    }
                    //배치해놓은 ImageView에 set

                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();

                }

                catch (FileNotFoundException e) { 		e.printStackTrace(); 			}

                catch (IOException e)                 {		e.printStackTrace(); 			}

                catch (Exception e)		         {             e.printStackTrace();			}

            }

        }

    }



// 선택된 이미지 파일명 가져오기

    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }


    //사진 업로드
    public void uploadImage(){
        StorageReference riversRef = mStorageRef.child("users").child(uid).child("Profile"+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...





                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String photoUri = String.valueOf(downloadUrl);
                Log.d("uri",photoUri);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
               DatabaseReference myRef = database.getReference("user");
                //파이어베이스 사용자 데이터에 사진 추가하기
                Map<String,Object>taskMap = new HashMap<String,Object>();
                taskMap.put("photo",photoUri);
                myRef.child(uid).updateChildren(taskMap);
                Toast.makeText(First.this,uid,Toast.LENGTH_LONG).show();



            }
        });
    }

    //디데이 계산기
    public void Dday(Calendar c){
        c.add(Calendar.DATE,50);
        Log.d(TAG,"기념일로 부터 50일째 되는 날 : "+String.valueOf(c.get(Calendar.YEAR)+"year"+c.get(Calendar.MONTH)+"day"+c.get(Calendar.DAY_OF_MONTH)));
        c.add(Calendar.DATE,100);
        Log.d(TAG,"기념일로 부터 200일째 되는 날 : "+String.valueOf(c.get(Calendar.YEAR)+"year"+c.get(Calendar.MONTH)+"day"+c.get(Calendar.DAY_OF_MONTH)));
        c.add(Calendar.DATE,200);
        Log.d(TAG,"기념일로 부터 200일째 되는 날 : "+String.valueOf(c.get(Calendar.YEAR)+"year"+c.get(Calendar.MONTH)+"day"+c.get(Calendar.DAY_OF_MONTH)));
        c.add(Calendar.DATE,300);
        Log.d(TAG,"기념일로 부터 300일째 되는 날 : "+String.valueOf(c.get(Calendar.YEAR)+"year"+c.get(Calendar.MONTH)+"day"+c.get(Calendar.DAY_OF_MONTH)));
        c.add(Calendar.DATE,400);
        Log.d(TAG,"기념일로 부터 400일째 되는 날 : "+String.valueOf(c.get(Calendar.YEAR)+"year"+c.get(Calendar.MONTH)+"day"+c.get(Calendar.DAY_OF_MONTH)));
        c.add(Calendar.DATE,500);
        Log.d(TAG,"기념일로 부터 500일째 되는 날 : "+String.valueOf(c.get(Calendar.YEAR)+"year"+c.get(Calendar.MONTH)+"day"+c.get(Calendar.DAY_OF_MONTH)));
        c.add(Calendar.DATE,600);
        Log.d(TAG,"기념일로 부터 600일째 되는 날 : "+String.valueOf(c.get(Calendar.YEAR)+"year"+c.get(Calendar.MONTH)+"day"+c.get(Calendar.DAY_OF_MONTH)));
    }



}
