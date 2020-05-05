package com.example.recycledviewpoolexample;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycledviewpoolexample.adapters.ItemAdapter;
import com.example.recycledviewpoolexample.adapters.SubItemAdapter;
import com.example.recycledviewpoolexample.helper.utils.PDFCreationUtils;
import com.example.recycledviewpoolexample.helper.utils.PdfBitmapCache;
import com.example.recycledviewpoolexample.models.ItemModel;
import com.example.recycledviewpoolexample.models.SubItemModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RattingActivity extends AppCompatActivity {
    private static final String TAG = null;
    private boolean IS_MANY_PDF_FILE;

    /**
     * This is identify to number of pdf file. If pdf model list size > sector so we have create many file. After that we have merge all pdf file into one pdf file
     */
    private int SECTOR = 100; // Default value for one pdf file.
    private int START;
    private int END = SECTOR;
    private int NO_OF_PDF_FILE = 1;
    private int NO_OF_FILE;
    private int LIST_SIZE;
    private ProgressDialog progressDialog;

    private List<SubItemModel> pdfModels = new ArrayList<>();
    private TextView btnPdfPath;
    private Button btnSharePdfFile;
    RecyclerView recyclerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratting);
        btnSharePdfFile = findViewById(R.id.btn_share_pdf);
        btnPdfPath = findViewById(R.id.btn_pdf_path);
        recyclerItem = findViewById(R.id.rv_show_demo);
        pdfModels = SubItemModel.createDummyPdfModel();
        findViewById(R.id.btn_create_pdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(RattingActivity.this, LinearLayoutManager.VERTICAL, false);
        SubItemAdapter itemAdapter = new SubItemAdapter(buildSubItemList(), this);
        recyclerItem.setLayoutManager(layoutManager);
        recyclerItem.setAdapter(itemAdapter);
    }

    private List<SubItemModel> buildSubItemList() {
        List<SubItemModel> subItemModelList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SubItemModel subItemModel = new SubItemModel(i, "Sub ItemModel " + i, "Description " + i);
            subItemModelList.add(subItemModel);
        }
        return subItemModelList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            generatePdfReport();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        } else {
            generatePdfReport();
        }
    }

    private void generatePdfReport() {
        LIST_SIZE = pdfModels.size();
        NO_OF_FILE = LIST_SIZE / SECTOR;
        if (LIST_SIZE % SECTOR != 0) {
            NO_OF_FILE++;
        }
        if (LIST_SIZE > SECTOR) {
            IS_MANY_PDF_FILE = true;
        } else {
            END = LIST_SIZE;
        }
        createPDFFile();

    }

    private void createPDFFile() {
        // Find sub list for per pdf file data
        try {
            List<SubItemModel> pdfDataList = pdfModels.subList(START, END);
            PdfBitmapCache.clearMemory();
            PdfBitmapCache.initBitmapCache(getApplicationContext());
            final PDFCreationUtils pdfCreationUtils = new PDFCreationUtils(RattingActivity.this,
                    pdfDataList, LIST_SIZE, NO_OF_PDF_FILE);
            if (NO_OF_PDF_FILE == 1) {
                createProgressBarForPDFCreation(PDFCreationUtils.TOTAL_PROGRESS_BAR);
            }
            pdfCreationUtils.createPDF(new PDFCreationUtils.PDFCallback() {

                @Override
                public void onProgress(final int i) {
                    progressDialog.setProgress(i);
                }

                @Override
                public void onCreateEveryPdfFile() {
                    // Execute may pdf files and this is depend on NO_OF_FILE
                    if (IS_MANY_PDF_FILE) {
                        NO_OF_PDF_FILE++;
                        if (NO_OF_FILE == NO_OF_PDF_FILE - 1) {
                            progressDialog.dismiss();
                            createProgressBarForMergePDF();
                            pdfCreationUtils.downloadAndCombinePDFs();
                        } else {
                            // This is identify to manage sub list of current pdf model list data with START and END
                            START = END;
                            if (LIST_SIZE % SECTOR != 0) {
                                if (NO_OF_FILE == NO_OF_PDF_FILE) {
                                    END = (START - SECTOR) + LIST_SIZE % SECTOR;
                                }
                            }
                            END = SECTOR + END;
                            createPDFFile();
                        }
                    } else {
                        // Merge one pdf file when all file is downloaded
                        progressDialog.dismiss();
                        createProgressBarForMergePDF();
                        pdfCreationUtils.downloadAndCombinePDFs();
                    }
                }

                @Override
                public void onComplete(final String filePath) {
                    progressDialog.dismiss();
                    if (filePath != null) {
                        btnPdfPath.setVisibility(View.VISIBLE);
                        btnPdfPath.setText("PDF path : " + filePath);
                        Toast.makeText(RattingActivity.this, "pdf file " + filePath, Toast.LENGTH_LONG).show();
                        btnSharePdfFile.setVisibility(View.VISIBLE);
                        btnSharePdfFile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharePdf(filePath);
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(RattingActivity.this, "Error  " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "createPDFFile: ", e);
            e.printStackTrace();
        }
    }

    private void sharePdf(String filePath) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        ArrayList<Uri> uris = new ArrayList<>();
        File fileIn = new File(filePath);
        Uri u = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, fileIn);
        uris.add(u);
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_to)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.error_file), Toast.LENGTH_SHORT).show();
        }
    }

    private void createProgressBarForPDFCreation(int maxProgress) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("PDF file is being generating...Please wait");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(maxProgress);
        progressDialog.show();
    }

    private void createProgressBarForMergePDF() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait pages are merging  into file");
        progressDialog.show();
    }

}