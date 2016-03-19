package com.gldraphael.hslpicker;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.gldraphael.hslpicker.BR;

import com.gldraphael.hslpicker.databinding.ContentMainBinding;

public class MainActivity extends AppCompatActivity {

    /**
     * The main activity ViewModel
     */
    public class HslVM extends BaseObservable {

        private IOnColorsUpdated eventHandler;

        private int hue = 192;
        private int saturation = 0;
        private int lightness = 0;

        public HslVM(IOnColorsUpdated eventHandler){
            this.eventHandler = eventHandler;
        }

        public String getHex(){
            return "#CCC";
        }

        public int getColor(){
            return ColorUtils.HSLToColor(new float[] {
                    getHue(),
                    getSaturation() * 0.01f,
                    getLightness() * 0.01f});
        }

        @Override
        public synchronized void notifyChange() {
            super.notifyChange();
            eventHandler.onColorsUpdated();
            Log.d("###", "notifyChange");
        }

        @Override
        public void notifyPropertyChanged(int fieldId) {
            super.notifyPropertyChanged(fieldId);

            eventHandler.onColorsUpdated();
            Log.d("###", "propertyChange");
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
            notifyPropertyChanged(BR.lightness);
        }

        @Override
        public String toString() {
            return "HSL(" + hue + ", " + saturation + ", " + lightness + ")";
        }
    }

    public interface IOnColorsUpdated {
        void onColorsUpdated();
    }

    private HslVM hsl = new HslVM(new IOnColorsUpdated() {
        @Override
        public void onColorsUpdated() {
            updateBackground();
            Log.d("###", "Hey!");
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
        updateBackground();

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

    private void updateBackground(){
        Log.d("###", "Updating background to " + hsl.toString());
        content.setBackgroundColor(hsl.getColor());
    }

    public void onClickSlider(View view) {
        updateBackground();
    }
}
