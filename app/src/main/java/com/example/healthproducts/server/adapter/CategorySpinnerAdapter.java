package com.example.healthproducts.server.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthproducts.R;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Category;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {
    public CategorySpinnerAdapter(@NonNull Context context, @NonNull List<String> objects){
        super(context, R.layout.spinner_item, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.spinner_item, null);
        }

        ((TextView)convertView.findViewById(R.id.tv_spinner_item))
                .setText(DataBase.CATEGORY_LIST.get(position).getName());

        return convertView;
    }
}
