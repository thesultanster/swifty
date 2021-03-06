package com.example.haasith.parse2.profile;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.haasith.parse2.booking.Booking;
import com.example.haasith.parse2.current_session.CurrentSession;
import com.example.haasith.parse2.R;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Profile extends AppCompatActivity implements ConfirmPaymentCommunicator {

    TextView mtutorUsername;
    TextView tutorFirstname;
    TextView college;
    TextView degree;
    TextView total;
    TextView homeworkPrice;
    TextView testPrice;
    TextView crashPrice;
    Button confirmPayment;
    String tutorFirst;
    String tutorLast;
    String tutorUsername;
    String tutorId;
    String tutorCollege;
    String tutorDegree;
    int tutorHomeowrk;
    int tutorTest;
    int tutorCrash;
    CheckBox homework;
    CheckBox test;
    CheckBox crash;

    double rating;
    int sum = 0;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tutorUsername = extras.getString("username");
            tutorId = extras.getString("tutorId");
            tutorFirst = extras.getString("firstname");
            tutorLast = extras.getString("lastname");
            rating = extras.getDouble("rating");
            tutorCollege = extras.getString("college");
            tutorDegree = extras.getString("degree");
            tutorHomeowrk = extras.getInt("tutorHomework");
            tutorTest = extras.getInt("tutorTest");
            tutorCrash = extras.getInt("tutorCrash");
        }


        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getBackground().setAlpha(300);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(0xFFffffff);
        collapsingToolbarLayout.setTitle(tutorFirst + " " + tutorLast);
        collapsingToolbarLayout.setExpandedTitleColor(0xFFffffff);

        mtutorUsername = (TextView) findViewById(R.id.username);
        college = (TextView) findViewById(R.id.college);
        degree = (TextView) findViewById(R.id.degree);
        total = (TextView) findViewById(R.id.total);
        homeworkPrice = (TextView) findViewById(R.id.homeworkPrice);
        testPrice = (TextView) findViewById(R.id.testReviewPrice);
        crashPrice = (TextView) findViewById(R.id.crashCoursePrice);
        confirmPayment = (Button) findViewById(R.id.confirmpayment);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        homework = (CheckBox) findViewById(R.id.homeworkAssignment);
        test = (CheckBox) findViewById(R.id.testReview);
        crash = (CheckBox) findViewById(R.id.crashCourse);

        homeworkPrice.setText("$"+tutorHomeowrk);
        testPrice.setText("$"+tutorTest);
        crashPrice.setText("$" + tutorCrash);

        total.setText("$" + sum);



        homework.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sum+=tutorHomeowrk;
                    //homeworkT = false;
                    //homework.toggle();
                } else {
                    sum-=tutorHomeowrk;
                    //homeworkT = true;
                }

                total.setText("$"+sum);
            }
        });

        test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sum+=tutorTest;
                    //testT = false;
                    //test.toggle();
                } else {
                    sum-=tutorTest;
                    //testT = true;
                }

                total.setText("$"+sum);

            }
        });

        crash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sum+=tutorCrash;
                    //crashT = false;
                    //crash.toggle();
                } else {
                    sum-=tutorCrash;
                    //crashT = true;
                }

                total.setText("$"+sum);

            }
        });



        ratingBar.setRating((float) rating);
        mtutorUsername.setText(tutorUsername);
        college.setText(tutorCollege);
        degree.setText(tutorDegree);

        confirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // No free transactions
                if(sum > 0) {
                    FragmentManager fragmentManager = getFragmentManager();
                    ConfirmPaymentDialog myDialog = new ConfirmPaymentDialog().newInstance(sum);
                    myDialog.show(fragmentManager, "Confirm Payment");
                }
            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onDialogPayment() {

        final ParseObject session = new ParseObject("TutorSession");
        session.put("clientId", ParseUser.getCurrentUser().getObjectId());
        session.put("client", ParseUser.getCurrentUser());
        session.put("tutorId", tutorId);
        session.put("clientRelease", false);
        session.put("tutorRelease", false);
        session.put("isCompleted", false);
        session.put("tutorAccepted", false);
        session.put("tutorRejected", false);
        session.put("meetingId", "0");
        // TODO: This is defaulted to inPerson
        session.put("sessionType", "inPerson");
        session.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {


                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("userId", tutorId);
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);
                push.setMessage(ParseUser.getCurrentUser().getString("firstname") + " Sent A Tutoring Request!");
                push.sendInBackground();


                // MY_PREFS_NAME - a static String variable like:
                //public static final String MY_PREFS_NAME = "MyPrefsFile";
                SharedPreferences.Editor editor = getSharedPreferences("CurrentSessionDetails", MODE_PRIVATE).edit();
                editor.putString("clientId", ParseUser.getCurrentUser().getObjectId());
                editor.putString("tutorId", tutorId);
                editor.putString("sessionId", session.getObjectId());
                // TODO: This is defaulted to InPerson
                editor.putString("sessionType", "inPerson");
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), CurrentSession.class);
                intent.putExtra("tutorHomework", tutorHomeowrk);
                intent.putExtra("tutorTest", tutorTest);
                intent.putExtra("tutorCrash",tutorCrash);
                startActivity(intent);
            }
        });


    }
}
