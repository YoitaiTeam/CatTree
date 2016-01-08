package com.yoitai.cattree;

import com.yoitai.glib.Vector2;

/**
 * Created by yuarak on 2015/12/04.
 */
public class Menu {
    // 状態
    public static final int DISP_CLOSE = 0;
    public static final int DISP_OPEN = 1;
    public static final int DISP_SELECT = 2;

    // パラメータ
    public static final float SPEED = 2.0f;            // キャラ横方向スピード
    public static final float ACCEL = 0.02f;        // 重力加速度
    public static final float PUSH_SPEED_Y = -5.0f;    // プッシュ時のスピード

    public static final int MENU_OPEN = 1;
    public static final int MENU_CLOSE = 0;

    // メンバ変数
    int mStatus;        // 状態
    Vector2 mPos;        // 位置
    Vector2 mSpeed;    // スピード
    Vector2 mAccel;    // 加速度
    MainView mMainView; // MainView
    Input mInput;       // 入力
    Stage mStage;       // ステージ
    int mFrameNo;       // フレーム番号
    int mPatternNo;    // パターン番号

    // コンストラクタ
    public Menu() {
        mStatus = DISP_CLOSE;
//        double init_x = Math.random() * MainRenderer.CONTENTS_W;
        double init_x = MainRenderer.CONTENTS_W / 4;
//        double init_y = Math.random() * MainRenderer.CONTENTS_H;
        double init_y = MainRenderer.CONTENTS_H / 2;
        mPos = new Vector2((float) init_x, (float) init_y);
        mSpeed = new Vector2(SPEED, 0.0f);
        mAccel = new Vector2(0.0f, 0.0f);
        mFrameNo = 0;
        mPatternNo = 10;
    }

    // setter
    public void setView(MainView _view) {
        mMainView = _view;
    }

    public void setInput(Input _input) {
        mInput = _input;
    }

    public void setStage(Stage _stage) {
        mStage = _stage;
    }

    // 毎フレーム処理
    public void frameFunction() {
        switch (mStatus) {
            case DISP_CLOSE: {

                // 待ち状態
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenShop()) {

                    // 画面がタッチされた：開始へ
                    mStatus = DISP_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM01;
                }
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenGoods()) {

                    // 画面がタッチされた：開始へ
                    mStatus = DISP_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM02;
                }
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenAlbum()) {
                    //mInput.checkStatus(Input.STATUS_DOWN) &&

                    // 画面がタッチされた：開始へ
                    mStatus = DISP_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM03;
                }
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenActive()) {
                    //mInput.checkStatus(Input.STATUS_DOWN) &&

                    // 画面がタッチされた：開始へ
                    mStatus = DISP_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM04;
                }
            }
            break;
            case DISP_OPEN: {
                //
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchCloseMenu()) {
                    // 他の画面がタッチされた：閉じる
                    mStatus = DISP_CLOSE;
                }
            }
            break;
            /*case DISP_SELECT: {
                //
            }
            break;*/
        }

        mFrameNo++;
    }

    // ゲームリセット
    void reset() {
        double init_x = Math.random() * MainRenderer.CONTENTS_W;
        double init_y = Math.random() * MainRenderer.CONTENTS_H;

        mStatus = DISP_CLOSE;
        mPos.Set((float) init_x, (float) init_y);
        mSpeed.Set(SPEED, 0.0f);
        mAccel.Set(0.0f, 0.0f);
    }

    public boolean touchOpenShop() {
        float x = mInput.getX();
        float y = mInput.getY();

        if (x > 0 && x < 100 && Math.abs(y) > 550) return true;
        return false;
    }

    public boolean touchOpenGoods() {
        float x = mInput.getX();
        float y = mInput.getY();

        if (x > 120 && x < 220 && Math.abs(y) > 550) return true;
        return false;
    }

    public boolean touchOpenAlbum() {
        float x = mInput.getX();
        float y = mInput.getY();
//        Log.i("touchOpenA", mInput.getX() + " " + mInput.getY() +" "+Math.abs(y));

        if (x > 240 && x < 340 && Math.abs(y) > 550) return true;
        return false;
    }

    public boolean touchOpenActive() {
        float x = mInput.getX();
        float y = mInput.getY();

        if (x > 360 && x < 460 && Math.abs(y) > 550) return true;
        return false;
    }

    public boolean touchCloseMenu() {
        float x = mInput.getX();
        float y = mInput.getY();

        if (x > 400 && x < 460 && Math.abs(y) < 250 && Math.abs(y) > 210) return true;
        return false;
    }

    // メニューの開閉状態チェック
    public boolean isCloseMenu() {
        // メニューを閉じている時
        if (mStatus == MENU_CLOSE) {
            return (true);
        } else {
            if (touchCloseMenu()) {
                //メニュー閉じるボタンのみ
                return (true);
            }
        }
        return false;
    }

    // 描画
    public void draw() {
        DrawParams params;

        if (mStatus == DISP_OPEN) {

            params = mMainView.getMainRenderer().allocDrawParams();
            params.setSprite(mPatternNo);
            params.getPos().X = mPos.X;
            params.getPos().Y = mPos.Y;
            params = mMainView.getMainRenderer().allocDrawParams();
            params.setSprite(Game.BTN_CLOSE01);
            params.getPos().X = mPos.X;
            params.getPos().Y = mPos.Y;
            mInput.mMenuStatus = MENU_OPEN;
        } else {
            mInput.mMenuStatus = MENU_CLOSE;
        }
    }

}
