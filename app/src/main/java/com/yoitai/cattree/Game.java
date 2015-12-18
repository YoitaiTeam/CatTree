package com.yoitai.cattree;

import android.media.MediaPlayer;

import com.yoitai.cattree.object.WateringPot;

// ゲームの制御
public class Game {
    // ゲーム設定
    public static final int CAT_KIND_NUM = 20;

    // テクスチャ番号
    public static final int TEXNO_BACK = 0;         // 背景
    public static final int TEXNO_CATTREE = 1;      // ねこのなる木
    public static final int TEXNO_TBLOCK = 2;       // 障害物上
    public static final int TEXNO_BBLOCK = 3;       // 障害物下
    public static final int TEXNO_BASE_CAT = 100;   // 猫TEXTNOのベース
    public static final int TEXNO_CAT0 = 101;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT1 = 102;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT2 = 103;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT3 = 104;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT4 = 105;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT5 = 106;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT6 = 107;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT7 = 108;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT8 = 109;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT9 = 110;       // TODO:猫の名前書きます
    public static final int TEXNO_CAT10 = 111;      // TODO:猫の名前書きます
    public static final int TEXNO_CAT11 = 112;      // TODO:猫の名前書きます
    public static final int TEXNO_CAT12 = 113;      // TODO:猫の名前書きます
    public static final int TEXNO_CAT13 = 114;      // TODO:猫の名前書きます
    public static final int TEXNO_CAT14 = 115;      // TODO:猫の名前書きます
    public static final int TEXNO_RARE_CAT1 = 116;  // TODO:猫の名前書きます
    public static final int TEXNO_RARE_CAT2 = 117;  // TODO:猫の名前書きます
    public static final int TEXNO_RARE_CAT3 = 118;  // TODO:猫の名前書きます
    public static final int TEXNO_RARE_CAT4 = 119;  // TODO:猫の名前書きます
    public static final int TEXNO_RARE_CAT5 = 120;  // TODO:猫の名前書きます
    public static final int TEXNO_ENEMY0 = 7;       // 敵キャラクタ0
    public static final int TEXNO_ENEMY1 = 8;       // 敵キャラクタ1
    public static final int TEXNO_BULLET = 9;       // 弾
    public static final int TEXNO_WARTERING_POT = 10;       // じょうろ
    public static final int TEXNO_WARTERING_POT_FRAME = 11; // じょうろ
    public static final int TEXNO_SHOP = 12;        // じょうろ

    // メンバー変数
    MainActivity mMainActivity;
    MainRenderer mMyRenderer;
    SePlayer mSePlayer;
    MediaPlayer mBgmPlayer;

    Input mInput;

    // 内容が変化するゲーム情報
    long mFrameNo;
    Stage mStage;
    CatTree mCatTree;
    Shop mShop;
    WateringPot[] mWateringPot = new WateringPot[2];

    // コンストラクタ
    public Game() {
        mStage = new Stage();
        mCatTree = new CatTree();
        mShop = new Shop();
        mWateringPot[0] = new WateringPot();
        mWateringPot[0].setPatternNo(Game.TEXNO_WARTERING_POT_FRAME);
        mWateringPot[1] = new WateringPot();
        mWateringPot[1].setPatternNo(Game.TEXNO_WARTERING_POT);
    }

    // viewの設定
    public void setView(MainView _view) {
        mMainActivity = _view.getMainActivity();
        mMyRenderer = _view.mMainRenderer;
        mSePlayer = _view.mSePlayer;
        mBgmPlayer = _view.mBgmPlayer;
        mInput = _view.mInput;

        mStage.setView(_view);

        mCatTree.setView(_view);
        mCatTree.setInput(mInput);
        mCatTree.setStage(mStage);

        mShop.setView(_view);
        mShop.setInput(mInput);

        mWateringPot[0].setView(_view);
        mWateringPot[0].setInput(mInput);
        mWateringPot[1].setView(_view);
        mWateringPot[1].setInput(mInput);
    }

