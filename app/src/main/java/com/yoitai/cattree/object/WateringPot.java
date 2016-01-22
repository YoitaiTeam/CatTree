package com.yoitai.cattree.object;

import com.yoitai.cattree.CatTreeData;
import com.yoitai.cattree.DrawParams;
import com.yoitai.cattree.Game;
import com.yoitai.cattree.Input;
import com.yoitai.cattree.MainView;
import com.yoitai.cattree.R;
import com.yoitai.glib.Vector2;

/**
 * じょうろ
 */
public class WateringPot {

    // 状態
    public static final int STAT_EMPTY = 0;
    public static final int STAT_FILL = 1;
    public static final int STAT_REMAIN = 2;

    // 定数
    static final float LIMIT_TIME = 10.0f * 1000.0f;

    // メンバ変数
    int mPatternNo;
    int mStatus;        // 状態
    float mWaterLevel;  // 水位
    Vector2 mPos;       // 位置
    MainView mMainView; // MainView
    Input mInput;       // 入力
    int mFrameNo;       // フレーム番号
    long mPouredTime;   // 水を注いだ時間

    // コンストラクタ
    public WateringPot() {
        mStatus = STAT_EMPTY;
        mWaterLevel = 100.0f;
        mPos = new Vector2(384.0f, 515.0f);
        mFrameNo = 0;
    }

    // setter
    public void setView(MainView _view) {
        mMainView = _view;
    }

    public void setInput(Input _input) {
        mInput = _input;
    }

    public void setPouredTime(long _time) {
        poureWater(_time);
    }

    // 毎フレーム処理
    public void frameFunction() {
        switch (mStatus) {
            case STAT_EMPTY:
            case STAT_REMAIN: {
                if (mInput.checkStatus(Input.STATUS_DOWN) && touchTest()) {
                    // 画面がタッチされた：水を補充する
                    mStatus = STAT_FILL;
                    mMainView.getSePlayer().play(R.raw.pour);
                    poureWater(System.currentTimeMillis());
                } else if (mPatternNo == Game.TEXNO_POT_FILL) {
                    mWaterLevel = (1 - ((System.currentTimeMillis() - mPouredTime) / LIMIT_TIME)) * 100.0f;
                    mWaterLevel = Math.max(mWaterLevel, 0.0f);
                }
            }
            break;
            case STAT_FILL: {
                mStatus = STAT_REMAIN;
                mWaterLevel = 100.0f;
            }
            break;
        }

        mFrameNo++;
    }

    // 描画
    public void draw() {
        DrawParams params;
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(mPatternNo);
        params.getPos().X = mPos.X;
        params.getPos().Y = mPos.Y;
        params.getScl().X = 0.5f;
        params.getScl().Y = 0.5f;
        params.getOfs().X = 100.0f;
        params.getOfs().Y = mWaterLevel;
    }

    boolean touchTest() {
        return (Math.abs(mInput.getX() - mPos.X) < 62.5 && Math.abs(mInput.getY() - mPos.Y) < 61.25);
    }

    void poureWater(long _time) {
        CatTreeData.setLong(CatTreeData.POURED_TIME, _time);
        mPouredTime = _time;
    }

    float residualQuantity() {
        return mWaterLevel;
    }

    public void setPatternNo(int _no) {
        mPatternNo = _no;
    }
}
