package com.example.map524assignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listView;
    TextView itemName;
    TextView quantityNumber;
    TextView totalAmount;
    Button buyButton;
    Button managerButton;
    History historyManager;
    ItemsManager productManager;
    NumberPicker picker;
    ListItemAdapter adapter;
    Items selectedItem;
    int selected;
    double total = 0.0;
    Items pants = new Items("Pants", 10, 20.44);
    Items shoes = new Items("Shoes", 100, 10.44);
    Items hats = new Items("Hats", 50, 5.6);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buyButton = findViewById(R.id.buy);
        managerButton = findViewById(R.id.manager);
        picker = findViewById(R.id.numberPicker);
        picker.setMaxValue(100);
        picker.setMinValue(1);
        picker.setValue(1);

        productManager = ((MyApp)getApplication()).itemManager;
        historyManager = ((MyApp)getApplication()).historyManager;
        // Setting List View
        listView = findViewById(R.id.listview);
        // Setting Text Views
        itemName = findViewById(R.id.itemname);
        quantityNumber = findViewById(R.id.qty);
        totalAmount = findViewById(R.id.total);
        // Setting Items
        productManager.addProducts(pants);
        productManager.addProducts(shoes);
        productManager.addProducts(hats);
        picker.setOnValueChangedListener(((numberPicker, i, i1) -> {
            quantityNumber.setText(String.valueOf(i1));
            if(itemName.getText().toString().equals("Product Type")){
                CharSequence text = "Item is not selected";
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }else{
                if(selectedItem.quantity < i1){
                    CharSequence text = "Not enough products in stock";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                total = selectedItem.price * Double.parseDouble(quantityNumber.getText().toString());
                totalAmount.setText(String.valueOf(total));
            }
        }));
        adapter = new ListItemAdapter(productManager.products,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(((adapterView, view, i, l) -> {
            adapter.notifyDataSetChanged();
            selected = i;
            selectedItem = productManager.products.get(selected);
            itemName.setText(selectedItem.name);
            if(!quantityNumber.getText().toString().equals("Quantity")) {
                total = selectedItem.price * Double.parseDouble(quantityNumber.getText().toString());
                totalAmount.setText(String.valueOf(total));
            }
        }));
        buyButton.setOnClickListener(this);
        managerButton.setOnClickListener(this);
    }
    public boolean validate(){
        boolean valid = false;
        if(!itemName.getText().toString().equals("Product Type")
                && !totalAmount.getText().toString().equals("Total")
                && !quantityNumber.getText().toString().equals("Quantity")){
            valid = true;
        }
        return valid;
    }
    public boolean isEnough(){
        boolean enough = false;
        if(Integer.parseInt(quantityNumber.getText().toString()) <= selectedItem.quantity){
            enough = true;
        }
        return enough;
    }
    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.buy:
                if(validate()){
                    if(isEnough()){
                        int qty = Integer.parseInt(quantityNumber.getText().toString());
                        total = selectedItem.price * Double.parseDouble(quantityNumber.getText().toString());
                        historyManager.addItem(itemName.getText().toString(), qty, total);
                        AlertDialog.Builder build = new AlertDialog.Builder(this);
                        build.setTitle("Thank You for purchase");
                        build.setMessage("Your purchase is " + qty + " " + selectedItem.name + " for " + totalAmount.getText().toString());
                        build.show();
                        switch (selectedItem.name) {
                            case "Pants":
                                productManager.products.get(0).quantity-=qty;
                                break;
                            case "Shoes":
                                productManager.products.get(1).quantity-=qty;
                                break;
                            case "Hats":
                                productManager.products.get(2).quantity-=qty;
                                break;
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        CharSequence text = "Not enough products in stock";
                        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    CharSequence text = "All fields are required";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.manager:
                Intent myIntent = new Intent(this, ManagerActivity.class);
                startActivity(myIntent);
                break;
        }
    }

}