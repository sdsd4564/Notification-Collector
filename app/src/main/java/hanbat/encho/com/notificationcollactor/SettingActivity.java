package hanbat.encho.com.notificationcollactor;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import hanbat.encho.com.notificationcollactor.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    public final static int SETTING_FINISHED = 124;
    ActivitySettingBinding binding;
    int validate = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setResult(RESULT_OK);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        setTitle(R.string.setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validate = PreferenceManager.getInstance().getIntegerPref(this, "Validate");
        binding.textValidateDay.setText(validate == 61
                ? getString(R.string.validate_unlock)
                : new StringBuilder("유효일 ").append(validate).append("일"));

        // 최소값 10 ~ 최대값 60, 61은 유효기간 해제
        binding.seekValidateDay.setProgress(validate - 10);
        binding.seekValidateDay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                validate = progress + 10;
                binding.textValidateDay.setText(
                        validate == 61
                                ? getString(R.string.validate_unlock)
                                : new StringBuilder("유효일 ").append(validate).append("일")
                );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 값이 저장된 값과 달라졌을때만 저장함.
        if (validate != PreferenceManager.getInstance().getIntegerPref(this, "Validate")) {
            PreferenceManager.getInstance().setIntegerPref(this, "Validate", validate);
        }
    }
}
