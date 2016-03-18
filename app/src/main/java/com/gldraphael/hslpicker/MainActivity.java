package com.gldraphael.hslpicker;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gldraphael.hslpicker.databinding.ContentMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure databinding
        ContentMainBinding binding = DataBindingUtil.setContentView(this, R.layout.content_main);
        binding.setHslvm(new HslVM());
    }

    public class HslVM {
        public int hue = 53;
        public int saturation = 100;
        public int lightness = 50;
    }
}
