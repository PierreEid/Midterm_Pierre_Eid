package com.example.comp3074_midterm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber;
    private Button buttonGenerate;
    private Button buttonHistory;
    private ListView listViewResults;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> tableData;

    // Static list to store history of numbers
    public static ArrayList<Integer> historyList = new ArrayList<>();

    private int currentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextNumber = findViewById(R.id.editTextNumber);
        buttonGenerate = findViewById(R.id.buttonGenerate);
        buttonHistory = findViewById(R.id.buttonHistory);
        listViewResults = findViewById(R.id.listViewResults);

        // Initialize data list
        tableData = new ArrayList<>();

        // Setup adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableData);
        listViewResults.setAdapter(adapter);

        // Generate button click listener
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateTable();
            }
        });

        // ListView item click listener
        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
            }
        });

        // History button click listener
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void generateTable() {
        String input = editTextNumber.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            currentNumber = Integer.parseInt(input);

            // Clear previous data
            tableData.clear();

            // Generate multiplication table from 1 to 10
            for (int i = 1; i <= 10; i++) {
                int result = currentNumber * i;
                String row = currentNumber + " × " + i + " = " + result;
                tableData.add(row);
            }

            // Update adapter
            adapter.notifyDataSetChanged();

            // Add to history if not already present
            if (!historyList.contains(currentNumber)) {
                historyList.add(currentNumber);
            }

            Toast.makeText(this, "Table generated for " + currentNumber, Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteDialog(final int position) {
        final String selectedItem = tableData.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Do you want to delete this row?\n\n" + selectedItem);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the item
                tableData.remove(position);
                adapter.notifyDataSetChanged();

                // Show toast message
                Toast.makeText(MainActivity.this, "Deleted: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}