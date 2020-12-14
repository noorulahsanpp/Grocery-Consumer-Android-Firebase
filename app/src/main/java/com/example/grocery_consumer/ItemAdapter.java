package com.example.grocery_consumer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemAdapter extends FirestoreRecyclerAdapter<Item,ItemAdapter.ItemViewHolder> {
    static ArrayList<String> images = new ArrayList<>();
    static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> itemname = new ArrayList<>();
    public static  ArrayList<String> quantity = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton add;
    SubItemAdapter subItemAdapter;
    private String userid;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    final  List<HashMap<String, Object>> listitems = new ArrayList<>();
    private DocumentReference documentReference;
    SimpleAdapter adapter;
    public ItemAdapter(FirestoreRecyclerOptions<Item> options, String userId) {
        super(options);
        this.userid = userId;

    }

    @Override
    protected void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int position, @NonNull final Item item) {


        collectionReference.whereEqualTo("orderid",item.getOrderid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    itemname = (ArrayList<String>) document.get("name");
                    quantity = (ArrayList<String>) document.get("itemno");
                    images = (ArrayList<String>) document.get("image");
                    prices = (ArrayList<String>) document.get("price");
                    LinearLayoutManager layoutManager = new LinearLayoutManager(
                            itemViewHolder.rvSubItem.getContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                    );
                    itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
                    subItemAdapter = new SubItemAdapter(itemname,quantity,images,prices);
                    itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
                    itemViewHolder.tvItemTitle.setText(item.getCustomer());
                    itemViewHolder.phone.setText(item.getPhone());

                }

            }
        });



    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }





    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle,phone;
        private RecyclerView rvSubItem;


        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            phone = itemView.findViewById(R.id.tv_item_phone);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);

            firebaseFirestore = FirebaseFirestore.getInstance();
            collectionReference = firebaseFirestore.collection("customers").document(userid).collection("oldcart");


        }
    }


}