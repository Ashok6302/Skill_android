package com.example.signuploginrealtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class certificate extends AppCompatActivity {

    TextView certificateCategory;
    Button btnDownloadCertificate;
    ConstraintLayout certificateCL;
    private String Course;
    private int phpScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

//        certificateCategory = findViewById(R.id.certificateCategory);
        btnDownloadCertificate = findViewById(R.id.btnDownloadCertificate);
        certificateCL = findViewById(R.id.certificateCL);

        Intent intent = getIntent();
        if (intent.hasExtra("PHP")){
            Course = intent.getStringExtra("PHP");
        }
        if (intent.hasExtra("phpScore")){
            phpScore = intent.getIntExtra("phpScore",0);
        }
        if (intent.hasExtra("LOGICAL")){
            Course = intent.getStringExtra("LOGICAL");
        }
        if (intent.hasExtra("REASONING")){
            Course = intent.getStringExtra("REASONING");
        }
        if (intent.hasExtra("APTITUDE")){
            Course = intent.getStringExtra("APTITUDE");
        }
        if (intent.hasExtra("APTITUDE")){
            Course = intent.getStringExtra("APTITUDE");
        }

        if (Objects.equals(Course, "php")) {
            certificateCategory.setText("PHP Certificate");
        } else if (Objects.equals(Course, "logical")) {
            certificateCategory.setText("Logical Beginner Certificate");
        } else if (Objects.equals(Course, "reasoning")) {
            certificateCategory.setText("Reasoning Beginner Certificate");
        } else if (Objects.equals(Course, "aptitude")){
            certificateCategory.setText("Aptitude Beginner Certificate");
        } else if (Objects.equals(Course, "aptitude")){
            certificateCategory.setText("Aptitude Beginner Certificate");
        }

        btnDownloadCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(Course, "php")) {
                    createAndSavePdf("PHP Certificate.pdf");
                } else if (Objects.equals(Course, "logical")) {
                    createAndSavePdf("LogicalBeginnerCertificate.pdf");
                } else if (Objects.equals(Course, "reasoning")) {
                    createAndSavePdf("ReasoningBeginnerCertificate.pdf");
                } else if (Objects.equals(Course, "aptitude")){
                    createAndSavePdf("AptitudeBeginnerCertificate.pdf");
                } else if (Objects.equals(Course, "aptitude")){
                    createAndSavePdf("AptitudeBeginnerCertificate.pdf");
                }
            }
        });
    }

    private void createAndSavePdf(String pdfName) {
        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Create a PageInfo
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(certificateCL.getWidth(), certificateCL.getHeight(), 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Draw the view on the page
        certificateCL.draw(page.getCanvas());

        // Finish the page
        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // Create a file to save the PDF
        File file = new File(downloadsDir, pdfName);

        try {
            // Write the PDF content to the file
            document.writeTo(new FileOutputStream(file));

            // Close the document
            document.close();

            // Show a toast indicating the PDF is saved
            Toast.makeText(getApplicationContext(), "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            Log.e("path",file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}