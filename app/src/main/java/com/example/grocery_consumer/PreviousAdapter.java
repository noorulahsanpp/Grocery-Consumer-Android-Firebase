package com.example.grocery_consumer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PreviousAdapter extends FirestoreRecyclerAdapter<PreviousItem, PreviousAdapter.ItemViewHolder> {

    static ArrayList<String> images = new ArrayList<>();
    static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> itemname = new ArrayList<>();
    public static  ArrayList<String> quantity = new ArrayList<>();
    PreviousSubAdapter previousSubAdapter;

        public PreviousAdapter(FirestoreRecyclerOptions<PreviousItem> options) {
        super(options);

         }

    @Override
    protected void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int position, @NonNull final PreviousItem item) {
        itemViewHolder.tvItemTitle.setText(item.getStorename());
        itemViewHolder.tvdescription.setText(item.getDescription());
      String date;
      Date date1;
      SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
      date1 = item.getDate();
      date = simpleDateFormat.format(date1);
        itemViewHolder.tvDate.setText(date);
        itemname = item.getName();
        quantity = item.getItemno();
        images = item.getImage();
        prices = item.getPrice();
        float total =  0;
        for(int i = 0; i< quantity.size() ;i++ ){
           total = total +Integer.parseInt(quantity.get(i))*Float.parseFloat(prices.get(i));
        }
        itemViewHolder.tvTotal.setText("₹" +total);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(itemViewHolder.rvSubItem.getContext(),2,GridLayoutManager.VERTICAL,false);
        itemViewHolder.rvSubItem.setLayoutManager(gridLayoutManager);
        previousSubAdapter = new PreviousSubAdapter(itemname, quantity, images, prices);
        itemViewHolder.rvSubItem.setAdapter(previousSubAdapter);

    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        try {
            //set time in mili
            Thread.sleep(500);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ItemViewHolder(view);
    }





    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle,tvdescription;
        private RecyclerView rvSubItem;
        private TextView tvDate, tvTotal;


        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            tvdescription = itemView.findViewById(R.id.description);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
            tvDate = itemView.findViewById(R.id.date);
            tvTotal = itemView.findViewById(R.id.total);

        }
    }


}