package usi.memotion2.UI.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;

import usi.memotion2.R;
import usi.memotion2.UI.views.HomeView;
import usi.memotion2.UI.views.RegistrationView;
import usi.memotion2.local.database.controllers.LocalStorageController;
import usi.memotion2.local.database.controllers.SQLiteController;
import usi.memotion2.local.database.tables.UserTable;

/**
 * Created by usi on 04/02/17.
 */

public class HomeFragment extends Fragment {
//    private OnRegistrationSurveyChoice callback;
    private LocalStorageController localController;

    private RegistrationView registrationView;
    private HomeView homeView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        localController = SQLiteController.getInstance(getContext());
        View root = inflater.inflate(R.layout.home_layoutt, null);

        return root;
    }
}