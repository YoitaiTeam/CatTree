package com.yoitai.cattree;

import com.yoitai.glib.Calc;
import com.yoitai.glib.Vector2;

// キャラクタ管理クラス
public class UFO {
    // 状態
    public static final int STAT_WAITING = 0;
    public static final int STAT_PLAYING = 1;
    public static final int STAT_DEAD    = 2;

    // パラメータ
    public static final float SPEED = 2.0f;			// キャラ横方向スピード
    public static final float ACCEL = 0.02f;		// 重力加速度
    public static final float PUSH_SPEED_Y = -5.0f;	// プッシュ時のスピード

    // メンバ変数
    int mStatus;		// 状態
    Vector2 mPos;		// 位置
    Vector2 mSpeed;	// スピード
    Vector2 mAccel;	// 加速度
    MainView mMainView; // MainView
    Input mInput;       // 入力
    Stage mStage;       // ステージ
    int mFrameNo;       // フレーム番号
    int mPatternNo;    // パターン番号

    // コンストラクタ
    public UFO()
    {
        mStatus = STAT_WAITING;
        mPos = new Vector2(MainRenderer.CONTENTS_W/4 , MainRenderer.CONTENTS_H/2);
        mSpeed = new Vector2(SPEED ,0.0f);
        mAccel = new Vector2(0.0f ,0.0f);
        mFrameNo = 0;
        mPatternNo = 0;
    }

    // setter
    public void setView(MainView _view){mMainView = _view;}
    public void setInput(Input _input){mInput = _input;}
    public void setStage(Stage _stage){mStage = _stage;}

    // 毎フレーム処理
    public void frameFunction()
    {
        switch(mStatus){
            case STAT_WAITING:{
                // 待ち状態
                mPatternNo = Game.TEXNO_CHAR0;
                if( mInput.checkStatus(Input.STATUS_DOWN) ){
                    // 画面がタッチされた：開始へ
                    mStatus = STAT_PLAYING;
                }
            }break;
            case STAT_PLAYING:{
                // プレイ中
                if( mInput.checkStatus(Input.STATUS_PUSH) ){
                    // 画面をプッシュされた
                    mSpeed.Y = PUSH_SPEED_Y;
                    mAccel.Y = 0.0f;
                }

                mPos.Add(mSpeed);
                if( mStage.hitTest(mPos.X, mPos.Y, 32.0f, 32.0f) ){
                    // 衝突した：状態を死亡へ
                    mStatus = STAT_DEAD;
                }

                // 加速度によるスピード補正
                mSpeed.Add(mAccel);

                // 重力による加速度の補正
                mAccel.Y += ACCEL;

                if( mSpeed.Y < 0.0f ) {
                    if ((mFrameNo % 4) < 2) {
                        mPatternNo = Game.TEXNO_CHAR1;
                    } else {
                        mPatternNo = Game.TEXNO_CHAR2;
                    }
                }else{
                    mPatternNo = Game.TEXNO_CHAR0;
                }
            }break;
            case STAT_DEAD:{
                // 死亡：初期化
                mPatternNo = Game.TEXNO_CHAR0;
                reset();
            }break;
        }

        mFrameNo++;
    }

    // 描画
    public void draw()
    {
        DrawParams params;

        if( mStatus != STAT_DEAD ){
            params = mMainView.getMainRenderer().allocDrawParams();
            params.setSprite(mPatternNo);
            params.getPos().X = MainRenderer.CONTENTS_W/4;
            params.getPos().Y = mPos.Y;
            params.getScl().X = 0.5f;
            params.getScl().Y = 0.5f;
            params.setRot(Calc.CalcAngleRad(mSpeed.X, mSpeed.Y/8, false));

            if( mStatus == STAT_WAITING ) {
                // 静止中のみ
                // sinカーブにのせて揺らしてみる
                params.getPos().X += Math.sin(Calc.PI * ((mFrameNo * 10) % 360) / 180.0f) * 3;
                params.getPos().Y += Math.sin(Calc.PI * ((mFrameNo * 8) % 360) / 180.0f) * 2;
            }
        }
    }

    // ゲームリセット
    void reset()
    {
        mStatus = STAT_WAITING;
        mPos.Set(MainRenderer.CONTENTS_W / 4, MainRenderer.CONTENTS_H / 2);
        mSpeed.Set(SPEED, 0.0f);
        mAccel.Set(0.0f, 0.0f);
    }
}
