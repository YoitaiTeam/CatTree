package com.yoitai.cattree;

import com.yoitai.glib.Vector2;

// 効果音テスト
public class Shop {

    Vector2 mPos;       // 位置
    MainView mMainView; // MainView
    Input mInput;       // 入力
    static boolean isOpen;
    static boolean isBusy;

    public Shop() {
        mPos = new Vector2((float) MainRenderer.CONTENTS_W / 10, (float) MainRenderer.CONTENTS_H / 10);
    }

    public void setView(MainView _view) {
        mMainView = _view;
    }

    public void setInput(Input _input) {
        mInput = _input;
    }

    // 毎フレーム処理
    public void frameFunction() {
        if (!isBusy && mInput.checkStatus(Input.STATUS_DOWN) && touchTest()) {
            if (isOpen) {
                mMainView.getSePlayer().play(R.raw.close);
            } else {
                mMainView.getSePlayer().play(R.raw.open);
            }
            isOpen = !isOpen;
            isBusy = true;
        } else if (mInput.checkStatus(Input.STATUS_UP)) {
            isBusy = false;
        }
    }

    boolean touchTest() {
        float x = mInput.getX() - mPos.X;
        float y = mInput.getY() - mPos.Y;

        if (x > 0 && x < 128 && Math.abs(y) < 128) return true;
        return false;
    }

    // 描画処理
    public void draw() {
        DrawParams params;
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(Game.TEXNO_SHOP);
        params.getPos().X = mPos.X;
        params.getPos().Y = mPos.Y;
        params.getScl().X = 0.5f;
        params.getScl().Y = 0.5f;
        params.getOfs().X = 100.0f;
        params.getOfs().Y = 100.0f;
    }
}
