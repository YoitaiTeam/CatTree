package com.yoitai.cattree;

import com.yoitai.glib.Vector2;

/**
 * Created by yuarak on 2015/12/04.
 */
public class Menu {

    // 状態
    public static final int MENU_OPEN = 1; //メニューが開いている
    public static final int MENU_CLOSE = 0; //メニューが閉じている

    // メンバ変数
    int mStatus;        // 状態
    Vector2 mPos;        // 位置
    MainView mMainView; // MainView
    Input mInput;       // 入力
    Stage mStage;       // ステージ
    int mFrameNo;       // フレーム番号
    int mPatternNo;    // パターン番号
    static boolean isBusy;

    // コンストラクタ
    public Menu() {
        mStatus = MENU_CLOSE;
        double init_x = MainRenderer.CONTENTS_W / 4;
        double init_y = MainRenderer.CONTENTS_H / 2;
        mPos = new Vector2((float) init_x, (float) init_y);
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
            case MENU_CLOSE: {

                // 各種メニューを開く＆音
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenShop()) {
                    // お店
                    mStatus = MENU_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM01;
                    isBusy(mStatus);

                } else if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenGoods()) {
                    // グッズ
                    mStatus = MENU_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM02;
                    isBusy(mStatus);

                } else if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenAlbum()) {
                    // 猫アルバム
                    mStatus = MENU_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM03;
                    isBusy(mStatus);

                } else if (mInput.checkStatus(Input.STATUS_DOWN) && touchOpenActive()) {
                    // 設定
                    mStatus = MENU_OPEN;
                    mPatternNo = Game.TEXNO_ALBUM04;
                    isBusy(mStatus);
                }

            }
            break;
            case MENU_OPEN: {
                // メニューを閉じる＆音
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchCloseMenu()) {
                    // 他の画面がタッチされた：閉じる
                    mStatus = MENU_CLOSE;
                    isBusy(mStatus);
                }
            }
            break;
        }
        isBusy = false;
        mFrameNo++;
    }

    // メニューの開閉時の音
    boolean isBusy(int mode) {

        if (!isBusy && mode == MENU_CLOSE) {
            mMainView.getSePlayer().play(R.raw.open);
            isBusy = true;

        } else if (!isBusy && mode == MENU_OPEN) {
            mMainView.getSePlayer().play(R.raw.close);
            isBusy = true;
        }

        return true;
    }

    // ゲームリセット
    void reset() {
        double init_x = Math.random() * MainRenderer.CONTENTS_W;
        double init_y = Math.random() * MainRenderer.CONTENTS_H;

        mStatus = MENU_CLOSE;
        mPos.Set((float) init_x, (float) init_y);
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

        if (mStatus == MENU_OPEN) {

            params = mMainView.getMainRenderer().allocDrawParams();
            params.setSprite(mPatternNo);
            params.getPos().X = mPos.X;
            params.getPos().Y = mPos.Y;
            params.getScl().X = 0.4f;
            params.getScl().Y = 0.3f;
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
