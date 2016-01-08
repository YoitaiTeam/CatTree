package com.yoitai.cattree;

import android.widget.Toast;

import com.yoitai.glib.Calc;
import com.yoitai.glib.Vector2;

// キャラクタ管理クラス
public class Cat {
    // 状態
    public static final int STAT_WAITING = 0;
    public static final int STAT_PLAYING = 1;
    public static final int STAT_DEAD = 2;

    // パラメータ
    public static final float SPEED = 2.0f;            // キャラ横方向スピード
    public static final float ACCEL = 0.02f;        // 重力加速度
    public static final float PUSH_SPEED_Y = -5.0f;    // プッシュ時のスピード

    // メンバ変数
    int mStatus;        // 状態
    Vector2 mPos;        // 位置
    Vector2 mSpeed;    // スピード
    Vector2 mAccel;    // 加速度
    MainView mMainView; // MainView
    Input mInput;       // 入力
    Stage mStage;       // ステージ
    Menu mMenu;         // メニュー
    int mFrameNo;       // フレーム番号
    int mPatternNo;    // パターン番号
    Toast toast;

    // コンストラクタ
    public Cat() {
        mStatus = STAT_DEAD;
        double init_x = Math.random() * MainRenderer.CONTENTS_W;
//        double init_x = MainRenderer.CONTENTS_W / 4;
        double init_y = Math.random() * MainRenderer.CONTENTS_H;
//        double init_y = MainRenderer.CONTENTS_H / 2;
        mPos = new Vector2((float) init_x, (float) init_y);
        mSpeed = new Vector2(SPEED, 0.0f);
        mAccel = new Vector2(0.0f, 0.0f);
        mFrameNo = 0;
    }

    // setter
    public void setView(MainView _view) {
        mMainView = _view;
        toast = Toast.makeText(mMainView.getMainActivity(), "", Toast.LENGTH_LONG);
    }

    public void setInput(Input _input) {
        mInput = _input;
    }

    public void setStage(Stage _stage) {
        mStage = _stage;
    }

    public void setMenu(Menu _menu) {
        mMenu = _menu;
    }

    // 毎フレーム処理
    public void frameFunction() {
        switch (mStatus) {
            case STAT_WAITING: {
                // 待ち状態
                if (mInput.checkStatus(Input.STATUS_DOWN) && mMenu.isCloseMenu() && touchTest()) {
                    // 画面がタッチされた：開始へ
                    mStatus = STAT_PLAYING;
                    // タッチされたら鳴く
                    mMainView.getSePlayer().play();
                    // ポイントも付与する
                    addPoint();
                }
            }
            break;
            case STAT_PLAYING: {

                mPos.Add(mSpeed);
                if (mStage.hitTest(mPos.X, mPos.Y, 32.0f, 32.0f)) {
                    // 衝突した：状態を死亡へ
                    mStatus = STAT_DEAD;
                }

                // 加速度によるスピード補正
                mSpeed.Add(mAccel);

                // 重力による加速度の補正
                mAccel.Y += ACCEL;
            }
            break;
            case STAT_DEAD: {
                // 死亡：初期化
//                mPatternNo = Game.TEXNO_CHAR0;
                reset();
            }
            break;
        }

        mFrameNo++;
    }

    // 描画
    public void draw() {
        DrawParams params;

        if (mStatus != STAT_DEAD) {
            params = mMainView.getMainRenderer().allocDrawParams();
            params.setSprite(mPatternNo);
            params.getPos().X = mPos.X;
            params.getPos().Y = mPos.Y;
            params.getScl().X = 1.0f;
            params.getScl().Y = 1.0f;
            params.getOfs().X = 100.0f;
            params.getOfs().Y = 100.0f;
            params.setRot(Calc.CalcAngleRad(mSpeed.X, mSpeed.Y / 8, false));
        }
    }

    public boolean growUp(int _texno) {
        if (mStatus != STAT_DEAD) return false;
        mPatternNo = _texno;
        mStatus = STAT_WAITING;
        return true;
    }

    // ゲームリセット
    void reset() {
        double init_x = Math.random() * MainRenderer.CONTENTS_W;
        double init_y = Math.random() * MainRenderer.CONTENTS_H;

//        mStatus = STAT_WAITING;
        mPos.Set((float) init_x, (float) init_y);
        mSpeed.Set(SPEED, 0.0f);
        mAccel.Set(0.0f, 0.0f);
    }

    boolean touchTest() {
        float x = mInput.getX() - mPos.X;
        float y = mInput.getY() - mPos.Y;

        if (x > 0 && x < 60 && Math.abs(y) < 64) return true;
        return false;
    }

    void addPoint() {
        int point = CatTreeData.getInt(CatTreeData.POINT, 0);
        CatTreeData.setInt(CatTreeData.POINT, ++point);
        toast.setText(point + "ポイントゲットにゃ！");
        toast.show();
    }
}
