package com.example.cameratest.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.cameratest.Interface.EditImageFragmentListener;
import com.example.cameratest.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditImageFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {
    private EditImageFragmentListener listener;
    SeekBar seekBar_constraint,seekBar_brightness,seekBar_saturation;
    static EditImageFragment instance;

    public static EditImageFragment getInstance() {
        if (instance ==null){
            instance = new EditImageFragment();
        }
        return instance;
    }

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public EditImageFragment() {
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
        View itemView= inflater.inflate(R.layout.fragment_edit_image, container, false);
        seekBar_brightness = itemView.findViewById(R.id.seekbar_brightness);
        seekBar_constraint = itemView.findViewById(R.id.seekbar_constrain);
        seekBar_saturation = itemView.findViewById(R.id.seekbar_saturation);

        seekBar_brightness.setMax(200);
        seekBar_brightness.setProgress(100);

        seekBar_constraint.setMax(20);
        seekBar_constraint.setProgress(0);

        seekBar_saturation.setMax(30);
        seekBar_saturation.setProgress(10);


        seekBar_saturation.setOnSeekBarChangeListener(this);
        seekBar_constraint.setOnSeekBarChangeListener(this);
        seekBar_brightness.setOnSeekBarChangeListener(this);

        return  itemView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener!= null){
            if (seekBar.getId()==R.id.seekbar_brightness){
                listener.onBrightnessChanged(progress-100);
            }else if (seekBar.getId() == R.id.seekbar_constrain){
                progress+=10;
                float value = .10f*progress;
                listener.onConstraintChanged(value);
            }else if (seekBar.getId() == R.id.seekbar_saturation){
                float value = .10f*progress;
                listener.onSaturationChanged(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener!= null){
            listener.onEditStarted();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener!= null){
            listener.onEditCompleted();
        }
    }

    public void resetControls(){
        seekBar_brightness.setProgress(100);
        seekBar_constraint.setProgress(0);
        seekBar_saturation.setProgress(10);
    }
}