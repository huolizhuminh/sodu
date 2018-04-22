package zhu.minhui.com.shudu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/19.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class FirstActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        findViewById(R.id.play_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, GameActivity.class));
            }
        });
        findViewById(R.id.solve_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, SolveGameActivity.class));
            }
        });
    }
}
