package com.example.signuploginrealtime;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class pyquiz extends AppCompatActivity {
    private TextView questionTV, questionNumberTV;
    private Button option1btn, option2btn, option3btn, option4btn, nextBtn, submitBtn;
    private LinearLayout linearLayout1;

    final Context context = this;

    Dialog resultView;

    private int count = 0;

    ArrayList<modelquestions> quizModalArrayList;
    Random random;
    int questionAttempted = 0, currentPos, score;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_view);

        questionTV = findViewById(R.id.question_view);
        questionNumberTV = findViewById(R.id.no_of_questions_view);
        option1btn = findViewById(R.id.b1);
        option2btn = findViewById(R.id.b2);
        option3btn = findViewById(R.id.b3);
        option4btn = findViewById(R.id.b4);
        nextBtn = findViewById(R.id.next_btn);
        submitBtn = findViewById(R.id.submit_btn);
        linearLayout1 = findViewById(R.id.options_layout);
        quizModalArrayList = new ArrayList<>();
        resultView = new Dialog(context);
        random = new Random();

        // Initialize Firebase Authentication
//        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Initialize views
//        initializeViews();

        // Add Python questions
        addPythonQuestions();

        // Show the first question
        currentPos = 0;
        setDataToViews(currentPos);

        if (questionTV != null) {
        } else {
            Log.e("YourTag", "questionView is null");
        }

        for (int i = 0; i < 4; i++) {
            linearLayout1.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAns((Button) v);
                }
            });
        }

        playAnim(questionTV,0,quizModalArrayList.get(currentPos).getQuestion());

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                nextBtn.setEnabled(false);
                nextBtn.setAlpha(0.7f);
                enableOptions(true);
                resetOptionButtonBackgrounds();
                currentPos++;

                if (currentPos >= quizModalArrayList.size() - 1) {
                    nextBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                }

                count = 0;
                playAnim(questionTV, 0, quizModalArrayList.get(currentPos).getQuestion());

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRef = database.getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("scores");

                DatabaseReference pythonRef = userRef.child("PYTHON");

                if (score <= 6) {
                    Button tryAgain, certificate;
                    resultView = new Dialog(context);
                    resultView.setContentView(R.layout.result);
                    tryAgain = resultView.findViewById(R.id.tryAgainBtn);
                    certificate = resultView.findViewById(R.id.certificateBtn);
                    tryAgain.setVisibility(View.VISIBLE);
                    certificate.setVisibility(View.GONE);
                    tryAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent BG = new Intent(getApplicationContext(), pyquiz.class); //If User get 20% let him or her play again
                            startActivity(BG);
                        }
                    });

                    Objects.requireNonNull(resultView.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    resultView.show();

                } else {

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int currentTotalScore = dataSnapshot.exists() ? dataSnapshot.child("totalScore").getValue(Integer.class) : 0;
                            currentTotalScore += score;
                            userRef.child("totalScore").setValue(currentTotalScore);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(getApplicationContext(), "Failed to update total score", Toast.LENGTH_SHORT).show();
                        }
                    });

                    pythonRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int currentPythonScore = dataSnapshot.exists() ? dataSnapshot.child("pythonScore").getValue(Integer.class) : 0;
                            currentPythonScore += score;
                            pythonRef.child("pythonScore").setValue(currentPythonScore);
                            pythonRef.child("status").setValue("completed");

                            Button certificate, tryAgain;
                            resultView = new Dialog(context);
                            resultView.setContentView(R.layout.result);
                            tryAgain = resultView.findViewById(R.id.tryAgainBtn);
                            certificate = resultView.findViewById(R.id.certificateBtn);
                            certificate.setVisibility(View.VISIBLE);
                            tryAgain.setVisibility(View.GONE);

                            int finalCurrentPythonScore = currentPythonScore;
                            certificate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), certificate.class);
                                    intent.putExtra("PYTHON", "python");
                                    intent.putExtra("pythonScore", finalCurrentPythonScore);
                                    startActivity(intent);
                                }
                            });
                            Objects.requireNonNull(resultView.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            resultView.show();
                            Toast.makeText(getApplicationContext(), "You have successfully completed the test. Your score: " + score + "/" + quizModalArrayList.size(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(getApplicationContext(), "Failed to update Python score", Toast.LENGTH_SHORT).show();
                        }
                    });

                    count = 0;
                    playAnim(questionTV, 0, quizModalArrayList.get(currentPos).getQuestion());

                }
            }
        });
    }

    private void playAnim(final View view, final int value, final String data) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = quizModalArrayList.get(currentPos).getOption1();
                    } else if (count == 1) {
                        option = quizModalArrayList.get(currentPos).getOption2();
                    } else if (count == 2) {
                        option = quizModalArrayList.get(currentPos).getOption3();
                    } else if (count == 3) {
                        option = quizModalArrayList.get(currentPos).getOption4();
                    }
                    playAnim(linearLayout1.getChildAt(count), 0, option);
                    count++;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(Animator animation) {

                if (value == 0) {

                    try {
                        ((TextView) view).setText(data);
                        questionNumberTV.setText(currentPos + 1 + "/" + quizModalArrayList.size());
                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);


                    playAnim(view, 1, data);

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void checkAns(Button selectedOption) {
        enableOptions(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        String correctAnswer = quizModalArrayList.get(currentPos).getCorrectAnswer();
        String selectedAnswer = String.valueOf(quizModalArrayList.get(currentPos));

        if (selectedOption.getText().toString().equals(correctAnswer)) {
            // Correct Answer
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#14E39A")));
//            selectedOption.setBackgroundColor(getResources().getColor(R.color.green));
            score++;
        } else {
            // Wrong Answer
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2B55")));
//            selectedOption.setBackgroundColor(getResources().getColor(R.color.redd));
            Button correctOption = linearLayout1.findViewWithTag(quizModalArrayList.get(currentPos).getCorrectAnswer());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#14E39A")));
//            correctOption.setBackgroundColor(getResources().getColor(R.color.green));


//            // Find and highlight the correct answer in green
//            if (option1btn.getText().toString().equals(selectedAnswer)) {
//                option1btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7593FB")));
//            } else if (option2btn.getText().toString().equals(selectedAnswer)) {
//                option2btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7593FB")));
//            } else if (option3btn.getText().toString().equals(selectedAnswer)) {
//                option3btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7593FB")));
//            } else if (option4btn.getText().toString().equals(selectedAnswer)) {
//                option4btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7593FB")));
//            }
        }
    }

    // Method to add Python questions
    private void addPythonQuestions() {
        // Add your questions here
        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Python code?\n\nprint(5 + 3, 'Python')",
                "8 Python", "5 3 Python", "53 Python", "Error", "8 Python"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is the correct way to declare a variable in Python?",
                "variable = int", "int variable", "variable int", "variable: int", "int variable"));

        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Python code?\n\nx = 5\nprint(x)",
                "6", "5", "Error", "4", "5"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is a built-in data type in Python?",
                "Float", "float", "Real", "Integer", "float"));

        quizModalArrayList.add(new modelquestions(
                "What does the 'def' keyword mean in Python?",
                "It is used to define a class.",
                "It indicates that a method or function can be accessed without creating an instance of the class.",
                "It indicates the start of a function or method definition.",
                "None of the above",
                "It indicates the start of a function or method definition."));

        quizModalArrayList.add(new modelquestions(
                "Which data type in Python is used to store a sequence of characters?",
                "char", "string", "Character", "CharSequence", "string"));

        quizModalArrayList.add(new modelquestions(
                "What is the correct way to declare a constant variable in Python?",
                "const MAX_VALUE = 100", "MAX_VALUE = 100",
                "constant MAX_VALUE = 100", "final MAX_VALUE = 100", "MAX_VALUE = 100"));

        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Python code?\n\nstr1 = 'Hello'\nstr2 = 'Hello'\nprint(str1 == str2)",
                "True", "False", "Error", "None", "True"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is not a valid Python identifier?",
                "2variable", "_variable", "variable2", "$variable", "2variable"));

        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Python code?\n\nnumbers = [1, 2, 3, 4, 5]\nprint(len(numbers))",
                "5", "4", "6", "Error", "5"));

    }

    // Method to set data to views
    @SuppressLint("SetTextI18n")
    private void setDataToViews(int currentPos) {
        questionNumberTV.setText("Question: " + (currentPos + 1) + "/" + quizModalArrayList.size());

        questionTV.setText(quizModalArrayList.get(currentPos).getQuestion());
        option1btn.setText(quizModalArrayList.get(currentPos).getOption1());
        option2btn.setText(quizModalArrayList.get(currentPos).getOption2());
        option3btn.setText(quizModalArrayList.get(currentPos).getOption3());
        option4btn.setText(quizModalArrayList.get(currentPos).getOption4());
    }

    private void enableOptions(boolean enable) {
        for (int i = 0; i < 4; i++) {
            linearLayout1.getChildAt(i).setEnabled(enable);
            if (enable) {
                linearLayout1.getChildAt(i).setBackgroundResource(R.drawable.options_btn_bg);
            }
        }
    }

    private void resetOptionButtonBackgrounds() {
        option1btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff"))); // Reset to default color
        option2btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff"))); // Reset to default color
        option3btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff"))); // Reset to default color
        option4btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff"))); // Reset to default color
    }

    // Method to process user's answer
    private void processAnswer() {
        // Process user's answer and update score
    }

    // Method to clear selection
    private void clearSelection() {
        // Clear selection of options
    }
}