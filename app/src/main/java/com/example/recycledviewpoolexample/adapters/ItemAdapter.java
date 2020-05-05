package com.example.recycledviewpoolexample.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycledviewpoolexample.helper.SwipeItemTouchHelper;
import com.example.recycledviewpoolexample.models.ItemModel;
import com.example.recycledviewpoolexample.R;
import com.example.recycledviewpoolexample.models.SubItemModel;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements SwipeItemTouchHelper.SwipeHelperAdapter {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<ItemModel> itemModelList = new ArrayList<>();
    private List<ItemModel> itemModelList_swipe = new ArrayList<>();
    private List<SubItemModel> subItemModelList = new ArrayList<>();
    private SubItemAdapter subItemAdapter;
    ArrayList<String> stringArrayList = new ArrayList<>();
    private Context context;
    public ItemAdapter(List<ItemModel> itemModelList, Context context) {
        this.itemModelList = itemModelList;
        this.context = context;
    }

    public ItemAdapter(List<SubItemModel> subItemModelList) {
        this.subItemModelList = subItemModelList;
    }

    public ItemAdapter() {
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements SwipeItemTouchHelper.TouchViewHolder {
        private TextView tvItemTitle;
        private RecyclerView rvSubItem;
        private Button button;

        ItemViewHolder(final View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
            button = itemView.findViewById(R.id.btn_item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(itemView, "Item " + " clicked", Snackbar.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public void onItemSelected() {
            //  itemView.setBackground(context.getResources().getDrawable(R.drawable.shyam));
//            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }

        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor();
            itemView.setBackground(context.getResources().getDrawable(R.drawable.shyam));
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, final int i) {
        ItemModel itemModel = itemModelList.get(i);
        itemViewHolder.tvItemTitle.setText(itemModel.getItemTitle());
        // Create layout manager with initial prefetch itemModel count
        LinearLayoutManager layoutManagerSubMenu = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManagerSubMenu.setInitialPrefetchItemCount(itemModel.getSubItemModelList().size());
        // Create sub itemModel view adapter
        subItemAdapter = new SubItemAdapter(getSubItem(i), context);

        itemViewHolder.rvSubItem.setLayoutManager(layoutManagerSubMenu);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);
        itemViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "value inserted", Toast.LENGTH_SHORT).show();
                //  stringArrayList.add(subItemModel.getSubItemDesc());
                //stringArrayList.add(subItemModel.getSubItemTitle());
//                mainActivity.getMenuData(i);
                Snackbar.make(view, "items is" + subItemModelList.get(i).getSubItemTitle(), Snackbar.LENGTH_SHORT);
            }
        });
    }

    private List<SubItemModel> buildSubItemList() {
        List<SubItemModel> subItemModelList = new ArrayList<>();
        ArrayList<ItemModel> itemModelList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            SubItemModel subItemModel = new SubItemModel(i, "Sub ItemModel " + i, "Description " + i);
            subItemModelList.add(subItemModel);
        }
        subItemAdapter = new SubItemAdapter(subItemModelList, context);
        //instatiate adapter a

        return subItemModelList;
    }

    private List<SubItemModel> getSubItem(int position) {

        for (SubItemModel subItemModel : buildSubItemList()) {
            if (subItemModel.getSubItemID() == itemModelList.get(position).getItemID()) {
                subItemModelList.add(subItemModel);
            }
        }
        subItemAdapter = new SubItemAdapter(subItemModelList, context);
        //instatiate adapter a

        return subItemModelList;
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    @Override
    public void onItemDismiss(int position) {

        // handle when double swipe
        if (itemModelList.get(position).swiped) {
            itemModelList_swipe.remove(itemModelList.get(position));
            itemModelList.remove(position);
            notifyItemRemoved(position);
            return;
        }

        itemModelList.get(position).swiped = true;
        itemModelList_swipe.add(itemModelList.get(position));
        notifyItemChanged(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                for (ItemModel s : itemModelList_swipe) {
                    int index_removed = itemModelList.indexOf(s);
                    if (index_removed != -1) {
                        itemModelList.remove(index_removed);
                        notifyItemRemoved(index_removed);
                    }
                }
                itemModelList_swipe.clear();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

}