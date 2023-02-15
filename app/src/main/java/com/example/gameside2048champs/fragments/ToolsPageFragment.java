package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameside2048champs.R;

public class ToolsPageFragment extends Fragment {
    private OnToolsPageFragmentInteractionListener mListener;

    public ToolsPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void settingOnClickListeners() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools_page, container, false);

        settingOnClickListeners();

        return view;
    }

    public interface OnToolsPageFragmentInteractionListener {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnToolsPageFragmentInteractionListener) {
            mListener = (OnToolsPageFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment of ToolsPageFragment must implement " +
                    "OnToolsPageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
