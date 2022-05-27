package com.example.healthproducts.server.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthproducts.R;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.fragment.ChangeProductFragment;
import com.example.healthproducts.ui.ProductInfoFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Product> productList;

    public static final String PRODUCT_KEY = "Product";

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Product product = DataBase.PRODUCT_LIST.get(position);

        ((MyHolder)holder).tvName.setText(product.getName());
        ((MyHolder)holder).tvCategory.setText(product.getCategory().getName());
        ((MyHolder)holder).tvComposition.setText(product.getComposition());
        try{
            Picasso.get().load(product.getFoto()).into(((MyHolder)holder).ivImage);
        }catch(Exception e){e.printStackTrace();}





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductInfoFragment productInfoFragment = new ProductInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PRODUCT_KEY, product);
                bundle.putSerializable("act", "data");


                productInfoFragment.setArguments(bundle);

                ((AppCompatActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, productInfoFragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataBase.PRODUCT_LIST.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder{

        private TextView tvName, tvCategory, tvComposition;
        private ImageView ivImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_product_name);
            tvCategory = itemView.findViewById(R.id.tv_category_name);
            tvComposition = itemView.findViewById(R.id.tv_composition);
            ivImage = itemView.findViewById(R.id.iv_product_image);
        }
    }
}
