package com.example.grocery_consumer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    static FirebaseFirestore firebaseFirestore;

    static ArrayList<String> cartName = new ArrayList<>();
    static ArrayList<String> cartNum = new ArrayList<>();
    static ArrayList<String> cartImages = new ArrayList<>();
    static ArrayList<String> cartImageurl = new ArrayList<>();
    static ArrayList<String> cartPrices = new ArrayList<>();
    static String storeid,userid;
    float cartvalue = 0,total = 0,discount = 0;

    public CartAdapter( String userId, String id,ArrayList<String> itemno1, ArrayList<String> name1, ArrayList<String> prices1, ArrayList<String> images1) {
        userid = userId;
        storeid=id;
        cartName = name1;
        cartPrices = prices1;
        cartNum = itemno1;
        cartImages = images1;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {

        holder.Tvtopic.setText(cartName.get(position));
        holder.Tvprice.setText(cartPrices.get(position));
        String amount =cartPrices.get(position);
        String imageUrl = cartImages.get(position);
        cartImageurl.add(imageUrl);
        Picasso.get().load(imageUrl).into(holder.Ivimage);
        holder.Tvtopic.setText(cartName.get(position));
        String itemnum = cartNum.get(position);
        holder.editBtn.setNumber(itemnum);
                     float pdtprice = ( Float.parseFloat(amount)* Integer.parseInt(itemnum));
                     cartvalue = cartvalue +pdtprice;
                     MyCart.cartvalueTv.setText("₹ "+cartvalue);
                     total = total+discount+pdtprice;
                     MyCart.totalTv.setText(""+total);
    }

    @Override
    public int getItemCount() {
        return cartName.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder {

        TextView Tvtopic;
        TextView Tvprice;
        ImageView Ivimage;
        String value;
        ElegantNumberButton editBtn;
        String n, p, i;
        int flag = 0;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            Tvtopic = itemView.findViewById(R.id.itemname);
            Tvprice = itemView.findViewById(R.id.itemprice);
            Ivimage = itemView.findViewById(R.id.itemimage);
            editBtn = itemView.findViewById(R.id.editbutton);

            editBtn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    p = (String) Tvprice.getText();
                    i = cartImageurl.get(getAdapterPosition());
                    value = String.valueOf(newValue);
                    n = (String) Tvtopic.getText();
                    if (cartName.size() == 0) {
                        cartName.add(n);
                        cartNum.add(value);
                        cartPrices.add(p);
                        cartImages.add(i);
                    } else {
                        for (int i = 0; i < cartName.size(); i++) {
                            flag = 0;
                            if (n.equals(cartName.get(i))) {
                                cartNum.set(i, value);
                                flag = 1;
                                break;
                            }
                        }
                        if (flag != 1) {
                            cartName.add(n);
                            cartNum.add(value);
                            cartPrices.add(p);
                            cartImages.add(i);
                        }
                    }
                    if (newValue == 0) {
                        for (int i = 0; i < cartNum.size(); i++) {
                            String n1 = "0";
                            if (n1.equals(cartNum.get(i))) {

                                cartNum.remove(i);
                                cartName.remove(i);
                                cartPrices.remove(i);
                                cartImages.remove(i);
                                removeAt(i);
                                break;

                            }
                        }
                    }
                    ProductAdapter.prdtname= cartName;
                    ProductAdapter.prdtnum = cartNum;
                    ProductAdapter.prdtimages = cartImages;
                    ProductAdapter.prdtprices = cartPrices;
                    if(cartName.size()==0){
                       deletecart();
                    }
                    else {
                        updateProducts();
                    }

                    cartvalue=0;
                    total=0;

                    for(int i=0;i<cartNum.size();i++) {
                        cartvalue = cartvalue + (Float.parseFloat(cartPrices.get(i)) * Integer.parseInt(cartNum.get(i)));
                        total = total + discount + (Float.parseFloat(cartPrices.get(i)) * Integer.parseInt(cartNum.get(i)));
                    }

                    MyCart.totalTv.setText("₹ "+total);
                    MyCart.cartvalueTv.setText("₹ "+cartvalue);

                }
            });
        }
    }
    public static void deletecart(){

        firebaseFirestore.collection("customers").document(userid+"").collection("cart").document("cart")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ProductAdapter.flag1=0;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public static void updateProducts(){
        firebaseFirestore.collection("customers").document(userid+"").collection("cart").document("cart").update(
                "itemno", cartNum , "name",cartName,"price",cartPrices,"image",cartImages);
    }

    public void removeAt(int position) {
          notifyItemRemoved(position);
    }

}

