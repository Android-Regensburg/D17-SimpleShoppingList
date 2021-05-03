package de.ur.mi.android.demos.simpleshoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Diese App stellt eine einfache Einkaufsliste zur Verfügung. Menge und Art der Einträge werden
 * über ein einfache Eingabeformular erfasst und als zusammengesetzter String in einer ArrayList
 * gespeichert. Diese ist über einen Adapter mit einem ListView verbunden. Der  Adapter sorgt
 * automatisch dafür, dass Änderungen an der Datenstruktur auch im User Interface angezeigt werden.
 * Dafür wird in der Activity folgender Ablauf implementiert:
 * <p>
 * - Beim Start werden die notwendigen UI-Elemente referenziert und die ArrayList und der Adapter erstellt
 * - Auf den UI-Elementen werden die notwendigen Listener für die Eingabe-Events durch anonyme, innere
 * Klassen abgefangen
 * - ListView und Datenstruktur werden durch den Adapter verbunden
 * - Beim Klick auf den Eingabe-Button werden die Inhhalte der Eingabefelder geprüft
 * - Sind diese nicht leer, wird ein neuer Eintrag zusammengesetzt (Menge + Beschreibung) und
 * in der ArrayList gespeichert
 * - Nach Hinzufügen der neuen Inhalte zur ArrayList wird das ListView durch den Adapter (Aufruf der
 * notifyDataSetChanged-Methode) über die Änderungen informiert. Das View stellt dann automatische
 * die neuen Inhalte im UI dar.
 * <p>
 * Zusätzlich wird der Long-Click auf Einträge der ListView abgefangen: Das so ausgewählte Element
 * wird aus der ArrayList entfernt und der Adapter erneut über die Änderungen an der Datenstruktur
 * informiert.
 */
public class MainActivity extends AppCompatActivity {

    // Datenstruktur für das interne Speichern der Einkaufsliste
    private ArrayList<String> shoppingList;
    // Adapter für das Verbinden von ArrayList und ListView
    private ArrayAdapter<String> shoppingListAdapter;

    // Referenzen auf die UI-Elemente, auf die auch nach der Initialisierung zugegriffen werden muss
    private EditText quantityInputElement;
    private EditText itemInputElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        initUI();
    }

    // Erstellt eine neue, leere ArrayList für die zukünftigen Einträge
    private void initList() {
        shoppingList = new ArrayList<>();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        quantityInputElement = findViewById(R.id.input_quantity); // Eingabefeld für Mengenangabe
        itemInputElement = findViewById(R.id.input_item); // Eingabefeld für Elementbeschreibung
        /**
         * Erstellen des Adapters, der die ArrayList (letzter Parameter) mit dem ListView verknüpfen
         * wird. Der zweite Paramter (layout) gibt an, WIE die Elemente, die der Adapter an
         * angeschlossene Views weiter gibt, in diesen dargestellt werden soll. Hier verwenden wir
         * ein vom Android-SDK vorgegebenes Layout, das die Inahlte als einfaches TextView darstellt.
         */
        shoppingListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingList);
        ListView shoppingListView = findViewById(R.id.list_items);
        // Verknüpfen von Adapter und ListView
        shoppingListView.setAdapter(shoppingListAdapter);
        // Abfangen längerer Klicks/Touches auf den Einträgen des ListView
        shoppingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * In der Callback-Methode erhalten wir unter anderem die Position des angeklickten
                 * Elements (Rank/Reihenfolge) als Parameter übergeben (positon). Über diese können
                 * wir den korrespondierenden Wert in der Datenstruktur identifizieren.
                 */
                return true;
            }
        });
        Button addItemButton = findViewById(R.id.input_add_button);
        // Abfangen von Klicks/Touches auf dem Eingabe-Button
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Aufruf der Methode zum HInzufügen neuer Einträge
                 * Achtung: In den Callback-Methoden der Listener vermeiden wir "zu viel Code". Wir
                 * verweisen hier nur auf anderen Stellen im Code, in den die eigentliche Verarbeitung
                 * des Events durchgeführt wird.
                 */
                onAddItemButtonClicked();
            }
        });
    }

    private void onAddItemButtonClicked() {
        // Auslesen der aktuellen Inhalte der Eingabefelder
        String item = itemInputElement.getText().toString();
        String quantityAsString = quantityInputElement.getText().toString();
        // Prüfen, ob in beiden Felder Inhalte durch die Nutzer*innen eingetragen wurden
        if (quantityAsString.length() > 0 && item.length() > 0) {
            // Prüft, ob eine sinnvolle Mengenangabe im ersten Feld steht
            int quantity = Integer.parseInt(quantityAsString);
            if (quantity > 0) {
                // Hinzufügen des neuen Eintrags zur Liste und zum UI
                addItemToShoppingList(quantity, item);
                // Rücksetzen der Eingabefelder für nächsten Eintrag
                itemInputElement.setText("");
                quantityInputElement.setText(R.string.input_quantity_default);
            }
        }
    }

    /**
     * Fügt ein neues Element zur Einkaufsliste hinzu und aktualisiert das UI
     *
     * @param quantity Mengenangabe für das neue Element
     * @param item     Beschreibung für das neue Element
     */
    private void addItemToShoppingList(int quantity, String item) {
        String stringForListEntry = quantity + "x " + item;
        shoppingList.add(stringForListEntry);
        /**
         * Jetzt teilen wir dem Adapter mit, dass das angeschlossen ListView über Ändeurngen
         * an der Datenstruktur (ArrayList) informiert werden soll.
         */
        shoppingListAdapter.notifyDataSetChanged();
    }

    /**
     * Entfernt ein Element aus der Einkaufsliste und aktualisiert das UI
     *
     * @param position Position des zu entfernenden Elements in der ArrayList
     */
    private void removeItemFromShoppingList(int position) {
        shoppingList.remove(position);
        shoppingListAdapter.notifyDataSetChanged();
    }
}