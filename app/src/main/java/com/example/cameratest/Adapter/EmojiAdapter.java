package com.example.cameratest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cameratest.R;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    Context context;
    List<String>emojiList;

    //讓別人可以取得這個EmojiAdapterListener契約
    EmojiAdapterListener listener;


    public EmojiAdapter(Context context, List<String> emojiList, EmojiAdapterListener listener) {
        this.context = context;
        this.emojiList = emojiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(context).inflate(R.layout.emoji_item,parent,false);

        return new EmojiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        holder.emoji_text_view.setText(emojiList.get(position));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }


    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView emoji_text_view;
        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            emoji_text_view = itemView.findViewById(R.id.emoji_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //當按鈕按下時
                    //String emojiList.get(position)將會傳送到
                    //辣個implements 這個契約的class 手上
                    //繼續下一步的旅程
                    listener.onEmojiItemSelected(emojiList.get(getAdapterPosition()));

                }
            });
        }
    }

    //fragment也是用同樣的方法互相傳遞參數(frag to frag / frag to activity)
    //締結一個契約叫做EmojiAdapterListener
    public interface EmojiAdapterListener{
        //當實作我這個契約的class呼叫 onEmojiItemSelected() 方法時，得到一個 String emoji
        void onEmojiItemSelected(String emoji);
    }
}
