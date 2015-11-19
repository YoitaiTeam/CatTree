package com.yoitai.cattree;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.opengl.GLES20;

import com.yoitai.glib.Color;
import com.yoitai.glib.Matrix33;
import com.yoitai.glib.Matrix44;
import com.yoitai.glib.Vector2;

public class DrawParams {
	// タイプ定義
	public static final int TYPE_ENDPOINT    = -1;
	public static final int TYPE_SCREENCLEAR = 0;
	public static final int TYPE_SPRITE      = 1;

	public static final int TYPE_LINEAR      = 0x00000001;

	// staticメンバー(このクラスで描画時のみ共通で使用)
	static Vector2 [] sPosWork;
	static Color [] sColWork;
	static Matrix33 sMat33Work;
	static Matrix44 sMat44Work;
	static Vector2 [] sTexelWork;
	static float [] sFloatWork;
	static IntBuffer sTransferPos;
	static IntBuffer sTransferCol;
	static IntBuffer sTransferTex;

	// メン数
	int mType;				// 表示情報タイプ
	int mStatus;			// 描画ステータス
	int mNumber;			// 表示対象ＩＤ
	Vector2 mPos;			// 位置
	Vector2 mScl;			// スケーリング
	float mRot;			// 回転
	Color mColor;			// カラー

	// 静的変数初期化
	static {
		// 位置ワーク
		sPosWork = new Vector2 [4];
		for(int i=0;i<4;i++) sPosWork[i] = new Vector2();

		// テクスチャ座標ワーク
		sTexelWork = new Vector2 [4];
		for(int i=0;i<4;i++) sTexelWork[i] = new Vector2();

		// 頂点カラーワーク
		sColWork = new Color [4];
		for(int i=0;i<4;i++) sColWork[i] = new Color();

		// 3x3行列ワーク
		sMat33Work = new Matrix33();

		// 4x4行列ワーク
		sMat44Work = new Matrix44();

		// float変換ワーク
		sFloatWork = new float [1024];

		// 転送用ワーク
		ByteBuffer bb = ByteBuffer.allocateDirect(4*4*3);
		bb.order(ByteOrder.nativeOrder());
		sTransferPos = bb.asIntBuffer();

		bb = ByteBuffer.allocateDirect(4*4*4);
		bb.order(ByteOrder.nativeOrder());
		sTransferCol = bb.asIntBuffer();

		bb = ByteBuffer.allocateDirect(4*4*2);
		bb.order(ByteOrder.nativeOrder());
		sTransferTex = bb.asIntBuffer();
	}

	// float配列からバッファに登録
	public void setFloatBuffer(IntBuffer _tag, float[] arr, int _count)
	{
		_tag.position(0);
		for(int i = 0; i < _count;++i){
			_tag.put(Float.floatToIntBits(arr[i]));
		}
		_tag.position(0);
    }

	// コンストラクタ
	public DrawParams()
	{
		mPos = new Vector2();
		mScl = new Vector2();
		mColor = new Color();
	}

	// 描画情報設定：スクリーンクリア
	public void setScreenClear(float _r, float _g, float _b)
	{
		mType = TYPE_SCREENCLEAR;
		mColor.Set(_r, _g, _b ,1.0f);
	}

	// 描画情報設定：終端子
	public void setEndPoint()
	{
		mType = TYPE_ENDPOINT;
	}

