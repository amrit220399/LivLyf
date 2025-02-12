package com.apsinnovations.livlyf;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.apsinnovations.livlyf.models.User;
import com.apsinnovations.livlyf.utils.MyCartListener;
import com.apsinnovations.livlyf.utils.MyCategoryListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MyCartListener, MyCategoryListener {

    private AppBarConfiguration mAppBarConfiguration;
    TextView txtName, txtEmail;
    ImageView imgProfile;
    View hView;
    int items;
    boolean isFirstTymResume = true;
    Menu menu;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable drawable = getDrawable(R.drawable.livlogo);
        drawable.setTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        toolbar.setLogo(drawable);
        new MyAsyncTask().execute("");
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
        hView = navigationView.getHeaderView(0);
        txtName = hView.findViewById(R.id.HV_txtName);
        txtEmail = hView.findViewById(R.id.HV_txtEmail);
        imgProfile = hView.findViewById(R.id.HV_img);
        fetchProfile();
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

        String order = getIntent().getStringExtra("order");
        if (order != null && order.equals("success")) {
            navController.navigate(R.id.action_nav_home_to_nav_orders);
        }
    }

    private void fetchProfile() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                assert user != null;
                txtName.setText(user.getName());
                txtEmail.setText(user.getEmail());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });

        Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        Picasso.get().load(uri).fit().into(imgProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
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
        }else{
            MenuItem item = menu.findItem(R.id.opt_cart);
            item.setActionView(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstTymResume)
          new MyAsyncTask().execute("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstTymResume = false;
    }

    private void getMyCartCount(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("myCart")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.i(TAG, "onSuccess: Cart SIZE"+queryDocumentSnapshots.size());
                items=queryDocumentSnapshots.size();
                updateCart();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
                updateCart();
            }
        });
    }

    @Override
    public void setItems(int itemsCount) {
        new MyAsyncTask().execute("");
    }

    @Override
    public void setCategory(String name) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_home_to_nav_shopByCategory, bundle);
//        FragmentManager fm=getSupportFragmentManager();
//        FragmentTransaction ft=fm.beginTransaction();
//        ShopByCategoryFragment shopByCategoryFragment=new ShopByCategoryFragment();
//        shopByCategoryFragment.setArguments(bundle);
//        ft.replace(R.id.nav_host_fragment,shopByCategoryFragment)
//                .addToBackStack(null).commit();

    }

    class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            getMyCartCount();
            return null;
        }
    }
}