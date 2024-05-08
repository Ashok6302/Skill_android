package com.example.signuploginrealtime;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
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
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class cquiz extends AppCompatActivity {
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

        quizModalArrayList = new ArrayList<>();
        random = new Random();

        // Add 10 PHP questions
        addCprogrammingQuestions();

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

//        option1btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectOption(option1btn);
//            }
//        });
//
//        option2btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectOption(option2btn);
//            }
//        });
//
//        option3btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectOption(option3btn);
//            }
//        });
//
//        option4btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectOption(option4btn);
//            }
//        });

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

                DatabaseReference cRef = userRef.child("C Programming");

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
                            Intent BG = new Intent(getApplicationContext(), cquiz.class); //If User get 20% let him or her play again
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

                    cRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int currentCProgrammingScore = dataSnapshot.exists() ? dataSnapshot.child("cProgrammingScore").getValue(Integer.class) : 0;
                            currentCProgrammingScore += score;
                            cRef.child("cProgrammingScore").setValue(currentCProgrammingScore);
                            cRef.child("status").setValue("completed");

                            Button certificate, tryAgain;
                            resultView = new Dialog(context);
                            resultView.setContentView(R.layout.result);
                            tryAgain = resultView.findViewById(R.id.tryAgainBtn);
                            certificate = resultView.findViewById(R.id.certificateBtn);
                            certificate.setVisibility(View.VISIBLE);
                            tryAgain.setVisibility(View.GONE);

                            int finalCurrentCProgrammingScore = currentCProgrammingScore;
                            certificate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), certificate.class);
                                    intent.putExtra("C_PROGRAMMING", "cProgramming");
                                    intent.putExtra("cProgrammingScore", finalCurrentCProgrammingScore);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(getApplicationContext(), "Failed to update C Programming score", Toast.LENGTH_SHORT).show();
                        }
                    });

                    count = 0;
                    playAnim(questionTV, 0, quizModalArrayList.get(currentPos).getQuestion());

                }
                Objects.requireNonNull(resultView.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                resultView.show();
                Toast.makeText(getApplicationContext(), "You have successfully completed the test. Your score: " + score + "/" + quizModalArrayList.size(), Toast.LENGTH_SHORT).show();
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

    private void addCprogrammingQuestions() {
        // Add PHP questions here
        quizModalArrayList.add(new modelquestions(
                "What is the output of the following PHP code?\n\n<?php $x = 5; $y = 10; echo $x + $y; ?>",
                "510", "15", "5 + 10", "Error", "15"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is the correct way to start a session in PHP?",
                "start_session()", "session_start()", "session()", "start()", "session_start()"));

        quizModalArrayList.add(new modelquestions(
                "What will be the output of the following PHP code?\n\n<?php $x = 5; echo ++$x; ?>",
                "5", "6", "Error", "1", "6"));

        quizModalArrayList.add(new modelquestions(
                "Which function is used to redirect a user to a new page in PHP?",
                "redirect()", "header()", "location()", "transfer()", "header()"));

        quizModalArrayList.add(new modelquestions(
                "What does PHP stand for?",
                "Personal Home Page", "Hypertext Preprocessor", "Private Home Page", "Public Home Page", "Hypertext Preprocessor"));

        quizModalArrayList.add(new modelquestions(
                "What is the correct way to end a PHP statement?",
                "Semicolon (;)", "Period (.)", "Exclamation mark (!)", "Comma (,)", "Semicolon (;)"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is used to comment in PHP?",
                "// This is a comment", "# This is a comment", "<!-- This is a comment -->", "// This is a comment //", "// This is a comment"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is NOT a superglobal variable in PHP?",
                "$_GET", "$_POST", "$_SESSION", "$_GLOBAL", "$_GLOBAL"));

        quizModalArrayList.add(new modelquestions(
                "What does the PHP function var_dump() do?",
                "Displays structured information about one or more expressions", "Dumps information about a variable", "Prints backtrace", "Displays all variables", "Displays structured information about one or more expressions"));

        quizModalArrayList.add(new modelquestions(
                "Which operator is used for concatenation in PHP?",
                "+", "&", ".", "-", "."));

        // Add more questions as needed
    }

    @SuppressLint("SetTextI18n")
    private void setDataToViews(int currentPos) {
        questionNumberTV.setText("Question: " + (currentPos + 1) + "/" + quizModalArrayList.size());

        questionTV.setText(quizModalArrayList.get(currentPos).getQuestion());
        option1btn.setText(quizModalArrayList.get(currentPos).getOption1());
        option2btn.setText(quizModalArrayList.get(currentPos).getOption2());
        option3btn.setText(quizModalArrayList.get(currentPos).getOption3());
        option4btn.setText(quizModalArrayList.get(currentPos).getOption4());
    }

//    private void processAnswer() {
//        // Increment question attempted count
//        questionAttempted++;
//
//        // Check if the correct option is selected
//        if (option1btn.isSelected() && option1btn.getText().toString().equals(quizModalArrayList.get(currentPos).getCorrectAnswer())) {
//            currentscore++;
//        } else if (option2btn.isSelected() && option2btn.getText().toString().equals(quizModalArrayList.get(currentPos).getCorrectAnswer())) {
//            currentscore++;
//        } else if (option3btn.isSelected() && option3btn.getText().toString().equals(quizModalArrayList.get(currentPos).getCorrectAnswer())) {
//            currentscore++;
//        } else if (option4btn.isSelected() && option4btn.getText().toString().equals(quizModalArrayList.get(currentPos).getCorrectAnswer())) {
//            currentscore++;
//        }
//    }

//    private void showScore() {
//        // Display the score
//        Toast.makeText(getApplicationContext(), "Your score: " + currentscore + "/" + quizModalArrayList.size(), Toast.LENGTH_SHORT).show();
//
//        // Add the username and score to the scoreboard table in Firebase
//        DatabaseReference scoreboardRef = FirebaseDatabase.getInstance().getReference("scoreboard");
//        String username = ""; // Get the username from wherever it's stored in your app
//        ScoreboardEntry entry = new ScoreboardEntry(username, currentscore);
//        scoreboardRef.child(username).setValue(entry);
//    }


    private void selectOption(Button optionButton) {
        clearSelection();
        optionButton.setSelected(true);
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

    private void clearSelection() {
        option1btn.setSelected(false);
        option2btn.setSelected(false);
        option3btn.setSelected(false);
        option4btn.setSelected(false);
    }
}