	// 描画処理：スクリーンクリア
	public void runScreenClear(MainRenderer _renderer)
	{
		GLES20.glClearColor(mColor.R ,mColor.G ,mColor.B ,mColor.A);	// 背景色をセット
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);					// カラーバッファのみクリア
	}

	// 描画情報設定：スプライト描画
	public void setSprite(int _spno)
	{
		mType = TYPE_SPRITE;
		mNumber = _spno;
		mPos.Set(0.0f,0.0f);
		mScl.Set(1.0f,1.0f);
		mRot = 0.0f;
		mColor.Set(1.0f, 1.0f, 1.0f, 1.0f);
	}

	// 描画処理：スプライト描画
	public void runSprite(MainRenderer _renderer)
	{
		Texture tex;

		tex = _renderer.mTextures[mNumber];

		// 各座標セット
		sPosWork[0].Set(0.0f       ,0.0f);
		sPosWork[1].Set(tex.getSize().X, 0.0f);
		sPosWork[2].Set(0.0f       ,tex.getSize().Y);
		sPosWork[3].Set(tex.getSize().X, tex.getSize().Y);

		// 各テクスチャ座標セット
		sTexelWork[0].Set(0.0f                       ,0.0f                      );
		sTexelWork[1].Set(tex.getSize().X / tex.mTexSize.X, 0.0f);
		sTexelWork[2].Set(0.0f                       ,tex.getSize().Y / tex.mTexSize.Y);
		sTexelWork[3].Set(tex.getSize().X / tex.mTexSize.X, tex.getSize().Y / tex.mTexSize.Y);

		// 各頂点のカラーセット
		for(int i=0;i<4;i++){
			sColWork[i].Copy(mColor);
		}

		// 頂点のアフィン変換を行う
		sMat33Work.SetAffin(tex.mCenterPos, mRot, mScl, tex.mOffsetPos);
		sMat33Work.Translate(mPos.X, mPos.Y);
		for(int i=0;i<4;i++){
			sMat33Work.TransformCoord(sPosWork[i]);
		}

		// 使用するシェーダを登録
		Shader prog = _renderer.mShaders[MainRenderer.SHADER_2D];
		GLES20.glUseProgram( prog.mHandle);

		// 頂点座標の登録(まだ未転送)
		for(int i=0;i<4;i++){
			sFloatWork[i*3+0] = sPosWork[i].X;
			sFloatWork[i*3+1] = sPosWork[i].Y;
			sFloatWork[i*3+2] = 0.0f;
		}
		setFloatBuffer(sTransferPos, sFloatWork, 4 * 3);
		GLES20.glEnableVertexAttribArray( prog.mVertexPosID);
		GLES20.glVertexAttribPointer( prog.mVertexPosID,3 ,GLES20.GL_FLOAT ,false ,0 , sTransferPos);

		// 頂点カラーの登録(まだ未転送)
		for(int i=0;i<4;i++){
			sFloatWork[i*4+0] = sColWork[i].R;
			sFloatWork[i*4+1] = sColWork[i].G;
			sFloatWork[i*4+2] = sColWork[i].B;
			sFloatWork[i*4+3] = sColWork[i].A;
		}
		setFloatBuffer(sTransferCol, sFloatWork, 4 * 4);
		GLES20.glEnableVertexAttribArray( prog.mVertexColorID);
		GLES20.glVertexAttribPointer( prog.mVertexColorID, 4, GLES20.GL_FLOAT ,false ,0 , sTransferCol);

		// テクセル座標の登録(まだ未転送)
		for(int i=0;i<4;i++){
			sFloatWork[i*2+0] = sTexelWork[i].X;
			sFloatWork[i*2+1] = sTexelWork[i].Y;
		}
		setFloatBuffer(sTransferTex, sFloatWork, 4 * 2);
		GLES20.glEnableVertexAttribArray( prog.mVertexTexID);
		GLES20.glVertexAttribPointer( prog.mVertexTexID, 2, GLES20.GL_FLOAT ,false ,0 , sTransferTex);

		// スクリーン変換行列セット
		sMat44Work.Identity();
		sMat44Work.Translate(-MainRenderer.CONTENTS_W / 2.0f, -MainRenderer.CONTENTS_H / 2.0f, 0.0f);
		sMat44Work.Scaling(2.0f / MainRenderer.CONTENTS_W, -2.0f / MainRenderer.CONTENTS_H, 0.0f);
		sMat44Work.SetToFloatList(sFloatWork);
		GLES20.glUniformMatrix4fv(prog.mScreenMatID, 1, false, sFloatWork, 0);

		// アルファブレンドON
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA ,GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// 使用するテクスチャ設定
//		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex.mTexID);
		if( (mStatus & TYPE_LINEAR) != 0 ){
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER ,GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER ,GLES20.GL_LINEAR);
		}else{
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER ,GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER ,GLES20.GL_NEAREST);
		}
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S     ,GLES20.GL_REPEAT);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T     ,GLES20.GL_REPEAT);
		GLES20.glUniform1i(prog.mPixelTexID,0);

		// カリングOFF
		GLES20.glDisable(GLES20.GL_CULL_FACE);

		// ZテストOFF
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);

		// ポリゴン描画
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		// 頂点属性削除
		GLES20.glDisableVertexAttribArray( prog.mVertexPosID);
		GLES20.glDisableVertexAttribArray( prog.mVertexColorID);
		GLES20.glDisableVertexAttribArray( prog.mVertexTexID);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	// 描画実行
	public void run(MainRenderer _renderer)
	{
		switch(mType)
		{
			case TYPE_SCREENCLEAR:		runScreenClear(_renderer);	break;		// スクリーンクリア
			case TYPE_SPRITE:				runSprite(_renderer);		break;		// スプライト描画
		}
	}

	// getter / setter
	public int getType(){return(mType);}
	public Vector2 getPos(){return(mPos);}
	public Vector2 getScl(){return(mScl);}
	public float getRot(){return(mRot);}
	public void setRot(float _rot){mRot = _rot;}
	public Color getColor(){return(mColor);}
}
