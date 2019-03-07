package com.tuespotsolutions.schoolstudy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityPDF extends AppCompatActivity {
    @BindView(R.id.pdfView) PDFView pdfView;
    private String fileName = "1697_kebo114.pdf";
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpdf);
         dialog = new ProgressDialog(MainActivityPDF.this);
        dialog.setMessage("Please wait");

        dialog.show();
        Bundle extras = getIntent().getExtras();
        String userName;

        if (extras != null) {
            fileName = extras.getString("url");
            // and get whatever type user account id is
        }


        ButterKnife.bind(this);

        //New instance of PDFHelper
        new PDFHelper(this, fileName, new Callable<Void>() {
            @Override
            public Void call() {
                //Callable function if download is successful
                showPDF();
                return null;
            }
        }, new Callable<Void>() {
            @Override
            public Void call() {
                //Callable function if download isn't successful
                showError();
                return null;
            }
        });
    }

    public void showPDF(){
        //Getting the saved PDF
        File file = new File(this.getExternalFilesDir("pdfs") + File.separator + fileName);
        //Loading the PDF
        dialog.dismiss();
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    public void showError(){
        Toast.makeText(this, "Error downloading ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Deleting the PDF that was saved
        new File(this.getExternalFilesDir("pdfs")
                + File.separator + fileName).delete();
    }
}