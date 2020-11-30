package com.example.group_7_proj;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.group_7_proj.DashboardActivity;
import com.example.group_7_proj.EditPostActivity;
import com.example.group_7_proj.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptedJobsActivity extends AppCompatActivity {
    Button historyBackToMainBtn;
    DatabaseReference reff2;
    long maxPost = 0;
    String userNumber = "";
    public static long jobID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceptedjobs);

        Intent callerIntent = getIntent();
        userNumber = callerIntent.getStringExtra("User");

        reff2 = FirebaseDatabase.getInstance().getReference().child("jobPostTypeTest");
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxPost = (snapshot.getChildrenCount());
                System.out.println("****************************");
                System.out.println(maxPost);
                System.out.println("****************************");
                LinearLayout myLayout = (LinearLayout) findViewById(R.id.layoutdisplay);

                // Iterarate through all the job posts and render only those that are user created
                for (jobID = 1; jobID < maxPost + 1; jobID++) {
                        if (snapshot.hasChild("JOBPOST-" + jobID)) {


                    String trigger = snapshot.child("JOBPOST-" + jobID).child("employeeID").getValue().toString();
                    final String paymentStatus = snapshot.child("JOBPOST-" + jobID).child("completionStatus").getValue().toString();


                    // If the current user made the post, render the job, else don't
                    if (trigger.equals(userNumber)) {

                        final long tempJobID = jobID;
                        // Get values of the database fields
                        String employerName = snapshot.child("JOBPOST-" + jobID).child("employerName").getValue().toString();
                        String jobDetails = snapshot.child("JOBPOST-" + jobID).child("jobDetails").getValue().toString();
                        String jobTitle = snapshot.child("JOBPOST-" + jobID).child("jobTitle").getValue().toString();
                        String jobType = snapshot.child("JOBPOST-" + jobID).child("jobType").getValue().toString();
                        String salary = snapshot.child("JOBPOST-" + jobID).child("salary").getValue().toString();

                        // String completionStatus = snapshot.child("JOBPOST-" + jobID).child("completionStatus").getValue().toString();

                        // Create the text views
                        final TextView jobIDTextview = new TextView(getApplicationContext());
                        final TextView jobTitleTextview = new TextView(getApplicationContext());
                        final TextView employerNameTextview = new TextView(getApplicationContext());
                        final TextView jobTypeTextview = new TextView(getApplicationContext());
                        final TextView jobDetailsTextview = new TextView(getApplicationContext());
                        final TextView salaryTextview = new TextView(getApplicationContext());

                        // Set teh values that were received from the dabase to the text views
                        jobTitleTextview.setText(jobTitle);
                        jobTitleTextview.setTextSize(30);
                        jobIDTextview.setText("Job ID: " + jobID);
                        employerNameTextview.setText("Employer Name: " + employerName);
                        jobTypeTextview.setText("Job Type: " + jobType);
                        jobDetailsTextview.setText("Details: " + jobDetails);
                        salaryTextview.setText("Salary: " + salary + "\n");

                        // Add those text views to the layout so the user can see them
                        myLayout.addView(jobTitleTextview);
                        myLayout.addView(jobIDTextview);
                        myLayout.addView(jobTypeTextview);
                        myLayout.addView(employerNameTextview);
                        myLayout.addView(jobDetailsTextview);
                        myLayout.addView(salaryTextview);

                        // Creating the edit button for each post
                        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        final Button completeBtn = new Button(getApplicationContext());
                        // if the status is completed, then it should be shown as completed and not clickable
                        if (paymentStatus.equals("Completed")) {
                            completeBtn.setText("Job is completed");
                            completeBtn.setBackgroundColor(Color.rgb(0, 255, 0));

                            LinearLayout editLayout = (LinearLayout) findViewById(R.id.layoutdisplay);
                            editLayout.addView(completeBtn, editParams);

                        } else {


                            completeBtn.setText("Mark the Jobs as Completed ");
                            completeBtn.setBackgroundColor(Color.rgb(0, 191, 255));
                            int editJobID = (int) jobID;
                            //final int editJobIDMinusOne = editJobID;
                            completeBtn.setId(editJobID);
                            //final int editID_ = completeBtn.getId();


                            LinearLayout editLayout = (LinearLayout) findViewById(R.id.layoutdisplay);
                            editLayout.addView(completeBtn, editParams);

                            //When cancel button is clicked, send a bundleof info back to editpostactivity to reload the page
                            completeBtn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {

                                   new AlertDialog.Builder(AcceptedJobsActivity.this)
                                           .setTitle("Confirm")
                                           .setMessage("Mark the job as completed?")

                                           // Specifying a listener allows you to take an action before dismissing the dialog.
                                           // The dialog is automatically dismissed when a dialog button is clicked.
                                           .setPositiveButton(Dialog.BUTTON_POSITIVE, new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog, int which) {
                                                   // Continue with delete operation
                                                   reff2.child("JOBPOST-" + tempJobID).child("completionStatus").setValue("Completed");
                                                   completeBtn.setText("Job is completed");
                                                   completeBtn.setBackgroundColor(Color.rgb(0, 255, 0));
                                                   // send notification to employer





                                                   setContentView(R.layout.acceptedjobs);
                                               }
                                           })
                                           // A null listener allows the button to dismiss the dialog and take no further action.
                                           .setNegativeButton(Dialog.BUTTON_NEGATIVE, null)
                                           .setIcon(android.R.drawable.ic_dialog_alert)
                                           .show();
                                    }
                                }
                            );
                        }
                    }
                }
                    else{
                            jobID++;
                    }
            }

                // Creating a back to dashboard button at the bottom of all the posts
                historyBackToMainBtn = findViewById(R.id.historyBackToDBBtn);
                historyBackToMainBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.putExtra("User", userNumber);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w( "loadPost:onCancelled", error.toException());
            }
        });





    }

}

