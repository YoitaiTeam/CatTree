package com.yoitai.cattree;

import android.opengl.GLES20;

//============================================================================
//                     シェーダーのコード
//============================================================================
public class Shader {
	//----------------------------------------
	//            頂点シェーダ
	//----------------------------------------
	public static final String VSHADER_2D =
	"	attribute lowp vec4 VPos;\n"+
	"	attribute lowp vec4 VCol;\n"+
	"	attribute lowp vec2 VTex;\n"+
	"	uniform lowp mat4 ScrMat;\n"+
	"	varying vec4 OCol;\n"+
	"	varying vec2 OTex;\n"+
	"	void main(void)\n"+
	"	{\n"+
	"		gl_Position   = ScrMat  * VPos;\n"+
	"		OTex = VTex;\n"+
	"		OCol = VCol;\n"+
	"	}\n"
	;

	//----------------------------------------
	//    フラグメント(ピクセル)シェーダ
	//----------------------------------------
	public static final String PSHADER_TEXTURE2D =
	"	precision lowp float;\n"+
	"	varying vec4 OCol;\n"+
	"	varying vec2 OTex;\n"+
	"	uniform sampler2D Tex[4];\n"+
	"	void main(void)\n"+
	"	{\n"+
	"			gl_FragColor = texture2D( Tex[0], OTex ) * OCol;\n"+
	"	}\n"
	;

	//----------------------------------------
	//               メンバー
	//----------------------------------------
	int mHandle;					// コンパイル、リンク済シェーダハンドル
	int mScreenMatID;				// 変換行列属性ロケーションID
	int mVertexPosID;				// 頂点内座標属性ロケーションID
	int mVertexColorID;			// 頂点内カラー属性ロケーションID
	int mVertexTexID;				// 頂点内テクスチャ座標ロケーションID
	int mPixelTexID;				// ピクセルシェーダ内テクスチャID

	String mVertexCode;			// 頂点シェーダ
	String mFragmentCode;			// フラグメント(ピクセル)シェーダ

	//----------------------------------------
	//           コンパイル・リンク
	//----------------------------------------
	public boolean compileShader(String _vcode, String _pcode)
	{
		int[] compiled = new int[1];
		int vhandle;
		int phandle;

		// 頂点シェーダ
		mVertexCode = _vcode;
		vhandle = GLES20.glCreateShader( GLES20.GL_VERTEX_SHADER );
		GLES20.glShaderSource( vhandle ,_vcode );
		GLES20.glCompileShader( vhandle );
		GLES20.glGetShaderiv( vhandle, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if( compiled[0] == 0 )return(false);

		// フラグメント(ピクセル)シェーダ
		mFragmentCode = _pcode;
		phandle = GLES20.glCreateShader( GLES20.GL_FRAGMENT_SHADER );
		GLES20.glShaderSource( phandle ,_pcode );
		GLES20.glCompileShader( phandle );
		GLES20.glGetShaderiv( phandle, GLES20.GL_COMPILE_STATUS, compiled ,0 );
		if( compiled[0] == 0 )return(false);

		// 処理プログラム作成
		mHandle = GLES20.glCreateProgram();

		// ピクセル/頂点シェーダのアタッチ
		GLES20.glAttachShader(mHandle, vhandle );
		GLES20.glAttachShader(mHandle, phandle );

		// プログラムをリンクする
		GLES20.glLinkProgram(mHandle);

		// リンクに成功したかどうか調べる
		GLES20.glGetProgramiv(mHandle, GLES20.GL_LINK_STATUS, compiled ,0 );
		if( compiled[0] == 0 )return(false);

		// 共通データのロケーションID取得
		mScreenMatID = GLES20.glGetUniformLocation(mHandle,"ScrMat" );

		// 頂点シェーダのロケーションID取得
		mVertexPosID = GLES20.glGetAttribLocation(mHandle,"VPos" );
		mVertexColorID = GLES20.glGetAttribLocation(mHandle,"VCol" );
		mVertexTexID = GLES20.glGetAttribLocation(mHandle,"VTex" );

		// フラグメントシェーダのロケーションID取得
		mPixelTexID = GLES20.glGetUniformLocation(mHandle,"Tex[0]" );

		return(true);
	}

	//----------------------------------------
	//               リセット
	//----------------------------------------
	public boolean reset()
	{
		if( mHandle == 0 )return(true);

		return(compileShader(mVertexCode, mFragmentCode));
	}
}
