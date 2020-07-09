package com.apsinnovations.livlyf;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.apsinnovations.livlyf.utils.CartPrefMananger;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    int items;
    boolean isFirstTymResume = true;
    Menu menu;
    private CartPrefMananger cartPrefMananger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cartPrefMananger = new CartPrefMananger(this);
        items = cartPrefMananger.getItemsCount();
        Drawable drawable = getDrawable(R.drawable.livlogo);
        drawable.setTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        toolbar.setLogo(drawable);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_shopByCategory, R.id.nav_profile,
                R.id.nav_orders, R.id.nav_wishlist, R.id.nav_wallet,
                R.id.nav_support)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        if (items > 0) {
            final MenuItem item = menu.findItem(R.id.opt_cart);
            item.setActionView(R.layout.cart_notification_badge);
            View view = item.getActionView();
            TextView tv = view.findViewById(R.id.actionbar_notifcation_textview);
            tv.setText(String.valueOf(items));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.opt_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.opt_cart:
                Intent intent1=new Intent(this,MyCartActivity.class);
                startActivity(intent1);
                break;

        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void updateCart() {
        items = cartPrefMananger.getItemsCount();
        if(items>0) {
            final MenuItem item = menu.findItem(R.id.opt_cart);
            item.setActionView(R.layout.cart_notification_badge);
            View view = item.getActionView();
            TextView tv = view.findViewById(R.id.actionbar_notifcation_textview);
            tv.setText(String.valueOf(items));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstTymResume)
            updateCart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstTymResume = false;
    }
}