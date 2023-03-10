package com.example.renitus.renter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renitus.Entities.item_modal;
import com.example.renitus.R;
import com.example.renitus.databinding.ActivityCartBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cart_activity extends AppCompatActivity implements cart_RV_Adapter.onDeleteToCartListener, cart_RV_Adapter.onQuantityChangeListener {

    private ActivityCartBinding binding;
    private Toolbar toolbar;
    private ArrayList<item_modal> cartList = new ArrayList<>();
    private Map<item_modal, Integer> finalCartList_Map = new HashMap<>();
    private RecyclerView recyclerView;
    private cart_RV_Adapter adapter;
    private ExtendedFloatingActionButton proceed_to_checkout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            toolbar.setNavigationOnClickListener(arrow -> onBackPressed());
            toolbar.setTitle("Cart");
        }
        recyclerView = binding.cartRV;
        proceed_to_checkout_btn = binding.proceedToCheckoutBtn;

        recyclerView.setLayoutManager(new LinearLayoutManager(cart_activity.this,
                LinearLayoutManager.VERTICAL, false));

        if(getIntent().getBooleanExtra("fromHomeScreen", false)){
            adapter = new cart_RV_Adapter(this, cartList);
            recyclerView.setAdapter(adapter);
        }
        else{
            cartList = getIntent().getParcelableArrayListExtra("CartListFromRenterHomeScreen");
            Log.d("cartList Size", ""+cartList.size());

            adapter = new cart_RV_Adapter(this, cartList);
            recyclerView.setAdapter(adapter);

            for(item_modal item_modal:cartList){
                finalCartList_Map.put(item_modal, 1);
            }

            proceed_to_checkout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckOutItemDialog checkOutItemDialog = new CheckOutItemDialog();
                    checkOutItemDialog.setItem(finalCartList_Map);
                    checkOutItemDialog.show(getSupportFragmentManager(), "Check Out");
                }
            });
        }

    }

    @Override
    public void onDeleteButtonClicked(item_modal item) {

    }

    @Override
    public void onQuantitychange(item_modal cartitem, int quant) {
        finalCartList_Map.put(cartitem, quant);
        Log.d("insideMap", ""+cartitem.getItem_name() +"="+quant);
    }
}