package com.example.cameratest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cameratest.Adapter.StickerAdapter;
import com.example.cameratest.Interface.StickerFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class StickerFragment extends BottomSheetDialogFragment implements StickerAdapter.StickerAdapterListener {

    static StickerFragment instance;
    RecyclerView recycler_sticker;
    StickerFragmentListener listener;


    public void setListener(StickerFragmentListener listener) {
        this.listener = listener;
    }

    public static StickerFragment getInstance() {
        if(instance ==null){
            instance = new StickerFragment();
        }
        return instance;
    }

    public StickerFragment() {
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
        View itemView =  inflater.inflate(R.layout.fragment_sticker, container, false);
        recycler_sticker = itemView.findViewById(R.id.recycler_sticker);

        StickerAdapter adapter = new StickerAdapter(getContext(),this);
        recycler_sticker.setHasFixedSize(false);
        recycler_sticker.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recycler_sticker.setAdapter(adapter);

        return itemView;

    }

    @Override
    public void onStickerItemSelected(int sticker) {
        listener.onStickerSelected(sticker);
    }
}