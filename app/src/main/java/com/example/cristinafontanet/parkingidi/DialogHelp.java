package com.example.cristinafontanet.parkingidi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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

        listDataHeader.add("Estat d'una plaça");
        List<String> stat = new ArrayList<>();
        stat.add("Les places lliures estan indicades de color verd i amb un text indicatiu al costat del número de plaça");
        stat.add("Les places ocupades estan indicades de color vermell, amb la matricula al costat del número de plaça i la data i hora d'entrada");
        listDataChild.put(listDataHeader.get(0), stat);

        listDataHeader.add("Registra l'entrada d'un cotxe");
        List<String> entry = new ArrayList<>();
        entry.add("-   Clicant al botó '+' d'abaix a la dreta");
        entry.add("-   Clicant sobre qualsevol plaça lliure");
        entry.add("Si hi ha places lliures, es demana la matricula del cotxe i se li assigna una plaça aleatòria");
        entry.add("Si no hi ha places lliures, es mostra un missatge d'error");
        listDataChild.put(listDataHeader.get(1), entry);

        listDataHeader.add("Registra la sortida d'un cotxe");
        List<String> exit = new ArrayList<>();
        exit.add("-   Clicant sobre la plaça ocupada pel cotxe que vol sortir");
        exit.add("Es mostra el tiquet de sortida, amb les dades del cotxe i el preu a pagar");
        listDataChild.put(listDataHeader.get(2), exit);

        listDataHeader.add("Canvi de visualització de recaptació");
        List<String> recapt = new ArrayList<>();
        recapt.add("-   Clicant sobre el botó superior a la dreta en Historial");
        recapt.add("Es mostren els diferents tipus de visualització de la recaptació i permet triar entre ells");
        listDataChild.put(listDataHeader.get(3), recapt);

        listDataHeader.add("Tipus de visualització de la recaptació");
        List<String> recaptTypes = new ArrayList<>();
        recaptTypes.add("Recaptació total: Mostra la recaptació i les dades de tots els cotxes enregistrats al sistema des del seu inici i fins al moment de seleccionar aquesta funcionalitat");
        recaptTypes.add("Recaptació diària: Mostra la recaptació i les dades de tots els cotxes enregistrats des de les 00:00 del dia actual i fins al moment de seleccionar aquesta funcionalitat ");
        recaptTypes.add("Recaptació mensual: Mostra la recaptació i les dades de tots els cotxes enregistrats des de les 00:00 del dia 1 del més actual i fins al moment de seleccionar aquesta funcionalitat ");
        recaptTypes.add("Recaptació entre dues dates: Mostra la recaptació i les dades de tots els cotxes enregistrats des de les 00:00 del dia i fins al moment de seleccionar aquesta funcionalitat ");
        listDataChild.put(listDataHeader.get(4), recaptTypes);

        listDataHeader.add("Ordre de visualització");
        List<String> order = new ArrayList<>();
        order.add("La recaptació es pot mostrar tant en ordre ascendent com ascendent a partir de la data de sortida i canviar-se a partir del botó '1>9' ");
        listDataChild.put(listDataHeader.get(5), order);

        listDataHeader.add("Exporta dades a format csv");
        List<String> export = new ArrayList<>();
        export.add("-   Es pot exportar l'historial de sortides del parking i el resum diari d'aquestes en el menú d'opcions de l'aplicació");
        export.add("El fitxer es crea a la carpeta per defecte de Downloads. En cas de no tenir-ne, se'n pot crear una amb el mateix nom.");
        listDataChild.put(listDataHeader.get(6), export);

        listDataHeader.add("Canvia el preu");
        List<String> price = new ArrayList<>();
        price.add("-    Es pot canviar el preu per minut en el menú d'opcions de l'aplicació");
        price.add("El nou preu s'aplicarà a les sortides que es realitzin a partir de realitzar el canvi");
        listDataChild.put(listDataHeader.get(7), order);

        listDataHeader.add("Canvia el nombre de places");
        List<String> plots = new ArrayList<>();
        plots.add("-    Es pot canviar el nombre de places disponibles al parking en el menú d'opcions de l'aplicació");
        plots.add("Es recomana canviar el nombre de places amb les places buides ja que s'eliminaran els cotxes que ocupin les afectades.");
        listDataChild.put(listDataHeader.get(8), order);
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
