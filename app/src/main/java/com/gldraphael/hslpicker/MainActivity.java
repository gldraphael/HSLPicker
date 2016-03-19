package com.gldraphael.hslpicker;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.gldraphael.hslpicker.databinding.ContentMainBinding;
import com.gldraphael.hslpicker.BR;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "###MainActivity";

    /**
     * The main activity ViewModel
     */
    public class HslVM extends BaseObservable {

        private IOnColorsUpdatedListener eventHandler;

        private int hue = 205;
        private int saturation = 100;
        private int lightness = 50;

        public HslVM(IOnColorsUpdatedListener eventHandler){
            this.eventHandler = eventHandler;
        }

        /**
         * Converts a color to it's hex representation: http://stackoverflow.com/a/6540378/1678053
         * @return hex string of the color without alpha
         */
        @Bindable
        public String getHex(){
            return String.format("#%06X", (0xFFFFFF & getColor()));
        }

        public int getColor(){
            return ColorUtils.HSLToColor(new float[] {
                    getHue(),
                    getSaturation() * 0.01f,
                    getLightness() * 0.01f});
        }

        @Override
        public void notifyPropertyChanged(int fieldId) {
            super.notifyPropertyChanged(fieldId);

            eventHandler.onColorsUpdated();
            Log.v(TAG, "propertyChange");
        }

        @Bindable
        public int getHue() {
            return hue;
        }

        public void setHue(int hue) {
            this.hue = hue;
            notifyPropertyChanged(BR.hue);
        }

        @Bindable
        public int getSaturation() {
            return saturation;
        }

        public void setSaturation(int saturation) {
            this.saturation = saturation;
            notifyPropertyChanged(BR.saturation);
        }

        @Bindable
        public int getLightness() {
            return lightness;
        }

        public void setLightness(int lightness) {
            this.lightness = lightness;
            notifyPropertyChanged(com.gldraphael.hslpicker.BR.lightness);
        }

        @Override
        public String toString() {
            return "HSL(" + hue + ", " + saturation + ", " + lightness + ") " + getHex();
        }
    }

    public interface IOnColorsUpdatedListener {
        void onColorsUpdated();
    }

    private HslVM hsl = new HslVM(new IOnColorsUpdatedListener() {
        @Override
        public void onColorsUpdated() {
            updateUI();
        }
    });
    private View content = null;
    ContentMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = findViewById(android.R.id.content);

        // Configure databinding
        binding = DataBindingUtil.setContentView(this, R.layout.content_main);
        binding.setHslvm(hsl);
        updateUI();

        binding.sbHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hsl.setHue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hsl.setSaturation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.sbLightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hsl.setLightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateUI(){
        int color = hsl.getColor();

        // Update background color
        Log.v(TAG, "Updating background to " + hsl.toString());
        content.setBackgroundColor(color);

        int compColor = getContrastColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // update the status bar color
            getWindow().setStatusBarColor(color);

            // Update the slider colors
            Log.d(TAG, "Updating sliders to " + String.format("#%06X",compColor));
            updateSlider(binding.sbHue, compColor);
            updateSlider(binding.sbSaturation, compColor);
            updateSlider(binding.sbLightness, compColor);
        }

        binding.txtHexCode.setText(hsl.getHex());
        binding.txtHexCode.setTextColor(compColor);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateSlider(SeekBar seekBar, int color){
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        seekBar.setProgressTintList(colorStateList);
        seekBar.setProgressBackgroundTintList(colorStateList);
        seekBar.setThumbTintList(colorStateList);
    }

    private int getContrastColor(@ColorInt int color){

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        if(hsv[2] < 0.5) {
            hsv[2] = 0.7f;
        } else {
            hsv[2] = 0.3f;
        }

        hsv[1] = hsv[1] * 0.2f;

        return Color.HSVToColor(hsv);
    }
}
