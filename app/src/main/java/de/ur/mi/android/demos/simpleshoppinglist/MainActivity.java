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

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> shoppingList;
    private ArrayAdapter<String> shoppingListAdapter;

    private EditText quantityInputElement;
    private EditText itemInputElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        initUI();
    }

    private void initList() {
        shoppingList = new ArrayList<>();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        shoppingListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingList);
        ListView shoppingListView = findViewById(R.id.list_items);
        shoppingListView.setAdapter(shoppingListAdapter);
        shoppingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeItemFromShoppingList(position);
                return true;
            }
        });
        quantityInputElement = findViewById(R.id.input_quantity);
        itemInputElement = findViewById(R.id.input_item);
        Button addItemButton = findViewById(R.id.input_add_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItemButtonClicked();
            }
        });
    }

    private void onAddItemButtonClicked() {
        String item = itemInputElement.getText().toString();
        String quantityAsString = quantityInputElement.getText().toString();
        if (quantityAsString.length() > 0 && item.length() > 0) {
            int quantity = Integer.parseInt(quantityAsString);
            if (quantity > 0) {
                addItemToShoppingList(quantity, item);
                itemInputElement.setText("");
                quantityInputElement.setText(R.string.input_quantity_default);
            }
        }
    }

    private void addItemToShoppingList(int quantity, String item) {
        String stringForListEntry = quantity + "x " + item;
        shoppingList.add(stringForListEntry);
        shoppingListAdapter.notifyDataSetChanged();
    }

    private void removeItemFromShoppingList(int position) {
        shoppingList.remove(position);
        shoppingListAdapter.notifyDataSetChanged();
    }
}