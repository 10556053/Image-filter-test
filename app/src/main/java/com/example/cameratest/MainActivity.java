package com.example.cameratest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.cameratest.Interface.AddTextFragmentListener;
import com.example.cameratest.Interface.BrushFragmentListener;
import com.example.cameratest.Interface.EditImageFragmentListener;
import com.example.cameratest.Interface.EmojiFragmentListener;
import com.example.cameratest.Interface.FilterListFragmentListener;
import com.example.cameratest.Interface.StickerFragmentListener;
import com.example.cameratest.Utils.BitmapUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class MainActivity extends AppCompatActivity implements FilterListFragmentListener, EditImageFragmentListener, BrushFragmentListener , EmojiFragmentListener, AddTextFragmentListener, StickerFragmentListener {
    public static String pictureName = "flash.jpg";
    public static final int PERMISSION_PICK_IMAGE =1000;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
//    TabLayout tabLayout;
//    ViewPager viewPager;

    CoordinatorLayout coordinatorLayout;

    CardView btn_filters_list,btn_edit,btn_brush,btn_emoji,btn_text,btn_sticker;

    ImageView btn_undo,btn_redo;

    Bitmap originalBitmap,filteredBitmap,finalBitmap;

    FilterListFragment filterListFragment;
    EditImageFragment editImageFragment;
    StickerFragment stickerFragment;

    Context context;

    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float constraintFinal = 1.0f;

    EmojiFragment emojiFragment;
    TextFragment textFragment;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //=====================init views and set actionbar=========================================//
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MY FILTER");

        photoEditorView = findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this,photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                .build();
//        tabLayout = findViewById(R.id.tabs);
//        viewPager = findViewById(R.id.viewpager);
        coordinatorLayout = findViewById(R.id.coordinator);
        btn_filters_list = findViewById(R.id.btn_filters_list);
        btn_edit = findViewById(R.id.btn_edit);
        btn_brush = findViewById(R.id.btn_brush);
        btn_emoji = findViewById(R.id.btn_emoji);
        btn_text = findViewById(R.id.btn_text);
        btn_sticker = findViewById(R.id.btn_sticker);

        btn_undo = findViewById(R.id.btn_undo);
        btn_redo = findViewById(R.id.btn_redo);

        context = getApplicationContext();

        //=======================bottom cardview onclick============================================//

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
                //獲得該fragment的單一實體
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoEditor.setBrushDrawingMode(true);
                //獲得該fragment的單一實體
                BrushFragment brushFragment = BrushFragment.getInstance();
                //MainActivity.this 指的是最上面我們 implement過的BrushFragmentListener
                //並且導入他的4個listener(onBrushSizeChanged,onBrushOpacityChanged.....)
                brushFragment.setListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });
        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiFragment = EmojiFragment.getInstance();
                //emojiFragment等待MainActivity實作EmojiFragmentListener
                emojiFragment.setListener(MainActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });

        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textFragment = TextFragment.getInstance();
                //有了這行，MainActivity才算是得到契約
                //並且MainActivity會去實作該契約的方法
                textFragment.setListener(MainActivity.this);
                textFragment.show(getSupportFragmentManager(),textFragment.getTag());

            }
        });
        btn_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerFragment = StickerFragment.getInstance();
                stickerFragment.setListener(MainActivity.this);
                stickerFragment.show(getSupportFragmentManager(),stickerFragment.getTag());
            }
        });

        //=======================first time load image into PhotoEditorView=========================//
        loadImage();
        //btn undo,redo=====================================
        btn_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.undo();
            }
        });
        btn_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.redo();
            }
        });

    }
    //=======================first time load image into PhotoEditorView=========================//
    private void loadImage() {
        originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);

        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

//    private void setUpViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        filterListFragment = new FilterListFragment();
//        filterListFragment.setListener(this);
//
//        editImageFragment = new EditImageFragment();
//        editImageFragment.setListener(this);
//
//        adapter.addFragment(editImageFragment,"edit");
//        adapter.addFragment(filterListFragment,"filters");
//
//        viewPager.setAdapter(adapter);
//    }
    //=======================method in FilterListFragmentListener=========================//
    @Override
    public void onFilterSelected(Filter filter) {
        resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap =filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    //=======================reset the photo brightness, contrast,...=========================//
    private void resetControl() {
        if(editImageFragment!= null){
            editImageFragment.resetControls();
        }

        brightnessFinal=0;
        saturationFinal=1.0f;
        constraintFinal=1.0f;
    }

    //======================methods in EditImageFragmentListener===============================//

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


    //======================create options menu===============================//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    //======================options menu onItemSelected===============================//
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

    //=============method to save image to gallery, triggered in options menu=======================//
    private void saveImageFromGalary() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    try {
                                        //photoEditorView.getSource().setImageBitmap(saveBitmap);
                                        final String path = BitmapUtils.insertImage(getContentResolver(),saveBitmap,System.currentTimeMillis()+"_profile.jpg",null);

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
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
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
    //====================called by saveImageFromGallery to open phones gallery=====================//
    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    //=============method to open image to gallery, triggered in options menu=======================//
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
    //====================triggered by openImageFromGalary(startActivityForResult) to open phones gallery=====================//
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

    //===================methods from BrushFragmentListener =======================================//
    @Override
    public void onBrushSizeChanged(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChanged(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangeListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangeListener(boolean isEraser) {
        if (isEraser){
            photoEditor.brushEraser();
        }else{
            photoEditor.setBrushDrawingMode(true);
        }
    }
    //===================methods from EmojiFragmentListener =======================================//
    @Override
    public void onEmojiSelected(String emoji) {
        //從契約中取出emoji
        photoEditor.addEmoji(emoji);
        emojiFragment.dismiss();
    }



    @Override
    public void onStickerSelected(int sticker) {

        Drawable d = context.getResources().getDrawable(sticker);
        Bitmap bitmap = BitmapUtils.drawableToBitmap(d);
        photoEditor.addImage(bitmap);


        stickerFragment.dismiss();
    }


    @Override
    public void onAddTextButtonClicked(Typeface typeface, String text, int colorSelected) {
        photoEditor.addText(typeface,text,colorSelected);
        textFragment.dismiss();
    }
}