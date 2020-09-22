package com.example.percorsi.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.percorsi.R;
import com.example.percorsi.activity.RouteActivity;
import com.example.percorsi.adapter.RouteListAdapter;
import com.example.percorsi.model.Route;
import com.example.percorsi.persistence.RouteManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La classe RouteListFragment mostra la lista degli elementi Route generati
 * dall'utente, i quali rappresentano i Percorsi svolti.
 */
public class RouteListFragment extends Fragment{
    private static final String TAG = "FragmentListaPercorsi";

    private RecyclerView routeListRecyclerView = null;
    private LinearLayoutManager routeListLayoutManager = null;
    private RouteListAdapter routeListAdapter = null;

    private List<Route> routeList = null;

    private FloatingActionButton addRouteButton = null;

    private TextView nameRequestTextView, meansRequestTextView;
    private EditText nameEditText;
    private Spinner meansSpinner;

    private AlertDialog routeCreationDialog = null;

    public RouteListFragment() {
        Log.d(TAG, "Costruttore RouteListFragment chiamato");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_route_list, container, false);

        setupRouteList(rootView);

        addRouteButton = rootView.findViewById(R.id.add_route_floating_button);

        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cliccato floatingButton per l'aggiunta di un nuovo percorso");

                openRouteCreationDialog();
            }
        });

        return rootView;
    }

    private void setupRouteList(View v){
        Log.d(TAG, "Chiamato setupRouteList");
        routeListRecyclerView = v.findViewById(R.id.route_recycler_view);

        routeListLayoutManager = new LinearLayoutManager(getActivity());
        routeListLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        routeListRecyclerView.setLayoutManager(routeListLayoutManager);
        routeListRecyclerView.setHasFixedSize(true);

        this.routeList = RouteManager.getInstance(getContext()).getRouteList();
        routeListAdapter = RouteListAdapter.getInstance(getContext());
        routeListRecyclerView.setAdapter(routeListAdapter);

    }

    private void openRouteCreationDialog(){
        Log.d(TAG, "Aperto il CreateRouteDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_create_route_layout, null);

        setupRouteCreationDialogUI(rootView);
        setupRouteCreationDialogSpinner(rootView);

        builder.setView(rootView)
                .setTitle(R.string.create_route_dialog_title)
                .setPositiveButton(R.string.create_route_dialog_confirm_choice, null)
                .setNegativeButton(R.string.create_route_dialog_cancel_choice, null);

        this.routeCreationDialog = builder.create();
        routeCreationDialog.show();
        setupRouteCreationDialogButtons(routeCreationDialog);
    }

    private void setupRouteCreationDialogUI(View view){
        nameRequestTextView = view.findViewById(R.id.dialog_name_request_text_view);
        meansRequestTextView = view.findViewById(R.id.dialog_means_request_text_view);
        nameEditText = view.findViewById(R.id.dialog_name_edit_text);

        nameRequestTextView.setText(R.string.create_route_dialog_name_request);
        meansRequestTextView.setText(R.string.create_route_dialog_means_request);
    }

    private void setupRouteCreationDialogSpinner(View view){
        meansSpinner = view.findViewById(R.id.dialog_means_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.create_route_dialog_means_of_transport_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meansSpinner.setAdapter(adapter);
    }

    private void setupRouteCreationDialogButtons(final AlertDialog dialog){
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cliccato RouteCreationDialog: Annulla");
                dialog.dismiss();
            }
        });

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cliccato RouteCreationDialog: Crea");
                if (nameEditText.getText().toString().equals("")) {
                    nameEditText.setError(getContext().getText(R.string.create_route_dialog_no_name_error));
                }
                else{
                    String routeName = nameEditText.getText().toString();
                    String meansOfTransport = meansSpinner.getSelectedItem().toString();

                    Route route = new Route(routeName, meansOfTransport);
                    RouteListAdapter.getInstance(getContext()).addElementToList(route);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RouteListAdapter.ROUTE_ITEM, route);

                    Intent routeClickedIntent = new Intent(new Intent(v.getContext(), RouteActivity.class));
                    routeClickedIntent.putExtras(bundle);

                    dialog.dismiss();

                    updateList();

                    v.getContext().startActivity(routeClickedIntent);

                }
            }
        });
    }

    public void updateList(){
        if(routeListAdapter != null){
            Log.d(TAG, "Lista aggiornata");
            routeListAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "Impossibile aggiornare la lista: routeListAdapter è null");
    }
}
