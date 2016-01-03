package com.example.cristinafontanet.parkingidi;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogHelp extends android.app.DialogFragment implements View.OnClickListener {

    myExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public DialogHelp() {
        // Required empty public constructor
    }

    public static DialogHelp newInstance() {
        DialogHelp fragment = new DialogHelp();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_help, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        expListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        prepareListData();
        listAdapter = new myExpandableListAdapter(getActivity(),listDataHeader,listDataChild);
        expListView.setAdapter(listAdapter);

        Button close = (Button) v.findViewById(R.id.button2);
        close.setOnClickListener(this);

        return v;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Registra l'entrada d'un cotxe");
        List<String> entry = new ArrayList<>();
        entry.add("-   Clicant al botó amb el simbol '+' d'abaix a la dreta");
        entry.add("-   Clicant sobre qualsevol plaça lliure");
        entry.add("Si hi ha places lliures, es demana la matricula del cotxe i se li assigna una plaça aleatòria");
        entry.add("Si no hi ha places lliures, es mostra un missatge d'error");
        listDataChild.put(listDataHeader.get(0), entry); // Header, Child data

        listDataHeader.add("Registra la sortida d'un cotxe");
        List<String> exit = new ArrayList<>();
        exit.add("-   Clicant sobre la plaça ocupada pel cotxe que vol sortir");
        exit.add("Es mostra el tiquet de sortida, amb les dades del cotxe i el preu a pagar");
        listDataChild.put(listDataHeader.get(1), exit);

        listDataHeader.add("Canvi de visualització de recaptació");
        List<String> recapt = new ArrayList<>();
        recapt.add("-   Clicant sobre el botó rodó de la pàgina d'Historial");
        recapt.add("Es mostren els diferents tipus de visualització de la recaptació i permet triar entre ells");
        listDataChild.put(listDataHeader.get(2), recapt);

        listDataHeader.add("Recaptació total");
        List<String> recaptTotal = new ArrayList<>();
        recaptTotal.add("Mostra la recaptació i les dades de tots els cotxes enregistrats al sistema des del seu inici i fins al moment de seleccionar aquesta funcionalitat");
        listDataChild.put(listDataHeader.get(3), recaptTotal);

        listDataHeader.add("Recaptació diària");
        List<String> recaptDiaria = new ArrayList<>();
        recaptDiaria.add("Mostra la recaptació i les dades de tots els cotxes enregistrats des de les 00:00 del dia actual i fins al moment de seleccionar aquesta funcionalitat ");
        listDataChild.put(listDataHeader.get(4), recaptDiaria);

        listDataHeader.add("Recaptació mensual");
        List<String> recaptMensual = new ArrayList<>();
        recaptMensual.add("Mostra la recaptació i les dades de tots els cotxes enregistrats des de les 00:00 del dia 1 del més actual i fins al moment de seleccionar aquesta funcionalitat ");
        listDataChild.put(listDataHeader.get(5), recaptMensual);

        listDataHeader.add("Recaptació entre dues dates");
        List<String> recaptBetween = new ArrayList<>();
        recaptBetween.add("Mostra la recaptació i les dades de tots els cotxes enregistrats des de les 00:00 del dia i fins al moment de seleccionar aquesta funcionalitat ");
        listDataChild.put(listDataHeader.get(6), recaptBetween);

        listDataHeader.add("Ordre de visualització");
        List<String> order = new ArrayList<>();
        order.add("La recaptació es pot mostrar tant en ordre ascendent com ascendent i canviar-ne l'ordre a partir del botó 'a>z' ");
        listDataChild.put(listDataHeader.get(7), order);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button2) {
            dismiss();
        }
    }

}
