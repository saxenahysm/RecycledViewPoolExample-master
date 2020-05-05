package com.example.recycledviewpoolexample.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycledviewpoolexample.R;
import com.example.recycledviewpoolexample.models.ComboModel;

import java.util.List;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {
    private List<ComboModel> comboModelList;
    private Context context;

    public ComboAdapter(List<ComboModel> comboModelList, Context context) {
        this.comboModelList = comboModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_combo_item, viewGroup, false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder comboViewHolder, int i) {
        comboViewHolder.itemName.setText(comboModelList.get(i).getComboItemTitle());
        comboViewHolder.description.setText(comboModelList.get(i).getComboItemDesc());
        comboViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ComboViewHolder)viewHolder).itemName.setText(comboModelList.get(i).getComboItemTitle());
        ((ComboViewHolder)viewHolder).description.setText(comboModelList.get(i).getComboItemDesc());
    }
*/
    @Override
    public int getItemCount() {
        return comboModelList.size();
    }

     class ComboViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView description;
       private ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.text_combo_name);
            description=itemView.findViewById(R.id.text_combo_des);
        }
    }
}
