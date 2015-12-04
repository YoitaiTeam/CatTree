package com.yoitai.cattree;

// ゲームの制御
public class Game {
    // テクスチャ番号
    public static final int TEXNO_BACK = 0;    // 背景
    public static final int TEXNO_CATTREE = 1; // ねこのなる木
    public static final int MENU01 = 2;        // メニューA
    public static final int MENU02 = 3;        // メニューB
    public static final int TEXNO_CHAR0 = 4;        // キャラクタ0
    public static final int TEXNO_CHAR1 = 5;        // キャラクタ1
    public static final int TEXNO_CHAR2 = 6;        // キャラクタ2
    public static final int TEXNO_ENEMY0 = 7;        // 敵キャラクタ0
    public static final int TEXNO_ENEMY1 = 8;        // 敵キャラクタ1
    public static final int TEXNO_BULLET = 9;        // 弾
    public static final int ALBUM01 = 10;        // アルバム
    public static final int BTN_CLOSE01 = 11;

    // メンバー変数
    MainActivity mMainActivity;
    MainRenderer mMyRenderer;
    Input mInput;

    // 内容が変化するゲーム情報
    long mFrameNo;
    Stage mStage;
    CAT[] mCAT;
    Menu mMenu;

    // コンストラクタ
    public Game() {
        mStage = new Stage();
        mCAT = new CAT[5];
        for (int i = 0; i < 5; i++) {
            mCAT[i] = new CAT();
        }
        mMenu = new Menu();
    }

    // viewの設定
    public void setView(MainView _view) {
        mMainActivity = _view.getMainActivity();
        mMyRenderer = _view.mMainRenderer;
        mInput = _view.mInput;

        mStage.setView(_view);

        for (int i = 0; i < 5; i++) {
            mCAT[i].setView(_view);
            mCAT[i].setInput(mInput);
            mCAT[i].setStage(mStage);
        }

        mMenu.setView(_view);
        mMenu.setInput(mInput);
    }

    // ゲーム初期化処理(MyRendererからonSurfaceCreated時に実行されます)
    public void gameInitialize() {
        // 各テクスチャ読み込み
        mMyRenderer.getTexture(TEXNO_BACK).readTexture(mMainActivity, "background.png", 512, 512, 256.0f, 256.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CATTREE).readTexture(mMainActivity, "tree.png", 512, 512, 256.0f, 256.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(MENU01).readTexture(mMainActivity, "menu01.png", 50, 50, 256.0f, 256.0f, 0.0f, 0.0f);
        mMyRenderer.getTexture(MENU02).readTexture(mMainActivity, "menu02.png", 50, 50, 256.0f, 256.0f, 130.0f, 0.0f);
        mMyRenderer.getTexture(TEXNO_CHAR0).readTexture(mMainActivity, "cat.png", 256, 256, 100.0f, 128.0f, -100.0f, -128.0f);
        mMyRenderer.getTexture(TEXNO_CHAR1).readTexture(mMainActivity, "ufo_fire1.png", 173, 138, 84.0f, 57.0f, -84.0f, -57.0f);
        mMyRenderer.getTexture(TEXNO_CHAR2).readTexture(mMainActivity, "ufo_fire2.png", 173, 138, 84.0f, 57.0f, -84.0f, -57.0f);
        mMyRenderer.getTexture(TEXNO_ENEMY0).readTexture(mMainActivity, "bird1.png", 86, 79, 44.0f, 40.0f, -44.0f, -40.0f);
        mMyRenderer.getTexture(TEXNO_ENEMY1).readTexture(mMainActivity, "bird2.png", 90, 79, 44.0f, 40.0f, -44.0f, -40.0f);
        mMyRenderer.getTexture(TEXNO_BULLET).readTexture(mMainActivity, "bullet.png", 26, 18, 13.0f, 8.0f, -13.0f, 8.0f);
        mMyRenderer.getTexture(ALBUM01).readTexture(mMainActivity, "album.png", 500, 300, 0.0f, 0.0f, -60.0f, -100.0f);
        mMyRenderer.getTexture(BTN_CLOSE01).readTexture(mMainActivity, "x.png", 60, 60, 0.0f, 0.0f, 280.0f, -120.0f);
    }

    // 毎フレーム処理(FPS毎にMainThreadから呼ばれます)
    public void frameFunction() {
        // == 以下 フレーム処理 ==
        // ステージフレーム処理
        mStage.frameFunction();

        // UFOのフレーム処理
        for (int i = 0; i < 5; i++) {
            mCAT[i].frameFunction();
        }

        mMenu.frameFunction();

        mFrameNo++;

        // == 以下 描画処理 ==
        // スクリーンクリア
        DrawParams params;
        params = mMyRenderer.allocDrawParams();
        params.setScreenClear(0.0f, 0.0f, 0.0f);

        // ステージ描画
        mStage.draw(0);

        // ねこの描画
        for (int i = 0; i < 5; i++) {
            mCAT[i].draw();
        }

        mMenu.draw();
    }
}
