package com.example.cameratest;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cameratest.Adapter.ColorAdapter;
import com.example.cameratest.Interface.AddTextFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

//可以復用colorAdapter
public class TextFragment extends DialogFragment implements ColorAdapter.ColorAdapterListener {

    EditText edt_add_text;
    Button btn_add_text;
    int colorSelected = Color.parseColor("#000000");//預設顏色黑色
    RecyclerView recycler_text_color;
    AddTextFragmentListener listener;
    static TextFragment instance;

    //***要加static 別的class才拿的到***
    public static TextFragment getInstance() {
        if(instance==null){
            instance = new TextFragment();
        }
        return instance;
    }

    //讓別的Class可以拿到我的 AddTextFragmentListener 契約
    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    public TextFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_text, container, false);
        edt_add_text = itemView.findViewById(R.id.edt_add_text);
        btn_add_text = itemView.findViewById(R.id.btn_add_text);
        recycler_text_color = itemView.findViewById(R.id.recycler_text_color);

        recycler_text_color.setHasFixedSize(true);
        recycler_text_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        ColorAdapter colorAdapter= new ColorAdapter(getContext(),this);
        recycler_text_color.setAdapter(colorAdapter);

        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //將得到的text及顏色碼放入契約中
                listener.onAddTextButtonClicked(edt_add_text.getText().toString(),colorSelected);
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return itemView;
    }

    //從契約 ColorAdapterListener那拿到顏色
    @Override
    public void onColorSelected(int color) {
        edt_add_text.setTextColor(color);
        colorSelected = color;
    }
}