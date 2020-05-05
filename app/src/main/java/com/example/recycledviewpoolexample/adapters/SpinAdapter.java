package com.example.recycledviewpoolexample.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.recycledviewpoolexample.R;
import com.example.recycledviewpoolexample.models.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class SpinAdapter extends ArrayAdapter<ItemModel> {
    Context context;
    private List<ItemModel> itemModelList = new ArrayList<>();

    public SpinAdapter(@NonNull Context context, int resource, List<ItemModel> itemModelList) {
        super(context, resource, itemModelList);
    }

    private View initialSelection(boolean dropdown) {
        TextView view = new TextView(getContext());
        view.setText(R.string.All);

        if (dropdown) {
            // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(true);
        }
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        //   ItemModel item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_table_list_layout, parent, false);
            position -= 1;
        }
        TextView textTableNumber = convertView.findViewById(R.id.itemList);
        textTableNumber.setText(itemModelList.get(position).getItemTitle());
        return convertView;

    }
}
