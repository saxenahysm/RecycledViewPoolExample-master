package com.example.recycledviewpoolexample.adapters;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycledviewpoolexample.MainActivity;
import com.example.recycledviewpoolexample.R;
import com.example.recycledviewpoolexample.helper.SwipeItemTouchHelper;
import com.example.recycledviewpoolexample.models.ItemModel;
import com.example.recycledviewpoolexample.models.SubItemModel;

import java.util.ArrayList;
import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {
    //private MainActivity mainActivity;
    private List<SubItemModel> subItemModelList=new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();
    private Context context;
    private Object Tag;

    public SubItemAdapter() {
    }

    public SubItemAdapter(List<SubItemModel> subItemModelList, Context context) {
        this.subItemModelList = subItemModelList;
        this.context = context;
    }
    public void setListData(List<SubItemModel> pdfModels) {
        this.subItemModelList = pdfModels;
        notifyDataSetChanged();
    }
    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle;
        Button button;

        SubItemViewHolder(final View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_sub_item_title);
            button = itemView.findViewById(R.id.btn);
        }
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, final int position) {
        final SubItemModel subItemModel = subItemModelList.get(position);
        subItemViewHolder.tvSubItemTitle.setText(subItemModel.getSubItemTitle());
        subItemViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "value inserted", Toast.LENGTH_SHORT).show();
                //  stringArrayList.add(subItemModel.getSubItemDesc());
                //stringArrayList.add(subItemModel.getSubItemTitle());
//                mainActivity.getMenuData(i);
                Snackbar.make(view, "items is" + subItemModelList.get(position).getSubItemTitle(), Snackbar.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subItemModelList.size();
    }

}
