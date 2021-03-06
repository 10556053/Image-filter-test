//package com.example.cameratest.Fragments;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.cameratest.Adapter.ThumbnailAdapter;
//import com.example.cameratest.Interface.FilterListFragmentListener;
//import com.example.cameratest.MainActivity;
//import com.example.cameratest.R;
//import com.example.cameratest.Utils.BitmapUtils;
//import com.example.cameratest.Utils.SpacesItemDecoration;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.zomato.photofilters.FilterPack;
//import com.zomato.photofilters.imageprocessors.Filter;
//import com.zomato.photofilters.utils.ThumbnailItem;
//import com.zomato.photofilters.utils.ThumbnailsManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class FilterListFragment extends BottomSheetDialogFragment implements FilterListFragmentListener{
//    RecyclerView recyclerView;
//    ThumbnailAdapter adapter;
//    List<ThumbnailItem> thumbnailItems;
//    FilterListFragmentListener listener;
//
//    //fix crash
//    static Bitmap bitmap;
//
//    static FilterListFragment instance;
//
//    public static FilterListFragment getInstance(Bitmap bitmapSaved) {
//
//        bitmap = bitmapSaved;
//
//        if (instance ==null){
//            instance = new FilterListFragment();
//
//        }
//        return instance;
//    }
//
//    public FilterListFragment() {
//        // Required empty public constructor
//    }
//
//    public void setListener(FilterListFragmentListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View itemView= inflater.inflate(R.layout.fragment_filter_list, container, false);
//
//        thumbnailItems = new ArrayList<>();
//        adapter = new ThumbnailAdapter(thumbnailItems,this,getActivity());
//        recyclerView = itemView.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,getResources().getDisplayMetrics());
//        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
//        recyclerView.setAdapter(adapter);
//
//        displayThumbnail(bitmap);
//
//        return  itemView;
//    }
//
//    public void displayThumbnail(final Bitmap bitmap) {
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                Bitmap thumbImg;
//                if(bitmap==null){
//                    thumbImg = BitmapUtils.getBitmapFromAssets(getActivity(), MainActivity.pictureName , 100,100);
//                }else{
//                    thumbImg = Bitmap.createScaledBitmap(bitmap,100,100,false);
//                }
//
//                if (thumbImg ==null) {
//                    return;
//                }
//
//
//                ThumbnailsManager.clearThumbs();
//                thumbnailItems.clear();
//                //add normal bitmap first
//                ThumbnailItem thumbnailItem = new ThumbnailItem();
//                thumbnailItem.image = thumbImg;
//                thumbnailItem.filterName = "Normal";
//
//                ThumbnailsManager.addThumb(thumbnailItem);
//
//                List<Filter> filters = FilterPack.getFilterPack(getActivity());
//
//                for(Filter filter: filters){
//                    ThumbnailItem tI = new ThumbnailItem();
//                    tI.image = thumbImg;
//                    tI.filter = filter;
//                    tI.filterName = filter.getName();
//
//                    ThumbnailsManager.addThumb(tI);
//                }
//                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        };
//        new Thread( r).start();
//    }
//
//    @Override
//    public void onFilterSelected(Filter filter) {
//        if(filter != null){
//            listener.onFilterSelected(filter);
//        }
//    }
//}