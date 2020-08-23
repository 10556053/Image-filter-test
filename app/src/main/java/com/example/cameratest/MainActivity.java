package com.example.cameratest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cameratest.Adapter.ViewPagerAdapter;
import com.example.cameratest.Interface.EditImageFragmentListener;
import com.example.cameratest.Interface.FilterListFragmentListener;
import com.example.cameratest.Utils.BitmapUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.IOException;
import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class MainActivity extends AppCompatActivity implements FilterListFragmentListener , EditImageFragmentListener {
    public static String pictureName = "flash.jpg";
    public static final int PERMISSION_PICK_IMAGE =1000;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
//    TabLayout tabLayout;
//    ViewPager viewPager;

    CoordinatorLayout coordinatorLayout;

    CardView btn_filters_list,btn_edit;

    Bitmap originalBitmap,filteredBitmap,finalBitmap;

    FilterListFragment filterListFragment;
    EditImageFragment editImageFragment;

    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float constraintFinal = 1.0f;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MY FILTER");

        photoEditorView = findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this,photoEditorView).setPinchTextScalable(true).build();
//        tabLayout = findViewById(R.id.tabs);
//        viewPager = findViewById(R.id.viewpager);
        coordinatorLayout = findViewById(R.id.coordinator);
        btn_filters_list = findViewById(R.id.btn_filters_list);
        btn_edit = findViewById(R.id.btn_edit);

        btn_filters_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create singleton object
                FilterListFragment filterListFragment = FilterListFragment.getInstance();
                filterListFragment.setListener(MainActivity.this);
                filterListFragment.show(getSupportFragmentManager(),filterListFragment.getTag());
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });

        loadImage();
//        setUpViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);
    }

    private void loadImage() {
        originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);

        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        filterListFragment = new FilterListFragment();
        filterListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(editImageFragment,"edit");
        adapter.addFragment(filterListFragment,"filters");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap =filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    private void resetControl() {
        if(editImageFragment!= null){
            editImageFragment.resetControls();
        }

        brightnessFinal=0;
        saturationFinal=1.0f;
        constraintFinal=1.0f;
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstraintChanged(float constraint) {
        constraintFinal = constraint;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constraint));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constraintFinal));

        finalBitmap = myFilter.processFilter(bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=  item.getItemId();
        if(id==R.id.action_open){
            openImageFromGalary();
            return true;
        }
        if(id == R.id.action_save){
            saveImageFromGalary();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveImageFromGalary() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            try {

                                final String path = BitmapUtils.insertImage(getContentResolver(),finalBitmap,System.currentTimeMillis()+"_profile.jpg",null);

                                if (!TextUtils.isEmpty(path)){
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"image saved to gallary",Snackbar.LENGTH_SHORT)
                                            .setAction("open", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    openImage(path);
                                                }
                                            });
                                    snackbar.show();
                                }else{
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"unable to save image",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();



    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGalary() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_PICK_IMAGE);
                        }else{
                            Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == PERMISSION_PICK_IMAGE) {
            Bitmap bitmap = BitmapUtils.getBitMapFromGallery(this, data.getData(), 800, 800);

            //clear bitmap memory
            originalBitmap.recycle();
            finalBitmap.recycle();
            filteredBitmap.recycle();

            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);


            photoEditorView.getSource().setImageBitmap(originalBitmap);
            bitmap.recycle();

            //render selected image thumbnail
            filterListFragment.displayThumbnail(originalBitmap);
        }
    }
}