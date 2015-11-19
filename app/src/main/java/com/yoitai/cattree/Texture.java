package com.yoitai.cattree;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.yoitai.glib.Vector2;

public class Texture {
	// メンバ変数
	int mTexID;			// テクスチャID
	Vector2 mSize;			// テクスチャ内のデータサイズ
	Vector2 mTexSize;		// テクスチャサイズ
	Vector2 mCenterPos;	// アフィン変換の中心座標
	String mFilename;		// ファイル名
	Vector2 mOffsetPos;	// 表示オフセット

	// コンストラクタ
	public Texture()
	{
		mSize = new Vector2();
		mTexSize = new Vector2();
		mCenterPos = new Vector2();
		mOffsetPos = new Vector2();
	}

	// 画像ファイルをassetから読み込んでテクスチャとして登録
	public boolean readTexture(Activity _act, String _filename, int _w, int _h)
	{
		return(readTexture(_act, _filename, _w, _h, 0.0f, 0.0f, 0.0f, 0.0f));
	}
	public boolean readTexture(Activity _act, String _filename, int _w, int _h, float _cx, float _cy, float _ox, float _oy)
	{
		// テクスチャ画像の読み込み
		AssetManager as = _act.getAssets();
		InputStream is = null;
		try {
			is = as.open(_filename);
		} catch (IOException e) {
			return(false);
		}
		Bitmap bmp = BitmapFactory.decodeStream(is);
		try {
			is.close();
		} catch (IOException e) {
			return(false);
		}

		// テクスチャIDの確保
		int texid[] = new int[1];
		GLES20.glGenTextures(1, texid, 0);
		mTexID = texid[0];

		// 対象テクスチャ設定
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D , mTexID);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D ,0 ,bmp ,0);

		// テクスチャパラメータの保存
		mTexSize.X = bmp.getWidth();
		mTexSize.Y = bmp.getHeight();
		mSize.X = _w;
		mSize.Y = _h;
		mCenterPos.X = _cx;
		mCenterPos.Y = _cy;
		mOffsetPos.X = _ox;
		mOffsetPos.Y = _oy;

		// bitmap解放
		bmp.recycle();

		// テクスチャのバインドをとく
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

		// ファイル名を保存
		mFilename = ""+_filename;

		return(true);
	}

	// リセット
	public void reset(Activity _act)
	{
		if( mTexID == 0 )return;

		// テクスチャの再読み込み
		readTexture(_act, mFilename, (int) mSize.X, (int) mSize.Y, mCenterPos.X, mCenterPos.Y, mOffsetPos.X, mOffsetPos.Y);
	}

	// getter
	int getTexID(){return(mTexID);}			// テクスチャID
	Vector2 getSize(){return(mSize);}
	Vector2 getTexSize(){return(mTexSize);}
	Vector2 getCenterPos(){return(mCenterPos);}
	String getFilename(){return(mFilename);}
	Vector2 getOffsetPos(){return(mOffsetPos);}
}
