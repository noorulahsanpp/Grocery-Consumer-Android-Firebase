package com.example.grocery_consumer;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {


    static ArrayList<String> cName = new ArrayList<>();
    static  ArrayList<String> cNum = new ArrayList<>();
    static ArrayList<String> cImages = new ArrayList<>();
    static ArrayList<String> cImageurl = new ArrayList<>();
    static ArrayList<String> cPrices = new ArrayList<>();
    static  String storeid,userid,cartstoreid = "",cartid="";


//    static Date date = setDate();
    float cartvalue = 0,total = 0,discount = 0;

    public CartAdapter( String userId, String id,ArrayList<String> itemno1, ArrayList<String> name1, ArrayList<String> prices1, ArrayList<String> images1) {
        userid = userId;
        storeid=id;
        cName = name1;
        cPrices = prices1;
        cNum = itemno1;
        cImages = images1;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_items, parent, false);
        return new CartHolder(view);
    }
public static void updateProducts(){
    FirebaseFirestore firebaseFirestore;
    firebaseFirestore = FirebaseFirestore.getInstance();

    firebaseFirestore.collection("customers").document(userid+"").collection("cart").document("cart").update(
            "itemno", cNum , "name",cName,"price",cPrices,"image",cImages);
}
    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        holder.topic.setText(cName.get(position));
        holder.price.setText(cPrices.get(position));
        String amount =cPrices.get(position);
        String imageUrl = cImages.get(position);
        cImageurl.add(imageUrl);
        Picasso.get().load(imageUrl).into(holder.image);
        holder.topic.setText(cName.get(position));
        String itemnum = cNum.get(position);
         holder.edit.setNumber(itemnum);
        float pdtprice = ( Float.parseFloat(amount)* Integer.parseInt(itemnum));
        cartvalue = cartvalue +pdtprice;
                      MyCart.cartvalue.setText("₹ "+cartvalue);
                      total = total+discount+pdtprice;
                      MyCart.total.setText("₹ "+total);
    }

    @Override
    public int getItemCount() {
        return cName.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder {

        TextView topic;
        TextView price;
        ImageView image;
        String value;
        ElegantNumberButton edit;
        String n, p, i;
        int flag = 0;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.itemname);
            price = itemView.findViewById(R.id.itemprice);
            image = itemView.findViewById(R.id.itemimage);
            edit = itemView.findViewById(R.id.editbutton);
            edit.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    p = (String) price.getText();
                    i = cImageurl.get(getAdapterPosition());
                    value = String.valueOf(newValue);
                    n = (String) topic.getText();
                    if (cName.size() == 0) {
                        cName.add(n);
                        cNum.add(value);
                        cPrices.add(p);
                        cImages.add(i);
                    } else {
                        for (int i = 0; i < cName.size(); i++) {
                            flag = 0;
                            if (n.equals(cName.get(i))) {
                                cNum.set(i, value);
                                flag = 1;
                                break;
                            }
                        }
                        if (flag != 1) {
                            cName.add(n);
                            cNum.add(value);
                            cPrices.add(p);
                            cImages.add(i);
                        }
                    }
                    if (newValue == 0) {
                        for (int i = 0; i < cNum.size(); i++) {
                            String n1 = "0";
                            if (n1.equals(cNum.get(i))) {

                                cNum.remove(i);
                                cName.remove(i);
                                cPrices.remove(i);
                                cImages.remove(i);
                                removeAt(i);
                                break;

                            }
                        }
                    }
                    ProductAdapter.name = cName;
                    ProductAdapter.num = cNum;
                    ProductAdapter.images = cImages;
                    ProductAdapter.prices = cPrices;
                    if(cName.size()==0){
                        ProductAdapter.deletecart();
                    }
                    else {
                        updateProducts();
                    }
                    cartvalue=0;
                    total=0;
                    for(int i=0;i<cNum.size();i++) {
                        cartvalue = cartvalue + (Float.parseFloat(cPrices.get(i)) * Integer.parseInt(cNum.get(i)));
                        total = total + discount + (Float.parseFloat(cPrices.get(i)) * Integer.parseInt(cNum.get(i)));
                    }
                    MyCart.total.setText("₹ "+total);
                    MyCart.cartvalue.setText("₹ "+cartvalue);




                }
            });

        }

    }

    public void removeAt(int position) {
          notifyItemRemoved(position);

    }

}

