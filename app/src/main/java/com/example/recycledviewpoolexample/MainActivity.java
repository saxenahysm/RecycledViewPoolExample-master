package com.example.recycledviewpoolexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recycledviewpoolexample.adapters.ComboAdapter;
import com.example.recycledviewpoolexample.adapters.ItemAdapter;
import com.example.recycledviewpoolexample.helper.SwipeItemTouchHelper;
import com.example.recycledviewpoolexample.helper.utils.PDFCreationUtils;
import com.example.recycledviewpoolexample.helper.utils.PdfBitmapCache;
import com.example.recycledviewpoolexample.models.ComboModel;
import com.example.recycledviewpoolexample.models.ItemModel;
import com.example.recycledviewpoolexample.models.SubItemModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = null;
    String[] categories = {"All", "MenuType1", "MenuType2", "MenuType3", "MenuType5", "MenuType6", "MenuType7", "MenuType8"};
    Spinner mySpinner;
    RecyclerView recyclerItem, recyclerCombo;
    ItemAdapter itemAdapter = new ItemAdapter(buildItemList(), this);
    ItemTouchHelper mItemTouchHelper;
    List<ItemModel> itemModelList = new ArrayList<>();
    List<SubItemModel> subItemModels = new ArrayList<>();
    List<ComboModel> comboModelList = new ArrayList<>();
    Button buttonPdf, buttonAlert, buttonCustomdialog, btnSharePdfFile;
    boolean doubleBackToExitPressedOnce = false;
    private LinearLayout llScroll;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySpinner = findViewById(R.id.spinner);
        buttonPdf = findViewById(R.id.button);
        llScroll = findViewById(R.id.layout_recycler);
        btnSharePdfFile = findViewById(R.id.btnSharePdfFile);
        buttonAlert = findViewById(R.id.button1);
        buttonCustomdialog = findViewById(R.id.button2);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Select Table");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        buttonCustomdialog.setEnabled(true);
        buttonCustomdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.custom_dialog);
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            recyclerItem.smoothScrollToPosition(buildItemList().size());
                        }
                    });
                    dialog.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(MainActivity.this);
                            dialog.setContentView(R.layout.custom_dialog_full_screen);
                            dialog.show();
                            //Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                            //startActivity(intent);
                        }
                    });
                    dialog.setTitle("please confirm it ");
                    dialog.show();
                    dialog.getWindow().setAttributes(layoutParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnSharePdfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGeneratedPDF();
            }
        });
        buttonPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
                createPdf();
             /*   BlankFragment fragment = new BlankFragment();

                Bundle bundle = new Bundle();
                //       bundle.putParcelableArrayList("itemList", (ArrayList<? extends Parcelable>) itemModelList);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, fragment).addToBackStack(null).commit();
       */         // Intent intent = new Intent(MainActivity.this, RattingActivity.class);
                //startActivity(intent);
            }
        });
        buttonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes buttonPdf clicked
                                intent = new Intent(MainActivity.this, Main2Activity.class);
                                String string = "Hello Main";
                                intent.putExtra("message", string);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No buttonPdf clicked
                                intent = new Intent(MainActivity.this, RattingActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.mipmap.ic_launcher).setMessage("total item in cart is =123" + "worth 1254").setTitle("Do you want to place Order ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        recyclerItem = findViewById(R.id.rv_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        ItemAdapter itemAdapter = new ItemAdapter(buildItemList(), this);
        recyclerItem.setNestedScrollingEnabled(true);
        recyclerItem.setHasFixedSize(true);
        recyclerItem.setLayoutManager(layoutManager);
        recyclerItem.setAdapter(itemAdapter);
        // Create layout manager with initial prefetch item count
        recyclerCombo = findViewById(R.id.rv_combo);
        LinearLayoutManager layoutManagerCombo = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        // Create sub item view adapter
        ComboAdapter comboAdapter = new ComboAdapter(buildComboItemList(), this);
        recyclerCombo.setNestedScrollingEnabled(false);
        recyclerCombo.setLayoutManager(layoutManagerCombo);
        recyclerCombo.setHasFixedSize(true);
        recyclerCombo.setAdapter(comboAdapter);
        ItemTouchHelper.Callback callback = new SwipeItemTouchHelper(itemAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerItem);

        // SpinnerAdapter spinnerAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, categorymenu());
        //ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        //mySpinner.setAdapter(spinnerAdapter);
        mySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories));
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long itemID) {
                try {

                    if (position > 0 && position < categories.length) {

                        if (itemModelList.get(position).getItemID() >= 0 && itemModelList.get(position).getItemID() == itemID) {
                            getSelectedCategoryData(position);
                            //  recyclerItem.smoothScrollToPosition(itemModelList.get(position).getItemID());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public List<ItemModel> getData() {
        return itemModelList;
    }

    private List<ItemModel> buildItemList() {
        List<ItemModel> itemModelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemModel itemModel = new ItemModel("item" + i, i, buildSubItemList());
            itemModelList.add(itemModel);
        }
        return itemModelList;

    }

    private List<SubItemModel> buildSubItemList() {
        List<SubItemModel> subItemModelList = new ArrayList<>();
        ArrayList<ItemModel> itemModelList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            SubItemModel subItemModel = new SubItemModel(i, "Sub ItemModel " + i, "Description " + i);
            subItemModelList.add(subItemModel);
        }
        itemAdapter = new ItemAdapter(itemModelList, this);

        return subItemModelList;
    }

    private List<ComboModel> buildComboItemList() {
        List<ComboModel> comboModelList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ComboModel comboModelItem = new ComboModel("ComboModel ItemModel" + i, "Description" + i);
            comboModelList.add(comboModelItem);
        }
        return comboModelList;
    }

    private void getSelectedCategoryData(int categoryId) {
        //arraylist to hold selected cosmic bodies
        ComboAdapter comboAdapter;
        ArrayList<ItemModel> itemModelList = new ArrayList<>();

        if (categoryId == 0) {
            itemAdapter = new ItemAdapter(buildItemList(), this);
        } else {
            //filter by id
            for (ItemModel itemModel : buildItemList()) {
                if (itemModel.getItemID() == categoryId) {
                    itemModelList.add(itemModel);
                }
            }
            //instatiate adapter a
            itemAdapter = new ItemAdapter(itemModelList, this);
            //recyclerItem.smoothScrollToPosition(5);

        }
        //set the adapter to GridView
        recyclerItem.setAdapter(itemAdapter);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();

        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void getMenuData(int id) {
    }/*{
        ArrayList<Integer> integerArrayList = new ArrayList<>();

        ArrayList stringArrayList = new ArrayList<String>();
        for (int i = 0; i < itemModelList.size(); i++) {
            stringArrayList.add(itemModelList.get(i).getItemID());
            stringArrayList.add(Integer.valueOf(itemModelList.get(i).getItemTitle()));
        }
        ArrayList integerArrayList1 = new ArrayList<>(stringArrayList);

    }*/

    ///////////////////pdfview
    private Bitmap loadBitmapFromView(LinearLayout llScroll, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        llScroll.draw(c);
        return b;
    }

    void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/pdffromScroll.pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

    }

    private void openGeneratedPDF() {
        File file = new File("/sdcard/pdffromScroll.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(MainActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

}