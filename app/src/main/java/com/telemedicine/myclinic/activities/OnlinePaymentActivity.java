package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.telemedicine.myclinic.adapters.OnlineServiceAdapter;
import com.telemedicine.myclinic.models.OnlineServicesModel;
import com.telemedicine.myclinic.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OnlinePaymentActivity extends BaseActivity {

    @BindView(R.id.services_recyler)
    RecyclerView serviceRecycler;

    @BindView(R.id.view_all_recycler)
    RecyclerView viewAllRecycler;

    @BindView(R.id.view_all_container)
    LinearLayout viewAllContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        serviceRecycler.setLayoutManager(new LinearLayoutManager(this));
        viewAllRecycler.setLayoutManager(new LinearLayoutManager(this));
        List<OnlineServicesModel> list = new ArrayList<>();
        OnlineServicesModel onlineServicesModel = new OnlineServicesModel();
        onlineServicesModel.setServiceNumber("0185");
        onlineServicesModel.setServiceType("Consultation");
        onlineServicesModel.setServiceMoney("Insurance / 100 SAR");
        list.add(onlineServicesModel);
        list.add(onlineServicesModel);
        list.add(onlineServicesModel);
        OnlineServiceAdapter adapter = new OnlineServiceAdapter(this, list);
        serviceRecycler.setAdapter(adapter);
        viewAllRecycler.setAdapter(adapter);
    }

    @Override
    protected int layout() {
        return R.layout.activity_online_payment;
    }


    public void ViewAll(View view) {

        Animation slideUpAnimation, slideDownAnimation;

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        if (viewAllContainer.getVisibility() == View.GONE) {
            viewAllContainer.startAnimation(slideUpAnimation);
            viewAllContainer.setVisibility(View.VISIBLE);
        } else {
            viewAllContainer.startAnimation(slideDownAnimation);
            viewAllContainer.setVisibility(View.GONE);
        }
    }
}
