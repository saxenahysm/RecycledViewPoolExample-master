package com.example.recycledviewpoolexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.recycledviewpoolexample.models.ItemModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity mainActivity= (MainActivity) getActivity();
        //List<ItemModel> listFromMain=mainActivity.getData();
        assert getArguments() != null;
        List<ItemModel> mlist = (List<ItemModel>) getArguments().getSerializable("itemList");

        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

}
