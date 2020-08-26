package com.example.cameratest;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.example.cameratest.Adapter.ColorAdapter;
import com.example.cameratest.Interface.BrushFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class BrushFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {

    SeekBar seekBar_brush_size,seekBar_opacity_size;
    RecyclerView recycler_color;
    ToggleButton btn_brush_state;
    ColorAdapter colorAdapter;
    //require a interface BrushFragmentListener
    BrushFragmentListener listener;

    static BrushFragment instance;

    public static BrushFragment getInstance() {
        if(instance == null){
            instance = new BrushFragment();
        }
        return instance;
    }

    //set BrushFragmentListener
    public void setListener(BrushFragmentListener listener) {
        this.listener = listener;
    }

    public BrushFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_brush, container, false);

        seekBar_brush_size = itemView.findViewById(R.id.seekbar_brush_size);
        seekBar_opacity_size = itemView.findViewById(R.id.seekbar_brush_opacity);
        btn_brush_state = itemView.findViewById(R.id.brush_state);
        recycler_color = itemView.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        colorAdapter= new ColorAdapter(getContext(),getColorList(),this);
        recycler_color.setAdapter(colorAdapter);
        //events
        seekBar_opacity_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //pass opacity value to seekBar_opacity_size
                listener.onBrushOpacityChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //pass brush_size value to seekBar_brush_size
                listener.onBrushSizeChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_brush_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //pass checkState to btn_brush_state
                listener.onBrushStateChangeListener(isChecked);
            }
        });

        return itemView;
    }

    private List<Integer> getColorList() {
        List<Integer> colorList=new ArrayList<>();

        colorList.add(Color.parseColor("#f1f1ea"));
        colorList.add(Color.parseColor("#faf6ea"));
        colorList.add(Color.parseColor("#e53c67"));
        colorList.add(Color.parseColor("#a2ca4a"));
        colorList.add(Color.parseColor("#ffbbff"));
        colorList.add(Color.parseColor("#ff3267"));
        colorList.add(Color.parseColor("#007bae"));
        colorList.add(Color.parseColor("#bdcf46"));
        colorList.add(Color.parseColor("#f4d4f4"));
        colorList.add(Color.parseColor("#e9a2e0"));
        colorList.add(Color.parseColor("#ff7f00"));
        colorList.add(Color.parseColor("#b5a8ea"));


        return colorList;
    }

    //implemented from ColorAdapter
    //get color (int) from onColorSelected method
    @Override
    public void onColorSelected(int color) {
        listener.onBrushColorChangeListener(color);
    }
}