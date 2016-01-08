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
    public static final int MENU01 = 2;        // メニューA
    public static final int MENU02 = 3;        // メニューB
    public static final int TEXNO_WOOD_STEM = 4;    // ねこのなる木の幹
    public static final int TEXNO_WOOD_LEAF = 5;    // ねこのなるきの葉
    public static final int TEXNO_ZARU = 6;         // ザル
    public static final int TEXNO_ENEMY0 = 7;       // 敵キャラクタ0
    public static final int TEXNO_ENEMY1 = 8;       // 敵キャラクタ1
    public static final int TEXNO_BULLET = 9;       // 弾
    public static final int ALBUM01 = 10;           // アルバム1
    public static final int BTN_CLOSE01 = 11;
    public static final int ALBUM02 = 12;           // アルバム2
    public static final int TEXNO_POT_FILL = 13;    // じょうろ
    public static final int TEXNO_POT_EMPTY = 14;   // じょうろ
    public static final int TEXNO_SHOP = 15;        // じょうろ
    public static final int TEXNO_LEAF = 16;        // 草
    public static final int TEXNO_BASE_CAT = 100;   // 猫TEXTNOのベース
    public static final int TEXNO_CAT0 = 101;       // まいどさん
    public static final int TEXNO_CAT1 = 102;       // ぐれいさん
    public static final int TEXNO_CAT2 = 103;       // らっきーさん
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

    // メンバー変数
    MainActivity mMainActivity;
    MainRenderer mMyRenderer;
    SePlayer mSePlayer;
    MediaPlayer mBgmPlayer;
    Menu mMenu;

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
        mWateringPot[0].setPatternNo(Game.TEXNO_POT_FILL);
        mWateringPot[1] = new WateringPot();
        mWateringPot[1].setPatternNo(Game.TEXNO_POT_EMPTY);
        mMenu = new Menu();
    }

    // viewの設定
    public void setView(MainView _view) {
        mMainActivity = _view.getMainActivity();
        mMyRenderer = _view.mMainRenderer;
        mSePlayer = _view.mSePlayer;
        mBgmPlayer = _view.mBgmPlayer;
        mInput = _view.mInput;

        mStage.setView(_view);

        mMenu.setView(_view);
        mMenu.setInput(mInput);

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
        mMyRenderer.getTexture(TEXNO_BACK).readTexture(mMainActivity, "main_bg.png", 960, 1440, 0.0f, 0.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CATTREE).readTexture(mMainActivity, "tree.png", 512, 512, 256.0f, 256.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(MENU01).readTexture(mMainActivity, "menu01.png", 50, 50, 256.0f, 256.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(MENU02).readTexture(mMainActivity, "menu02.png", 50, 50, 256.0f, 256.0f, 130.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_WOOD_STEM).readTexture(mMainActivity, "wood_stem.png", 850, 1012, 425.0f, 506.0f, -425.0f, -506.0f);
        mMyRenderer.getTexture(TEXNO_WOOD_LEAF).readTexture(mMainActivity, "wood_leaf.png", 850, 1012, 425.0f, 506.0f, -425.0f, -506.0f);
        mMyRenderer.getTexture(TEXNO_ZARU).readTexture(mMainActivity, "zaru.png", 240, 230, 120.0f, 115.0f, -120.0f, -115.0f);
        mMyRenderer.getTexture(TEXNO_CAT0).readTexture(mMainActivity, "cat_01_maido.png", 125, 185, 62.5f, 92.5f, -52.5f, -92.5f);
        mMyRenderer.getTexture(TEXNO_CAT1).readTexture(mMainActivity, "cat_02_gray.png", 102, 182, 61.0f, 91.0f, -61.0f, -91.0f);
        mMyRenderer.getTexture(TEXNO_CAT2).readTexture(mMainActivity, "cat_03_lucky.png", 208, 192, 104.0f, 96.0f, -104.0f, -96.0f);
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
        mMyRenderer.getTexture(TEXNO_POT_FILL).readTexture(mMainActivity, "pot_fill.png", 250, 205, 125.0f, 102.5f, -125.0f, -102.5f);
        mMyRenderer.getTexture(TEXNO_POT_EMPTY).readTexture(mMainActivity, "pot_empty.png", 250, 205, 125.0f, 102.5f, -125.0f, -102.5f);
        mMyRenderer.getTexture(TEXNO_SHOP).readTexture(mMainActivity, "shop.png", 128, 128, 0.0f, 0.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(ALBUM01).readTexture(mMainActivity, "album.png", 500, 300, 0.0f, 0.0f, -60.0f, -100.0f);
        mMyRenderer.getTexture(BTN_CLOSE01).readTexture(mMainActivity, "x.png", 60, 60, 0.0f, 0.0f, 280.0f, -120.0f);
        mMyRenderer.getTexture(ALBUM02).readTexture(mMainActivity, "album2.png", 500, 300, 0.0f, 0.0f, -60.0f, -100.0f);
        mMyRenderer.getTexture(TEXNO_LEAF).readTexture(mMainActivity, "cat_leaf.png", 170, 90, 85.0f, 45.0f, -85.0f, -45.0f);

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

        mMenu.frameFunction();

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

        // メニュー描画
        mMenu.draw();
    }
}
