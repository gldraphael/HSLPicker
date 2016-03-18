package com.gldraphael.hslpicker;

import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.gldraphael.hslpicker.databinding.ContentMainBinding;

public class MainActivity extends AppCompatActivity {

    /**
     * The main activity ViewModel
     */
    public class HslVM {
        public int hue = 192;
        public int saturation = 100;
        public int lightness = 50;

        public String getHex(){
            return "#CCC";
        }

        public int getColor(){
            return ColorUtils.HSLToColor(new float[] { hue, saturation * 0.01f, lightness * 0.01f});
        }
    }

    private HslVM hsl = new HslVM();
    private View content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = findViewById(android.R.id.content);

        // Configure databinding
        ContentMainBinding binding = DataBindingUtil.setContentView(this, R.layout.content_main);
        binding.setHslvm(hsl);
        updateBackground();
    }

    private void updateBackground(){
        content.setBackgroundColor(hsl.getColor());
    }
}
