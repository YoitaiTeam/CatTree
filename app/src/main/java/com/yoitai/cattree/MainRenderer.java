package com.yoitai.cattree;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class MainRenderer implements GLSurfaceView.Renderer {
	// 定義
	public static final int MAX_TEXTURE = 10;		// 最大テクスチャ数
	public static final int MAX_SHADER  = 1;		// 最大シェーダ数

	public static final int SHADER_2D   = 0;		// ２Ｄ用シェーダ

	public static final int CONTENTS_W  = 480;		// コンテンツの幅
	public static final int CONTENTS_H  = 480;		// コンテンツの高さ

	public static final int MAX_DRAWPARAMS = 200;	// 最大DrawParams数

	// メンバー変数
	MainView mMainView;
	boolean mRenderingStatus;

	// テクスチャ・シェーダ
	Texture [] mTextures;
	Shader [] mShaders;

	// 描画パラメータ管理
	DrawParams [][] mDrawParams;
	DrawParams [] mRenderingDrawParams;
	int mTargetDrawParamsNo;
	int mCurDrawParamNo;

	// コンストラクタ
	public MainRenderer(MainView _con)
	{
		// アタッチされるMainViewのインスタンスを保持
		mMainView = _con;

		// 描画中フラグ
		mRenderingStatus = false;

		// テクスチャクラス作成
		mTextures = new Texture [MAX_TEXTURE];
		for(int i=0;i<MAX_TEXTURE;i++){
			mTextures[i] = new Texture();
		}

		// シェーダ作成
		mShaders = new Shader [MAX_SHADER];
		for(int i=0;i<MAX_SHADER;i++){
			mShaders[i] = new Shader();
		}

		// 描画用ダブルバッファ確保
		mDrawParams = new DrawParams[2][];
		for(int i=0;i<2;i++){
			mDrawParams[i] = new DrawParams[MAX_DRAWPARAMS];
			for(int j=0;j<MAX_DRAWPARAMS;j++){
				mDrawParams[i][j] = new DrawParams();
			}
		}
		mTargetDrawParamsNo = 0;
		mCurDrawParamNo = 0;
	}

	// OpenGL サーフェス作成時に呼び出されるメソッド
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		if( mMainView.checkInitialized() == false ){
			// グラフィックの初期化も行われるのでこのタイミングでゲームの初期化
			mMainView.initialize();

			// シェーダーの初期化
			mShaders[SHADER_2D].compileShader(Shader.VSHADER_2D, Shader.PSHADER_TEXTURE2D);
		}else{
			// 既に初期化済：リセット
			reset();
		}
	}

	// OpenGL サーフェス変更時に呼び出されるメソッド
	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h)
	{
	}

	// OpenGL サーフェス描画時に呼び出されるメソッド
	@Override
	public void onDrawFrame(GL10 gl) {
		// 描画リクエストが発生しているか？
		if( checkRenderingRequest() == false )return;

		// ビューポート設定 ... 描画範囲
		int w = mMainView.getWidth();
		int h = mMainView.getHeight();
		if( w <= h ){
			GLES20.glViewport(0 ,(h-w)/2 ,w ,w);
		}else{
			GLES20.glViewport((w-h)/2 ,0 ,h ,h);
		}

		// 描画情報が登録されていれば描画処理
		if( mRenderingDrawParams != null ){
			int cnt = mRenderingDrawParams.length;
			for(int i=0;i<cnt;i++){
				// 描画情報の最後まできたら終了
				if( mRenderingDrawParams[i].getType() == DrawParams.TYPE_ENDPOINT )break;

				// 描画処理実行
				mRenderingDrawParams[i].run(this);
			}
		}
/*
		// コマ落ちテスト
		try {
			Thread.sleep(1000 / 30);
		}catch (Exception e){

		}
*/
		// 描画が終わったのでリクエストフラグをおろす
		setRenderingRequest(false);
	}

	// 描画情報をセット
	public synchronized void setDrawParams(DrawParams[] _next)
	{
		mRenderingDrawParams = _next;
	}

	// レンダリングリクエストをセット
	public synchronized void setRenderingRequest()
	{
		setRenderingRequest(true);
	}
	public synchronized void setRenderingRequest(boolean _flag)
	{
		mRenderingStatus = _flag;
	}

	// レンダリングリクエストをセット
	public synchronized boolean checkRenderingRequest()
	{
		return(mRenderingStatus);
	}

	public DrawParams[] getTargetDrawParams(){return(mDrawParams[mTargetDrawParamsNo]);}
	public DrawParams allocDrawParams(){return(mDrawParams[mTargetDrawParamsNo][mCurDrawParamNo++]);}
	public Texture getTexture(int _no){return(mTextures[_no]);}

	// 描画リクエスト＋ダブルバッファフリップ
	public void drawRequestAndFlip()
	{
		// 描画終端子
		allocDrawParams().setEndPoint();

		if( checkRenderingRequest() == false) {
			// 描画中ではない

			// 描画情報をRendererに渡す
			setDrawParams(mDrawParams[mTargetDrawParamsNo]);

			// 次に使う描画情報バッファを切り替える(FLIP)
			mTargetDrawParamsNo++;
			mTargetDrawParamsNo %= 2;

			// GLSurfaceViewに描画をリクエスト
			setRenderingRequest();
			mMainView.requestRender();		// OpenGLに描画させる(RENDERMODE_WHEN_DIRTY時)
		}

		mCurDrawParamNo = 0;
	}

	// OpenGLのバッファリセット
	public void reset()
	{
		// テクスチャクラス復帰
		for(int i=0;i<MAX_TEXTURE;i++){
			mTextures[i].reset(mMainView.mMainActivity);
		}

		// シェーダ復帰
		for(int i=0;i<MAX_SHADER;i++){
			mShaders[i].reset();
		}
	}
}
