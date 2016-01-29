package com.yoitai.cattree.object;

import android.widget.Toast;

import com.yoitai.cattree.CatTreeData;
import com.yoitai.cattree.DrawParams;
import com.yoitai.cattree.Game;
import com.yoitai.cattree.Input;
import com.yoitai.cattree.MainRenderer;
import com.yoitai.cattree.MainView;
import com.yoitai.cattree.Menu;
import com.yoitai.cattree.Stage;
import com.yoitai.glib.Vector2;

// キャラクタ管理クラス
public class Cat {
    // 状態
    public static final int STAT_WAITING = 0;
    public static final int STAT_PLAYING = 1;
    public static final int STAT_DEAD = 2;

    // パラメータ
    public static final float ACCEL = 0.02f;        // 重力加速度

    // メンバ変数
    int mStatus;        // 状態
    Vector2 mPos;       // 位置
    Vector2 mSpeed;     // スピード
    Vector2 mAccel;     // 加速度
    MainView mMainView; // MainView
    Input mInput;       // 入力
    Stage mStage;       // ステージ
    Menu mMenu;         // メニュー
    int mFrameNo;       // フレーム番号
    int mPatternNo;     // パターン番号
    Toast toast;
    int mFlameX;
    float mFlameScl;

    // コンストラクタ
    public Cat() {
        mStatus = STAT_DEAD;
        double init_x = Math.random() * MainRenderer.CONTENTS_W;
        double init_y = Math.random() * MainRenderer.CONTENTS_H;
        mPos = new Vector2((float) init_x, (float) init_y);
        mSpeed = new Vector2(0.0f, 0.0f);
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
                }
                if (mFlameScl < 1.0f) {
                    mFlameScl += 0.03f;
                }
            }
            break;
            case STAT_PLAYING: {

                mPos.Add(mSpeed);
                if (mStage.hitTest(mPos.X, mPos.Y, 32.0f, 32.0f)) {
                    // 衝突した：状態を死亡へ
//                    mStatus = STAT_DEAD;
                }
                if (Zaru.histTest(mPos.X, mPos.Y, 32.0f, 32.0f, 0.5f, 0.5f)) {
                    mStatus = STAT_DEAD;
                    // ポイントも付与する
                    addPoint();
                }

                // 重力による加速度の補正
                if (Math.abs(Zaru.POS_X - mPos.X) > 5) {
                    mSpeed.Set(
                            (Zaru.POS_X - mPos.X) > 0 ? 5.0f : -5.0f,
                            (float) (Math.sin((mFlameX++ * 0.2f) % 360) - Math.sin(((mFlameX - 1) * 0.2f) % 360) * 3.0f)
                    );
                } else {
                    // 加速度によるスピード補正
                    mSpeed.Add(mAccel);
                    mAccel.X = mSpeed.X * -1;
                    mAccel.Y += ACCEL;
                }
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

        if (mStatus == STAT_DEAD) return;
        // ねこ
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(mPatternNo);
        params.getPos().X = mPos.X;
        params.getPos().Y = mPos.Y;
        params.getScl().X = 0.5f * mFlameScl;
        params.getScl().Y = 0.5f * mFlameScl;
        params.getOfs().X = 100.0f;
        params.getOfs().Y = 100.0f;

        if (mStatus == STAT_PLAYING) return;
        // くさ
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(Game.TEXNO_LEAF);
        params.getPos().X = mPos.X;
        params.getPos().Y = mPos.Y + 40;
        params.getScl().X = 0.5f;
        params.getScl().Y = 0.5f;
        params.getOfs().X = 100.0f;
        params.getOfs().Y = 100.0f;

    }

    public boolean growUp(int _texno) {
        if (mStatus != STAT_DEAD) return false;
        mPatternNo = _texno;
        mStatus = STAT_WAITING;
        mFlameX = 0;
        mFlameScl = 0.5f;
        return true;
    }

    // ゲームリセット
    void reset() {
        double init_x = Math.random() * MainRenderer.CONTENTS_W;
        double init_y = Math.random() * 425.0f; // TODO: 木と葉っぱの位置から計算すること

//        mStatus = STAT_WAITING;
        mPos.Set((float) init_x, (float) init_y);
        mSpeed.Set(0.0f, 0.0f);
        mAccel.Set(0.0f, 0.0f);
    }

    boolean touchTest() {
        float x = mInput.getX() - mPos.X;
        float y = mInput.getY() - mPos.Y;

        if (x > 0 && x < 60 && Math.abs(y) < 64) return true;
        return false;
    }

    void addPoint() {
        try {
            int point = CatTreeData.getInt(CatTreeData.POINT, 0);
            CatTreeData.setInt(CatTreeData.POINT, ++point);
            toast.setText(point + "ポイントゲットにゃ！");
            toast.show();
        } catch (Exception e) {

        }
    }
}
