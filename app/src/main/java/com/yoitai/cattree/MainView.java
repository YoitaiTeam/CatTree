package com.yoitai.cattree;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MainView extends GLSurfaceView {
	// メンバー変数
	boolean mInitFlag;
	MainRenderer mMainRenderer;
	MainActivity mMainActivity;
	MainThread mMainThread;
	Input mInput;
	MainThreadCore mMainThreadCore;

	// ActivityやViewが破棄されてもいいようにstaticで残す
	static Game sGame = new Game();

	// コンストラクタ
	public MainView(Context _context)
	{
		super(_context);

		// MainActivityインスタンスの保持
		mMainActivity = (MainActivity)_context;

		// GLSurfaceのレンダリングクラスのインスタンス化
		mMainRenderer = new MainRenderer(this);

		// 入力管理クラスのインスタンス化
		mInput = new Input();

		// ゲーム制御クラスへViewセット
		sGame.setView(this);

		// 初期化完了フラグ初期化
		mInitFlag = false;

		// スレッドのコアルーチンクラスのインスタンス化
		mMainThreadCore = new MainThreadCore(this);

		// スレッドクラスのインスタンス化
		mMainThread = new MainThread(mMainThreadCore);

		// OpenGL初期化
//		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);		// (デバッグ出力)
		setEGLContextClientVersion(2);									// OpenGL ES 2.0を使う
		setRenderer(mMainRenderer);									// このViewで使うレンダラーを設定
		setRenderMode(RENDERMODE_WHEN_DIRTY);						// GLSurfaceView側で連続描画させない
	}

	// 初期化(MainRendererからonSurfaceCreated時に実行されます)
	void initialize()
	{
		// 初期化が既に終わっていれば無視
		if( checkInitialized() == true )return;

		// 入力初期化：拡縮対応含
		int w = getWidth();
		int h = getHeight();
		if( w <= h ){
			mInput.initialize(w, w, 0, (h - w) / 2, MainRenderer.CONTENTS_W, MainRenderer.CONTENTS_W);
		}else{
			mInput.initialize(h, h, (w - h) / 2, 0, MainRenderer.CONTENTS_W, MainRenderer.CONTENTS_W);
		}

		// ゲームの初期化処理
		sGame.gameInitialize();

		// 初期化完了フラグをセット
		setInitialized();

		// メインスレッド開始
		mMainThread.start();
	}

	// 毎フレーム処理(FPS毎にMainThreadCoreから呼ばれます)
	public void frameFunction(DrawParams[] _currentDrawParams)
	{
		// 入力処理
		mInput.frameFunction();

		// フレーム処理
		sGame.frameFunction();
	}

	// OSからのタッチ入力イベントを処理
	@Override
	public boolean onTouchEvent(MotionEvent _event)
	{
		// 入力処理
		mInput.eventFunction(_event);

		return(true);
	}

	// 初期化完了フラグ制御
	public synchronized void setInitialized(){setInitialized(true);}
	public synchronized void setInitialized(boolean _flag){mInitFlag = _flag;}
	public synchronized boolean checkInitialized(){return(mInitFlag);	}

	// MainRenderer取得
	public MainRenderer getMainRenderer(){return(mMainRenderer);}
	public MainActivity getMainActivity(){return(mMainActivity);}

	@Override
	public void onResume()
	{
		super.onResume();

		if( checkInitialized() ) {
			// スレッド開始:再利用できないので再度作成して実行
			mMainThread = new MainThread(mMainThreadCore);
			mMainThread.start();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();

		if( checkInitialized() ){
			// スレッドの停止：再利用できないので止める
			mMainThread.setStopRequestFlag();
			try {
				mMainThread.join();
			}catch(Exception e){
			}
			mMainThread = null;
		}
	}
}
