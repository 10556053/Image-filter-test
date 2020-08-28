package com.example.cameratest.Fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cameratest.Adapter.EmojiAdapter;
import com.example.cameratest.Interface.EmojiFragmentListener;
import com.example.cameratest.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener {

    RecyclerView recycler_emoji;
    static EmojiFragment instance;
    EmojiFragmentListener listener;


    //讓別人可以實作EmojiFragmentListener這個契約
    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    public static EmojiFragment getInstance() {
        if(instance ==null){
            instance = new EmojiFragment();
        }
        return instance;
    }

    public EmojiFragment() {
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
        View itemView =  inflater.inflate(R.layout.fragment_emoji, container, false);
        recycler_emoji = itemView.findViewById(R.id.recycler_emoji);


        recycler_emoji.setHasFixedSize(false);
        recycler_emoji.setLayoutManager(new GridLayoutManager(getActivity(),5));
        //此處的this是上面implements的EmojiAdapterListener
        EmojiAdapter adapter = new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()),this);
        recycler_emoji.setAdapter(adapter);
        return itemView;
    }


    //實現與EmojiAdapter.EmojiAdapterListener的契約
    //獲得String emoji
    @Override
    public void onEmojiItemSelected(String emoji) {
        //EmojiFragmentListener 接獲從 EmojiAdapter獲得的String emoji
        //並將其成為自己的參數(等待別的class實做自己，將參數傳遞下去)
        listener.onEmojiSelected(emoji);
    }
}