    // ゲーム初期化処理(MyRendererからonSurfaceCreated時に実行されます)
    public void gameInitialize() {
        // 各テクスチャ読み込み
        mMyRenderer.getTexture(TEXNO_BACK).readTexture(mMainActivity, "background.png", 512, 512, 256.0f, 256.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CATTREE).readTexture(mMainActivity, "tree.png", 512, 512, 256.0f, 256.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_TBLOCK).readTexture(mMainActivity, "tpole.png", 109, 512, 0.0f, 498.0f, 0.0f, -498.0f);
        mMyRenderer.getTexture(TEXNO_BBLOCK).readTexture(mMainActivity, "bpole.png", 109, 512, 0.0f, 16.0f, 0.0f, -16.0f);
        mMyRenderer.getTexture(TEXNO_CAT0).readTexture(mMainActivity, "cat.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT1).readTexture(mMainActivity, "cat2.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT2).readTexture(mMainActivity, "cat3.png", 72, 128, 36.0f, 0.0f, -36.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CAT3).readTexture(mMainActivity, "cat.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT4).readTexture(mMainActivity, "cat2.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT5).readTexture(mMainActivity, "cat3.png", 72, 128, 36.0f, 0.0f, -36.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CAT6).readTexture(mMainActivity, "cat.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT7).readTexture(mMainActivity, "cat2.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT8).readTexture(mMainActivity, "cat3.png", 72, 128, 36.0f, 0.0f, -36.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CAT9).readTexture(mMainActivity, "cat.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT10).readTexture(mMainActivity, "cat2.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT11).readTexture(mMainActivity, "cat3.png", 72, 128, 36.0f, 0.0f, -36.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CAT12).readTexture(mMainActivity, "cat.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT13).readTexture(mMainActivity, "cat2.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_CAT14).readTexture(mMainActivity, "cat3.png", 72, 128, 36.0f, 0.0f, -36.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_RARE_CAT1).readTexture(mMainActivity, "cat.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_RARE_CAT2).readTexture(mMainActivity, "cat2.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_RARE_CAT3).readTexture(mMainActivity, "cat3.png", 72, 128, 36.0f, 0.0f, -36.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_RARE_CAT4).readTexture(mMainActivity, "cat.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_RARE_CAT5).readTexture(mMainActivity, "cat2.png", 70, 105, 35.0f, 52.0f, -35.0f, -52.0f);
        mMyRenderer.getTexture(TEXNO_ENEMY0).readTexture(mMainActivity, "bird1.png", 86, 79, 44.0f, 40.0f, -44.0f, -40.0f);
        mMyRenderer.getTexture(TEXNO_ENEMY1).readTexture(mMainActivity, "bird2.png", 90, 79, 44.0f, 40.0f, -44.0f, -40.0f);
        mMyRenderer.getTexture(TEXNO_BULLET).readTexture(mMainActivity, "bullet.png", 26, 18, 13.0f, 8.0f, -13.0f, 8.0f);
        mMyRenderer.getTexture(TEXNO_WARTERING_POT).readTexture(mMainActivity, "watering_pot.png", 256, 230, 0.0f, 26.0f, 0.0f, -26.0f);
        mMyRenderer.getTexture(TEXNO_WARTERING_POT_FRAME).readTexture(mMainActivity, "watering_pot_frame.png", 256, 230, 0.0f, 26.0f, 0.0f, -26.0f);
        mMyRenderer.getTexture(TEXNO_SHOP).readTexture(mMainActivity, "shop.png", 128, 128, 0.0f, 0.0f, 0.0f, 0.0f);

        // 各SE読み込み
        mSePlayer.initialize(mMainActivity);
        mSePlayer.load(mMainActivity, R.raw.cat_cry1);
        mSePlayer.load(mMainActivity, R.raw.cat_cry2);
        mSePlayer.load(mMainActivity, R.raw.open);
        mSePlayer.load(mMainActivity, R.raw.close);
        mSePlayer.load(mMainActivity, R.raw.pour);

        // BGM設定
        mBgmPlayer.setLooping(true);
        mBgmPlayer.start();

        // データベース初期化
        CatTreeData.init(mMainActivity);
    }

    // 毎フレーム処理(FPS毎にMainThreadから呼ばれます)
    public void frameFunction() {
        // == 以下 フレーム処理 ==
        // ステージフレーム処理
        mStage.frameFunction();

        // 猫のなる木のフレーム処理
        mCatTree.frameFunction();

        // お店のフレーム処理
        mShop.frameFunction();

        // じょうろ描画
        mWateringPot[0].frameFunction();
        mWateringPot[1].frameFunction();

        mFrameNo++;

        // == 以下 描画処理 ==
        // スクリーンクリア
        DrawParams params;
        params = mMyRenderer.allocDrawParams();
        params.setScreenClear(0.0f, 0.0f, 0.0f);

        // ステージ描画
        mStage.draw(0);

        // ねこのなる木描画
        mCatTree.draw();

        // お店描画
        mShop.draw();

        // じょうろ描画
        mWateringPot[0].draw();
        mWateringPot[1].draw();
    }
}
