package com.android_app_2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ShoppingListAdapter adapter;
    private List<ShoppingItem> shoppingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button addButton = findViewById(R.id.addButton);

        shoppingItems = new ArrayList<>();
        adapter = new ShoppingListAdapter(shoppingItems);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog();
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showEditOrDeleteDialog(position);
            }
        });
    }

    private void showEditOrDeleteDialog(final int position) {
        final EditText editItemEditText = new EditText(this);
        editItemEditText.setText(shoppingItems.get(position).getItemName());

        // Create a dialog for editing or deleting an item
        new AlertDialog.Builder(this)
                .setTitle("Редактирование")
                .setView(editItemEditText)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    // Edit the item in the list and update the adapter
                    String updatedItemName = editItemEditText.getText().toString().trim();
                    if (!updatedItemName.isEmpty()) {
                        ShoppingItem updatedItem = new ShoppingItem(updatedItemName);
                        shoppingItems.set(position, updatedItem);
                        adapter.notifyItemChanged(position);
                    }
                })
                .setNegativeButton("Удалить", (dialog, which) -> {
                    // Remove the item from the list and update the adapter
                    shoppingItems.remove(position);
                    adapter.notifyItemRemoved(position);
                })
                .setNeutralButton("Отменить", null)
                .show();
    }

    private void showAddItemDialog() {
        final EditText newItemEditText = new EditText(this);

        // Create a dialog for adding a new item
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Добавить элемент")
                .setMessage("Введите название:")
                .setView(newItemEditText)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String newItemName = newItemEditText.getText().toString().trim();
                    if (!newItemName.isEmpty()) {
                        // Add the new item to the list
                        ShoppingItem newItem = new ShoppingItem(newItemName);
                        shoppingItems.add(newItem);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Отменить", null)
                .show();
    }
}
