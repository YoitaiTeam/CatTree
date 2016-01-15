package com.yoitai.cattree;

import android.util.Log;
import android.view.MotionEvent;

public class Input {
    // ステータス
    public static final int STATUS_DOWN = 0x00000001;                // 押されている状態
    public static final int STATUS_UP = 0x00000002;                // 離された瞬間
    public static final int STATUS_PUSH = 0x00000004;                // 押された瞬間

    // メンバー
    int mCurPosX;                // 現在の状態(MotionEventからの入力)
    int mCurPosY;
    int mCurStatus;
    int mPosX;                    // フレーム処理で使う状態(1/60毎に確定)
    int mPosY;
    int mStatus;
    int mMenuStatus;
    int mScreenW;                // スクリーンサイズ
    int mScreenH;
    int mContentX;            // 内容物範囲
    int mContentY;
    int mContentW;
    int mContentH;

    // コンストラクタ
    public Input() {
    }

    // 初期化処理
    public void initialize(int _sw, int _sh) {
        initialize(_sw, _sh, 0, 0, _sw, _sh);
    }

    public void initialize(int _sw, int _sh, int _cx, int _cy, int _cw, int _ch) {
        mCurPosX = 0;
        mCurPosY = 0;
        mCurStatus = 0;
        mPosX = 0;
        mPosY = 0;
        mStatus = 0;
        mMenuStatus = 0;
        mScreenW = _sw;
        mScreenH = _sh;
        mContentX = _cx;
        mContentY = _cy;
        mContentW = _cw;
        mContentH = _ch;
    }

    // リセット
    public void reset() {
        initialize(mScreenW, mScreenH, mContentX, mContentY, mContentW, mContentH);
    }

    // イベント処理
    public synchronized void eventFunction(MotionEvent _ev) {
        switch (_ev.getAction()) {
            // 押した/動いた
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mCurPosX = (int) _ev.getX();
                mCurPosY = (int) _ev.getY();
                mCurStatus = STATUS_DOWN;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if ((mCurStatus & STATUS_DOWN) != 0) {
                    mCurStatus = 0;
                }
                break;
        }
    }

    // フレーム処理
    public synchronized void frameFunction() {
        mPosX = mCurPosX;
        mPosY = mCurPosY;
        if ((mStatus & STATUS_DOWN) != 0) {
            if ((mCurStatus & STATUS_DOWN) != 0) {
                mStatus = mCurStatus;
            } else {
                mStatus = mCurStatus | STATUS_UP;
            }
        } else {
            if ((mCurStatus & STATUS_DOWN) != 0) {
                mStatus = mCurStatus | STATUS_PUSH;
            } else {
                mStatus = mCurStatus;
            }
        }
    }

    // 状態チェック
    public boolean checkStatus(int _status) {
        if ((mStatus & _status) == _status) return (true);
        return (false);
    }

    // X座標取得
    public float getX() {
        return ((mPosX - mContentX) * (float) mContentW / (float) mScreenW);
    }

    // Y座標取得
    public float getY() {
        return ((mPosY - mContentY) * (float) mContentH / (float) mScreenH);
    }
}
