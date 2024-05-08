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

public class javaquiz extends AppCompatActivity {
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

        // Add 10 Python programming questions
        addJavaQuestions();

        // Show the first question
        currentPos = 0;
        setDataToViews(currentPos);

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

                DatabaseReference javaRef = userRef.child("JAVA");

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
                            Intent BG = new Intent(getApplicationContext(), javaquiz.class); //If User get 20% let him or her play again
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

                    javaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int currentJavaScore = dataSnapshot.exists() ? dataSnapshot.child("javaScore").getValue(Integer.class) : 0;
                            currentJavaScore += score;
                            javaRef.child("javaScore").setValue(currentJavaScore);
                            javaRef.child("status").setValue("completed");

                            Button certificate, tryAgain;
                            resultView = new Dialog(context);
                            resultView.setContentView(R.layout.result);
                            tryAgain = resultView.findViewById(R.id.tryAgainBtn);
                            certificate = resultView.findViewById(R.id.certificateBtn);
                            certificate.setVisibility(View.VISIBLE);
                            tryAgain.setVisibility(View.GONE);

                            int finalCurrentJavaScore = currentJavaScore;
                            certificate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), certificate.class);
                                    intent.putExtra("JAVA", "java");
                                    intent.putExtra("javaScore", finalCurrentJavaScore);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(getApplicationContext(), "Failed to update Java score", Toast.LENGTH_SHORT).show();
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

    private void addJavaQuestions() {
        // Add Java programming questions here
        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Java code?\n\nSystem.out.println(5 + 3 + \"Java\")",
                "8Java", "5 3 Java", "53Java", "Error", "8Java"));

        quizModalArrayList.add(new modelquestions(
                "What is the correct way to declare a variable in Java?",
                "variable int;", "int variable;", "integer variable;", "variable = int;", "int variable;"));

        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Java code?\n\nint x = 5;\nSystem.out.println(x++);",
                "6", "5", "Error", "4", "5"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is a wrapper class in Java?",
                "Float", "float", "int", "Integer", "Integer"));

        quizModalArrayList.add(new modelquestions(
                "What does the 'static' keyword mean in Java?",
                "It indicates that a method or variable belongs to the class, not an instance of the class.",
                "It means the method or variable is constant.",
                "It indicates that a method can be accessed without creating an instance of the class.",
                "All of the above",
                "All of the above"));

        quizModalArrayList.add(new modelquestions(
                "Which data type in Java is used to store a single 16-bit Unicode character?",
                "char", "string", "Character", "CharSequence", "char"));

        quizModalArrayList.add(new modelquestions(
                "What is the correct way to declare a constant variable in Java?",
                "constant int MAX_VALUE = 100;", "int constant MAX_VALUE = 100;",
                "final int MAX_VALUE = 100;", "int final MAX_VALUE = 100;", "final int MAX_VALUE = 100;"));

        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Java code?\n\nString str1 = \"Hello\";\nString str2 = new String(\"Hello\");\nSystem.out.println(str1 == str2);",
                "true", "false", "Error", "null", "false"));

        quizModalArrayList.add(new modelquestions(
                "Which of the following is not a valid Java identifier?",
                "2variable", "_variable", "variable2", "$variable", "2variable"));

        quizModalArrayList.add(new modelquestions(
                "What is the output of the following Java code?\n\nint[] numbers = {1, 2, 3, 4, 5};\nSystem.out.println(numbers.length);",
                "5", "4", "6", "Error", "5"));

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
//    }

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

    private void selectOption(Button optionButton) {
        clearSelection();
        optionButton.setSelected(true);
    }


    private void clearSelection() {
        option1btn.setSelected(false);
        option2btn.setSelected(false);
        option3btn.setSelected(false);
        option4btn.setSelected(false);
    }
}