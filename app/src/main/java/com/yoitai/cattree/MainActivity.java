package com.yoitai.cattree;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    // メンバー変数
    MainView mMainView;

    // アクティビティ作成時に呼び出されるメソッド
    // onCreate
    @Override
    public void onCreate(Bundle _cycle) {
        super.onCreate(_cycle);

        // フルスクリーン、スクリーンをSLEEPさせない、タイトルバーを表示しない
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // OpenGLサーフェスを作って開始
        mMainView = new MainView(this);
        setContentView(mMainView);
    }

    // アクティビティ中断時に呼び出されるメソッド
    @Override
    protected void onPause() {
        super.onPause();
        mMainView.onPause();
    }

    // アクティビティ復帰時に呼び出されるメソッド
    @Override
    protected void onResume() {
        super.onResume();
        mMainView.onResume();
    }

    // キーが押されたときに呼び出されるメソッド
    @Override
    public boolean onKeyDown(int _code, KeyEvent _ev) {
        switch (_code) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                return (false);
            default:
                break;
        }
        return (super.onKeyDown(_code, _ev));
    }

    @Override
    protected void onDestroy() {
        mMainView.release();
        super.onDestroy();
    }
}
