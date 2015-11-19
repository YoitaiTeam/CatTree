package com.yoitai.glib;


//============================================================================
//                         3x3行列クラス
//============================================================================
public class Matrix33 {
	// ===================================================
	//                      置換文字
	// ===================================================

	// ===================================================
	//                    メンバー定義
	// ===================================================
	public float Matrix[][];									// 行列
	public float MatrixWork[][];								// GC対策行列計算用
	Matrix33 TemporaryMat;										// テンポラリ行列

	// ===================================================
	//                     メソッド
	// ===================================================
	// ---------------------------------------------------
	//                   コンストラクタ
	// ---------------------------------------------------
	public Matrix33()
	{
		// 3x3行列配列作成
		Matrix = new float [3][3];
		MatrixWork = null;
		TemporaryMat = null;
	}

	// ---------------------------------------------------
	//                  単位行列をセット
	// ---------------------------------------------------
	public void SetToFloatList(float [] _list)
	{
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				_list[i*3+j] = Matrix[i][j];
			}
		}
	}

	// ---------------------------------------------------
	//                  単位行列をセット
	// ---------------------------------------------------
	public void Identity()
	{
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				Matrix[i][j] = 0.0f;
			}
		}

		// 単位行列
		Matrix[0][0] = 1.0f;
		Matrix[1][1] = 1.0f;
		Matrix[2][2] = 1.0f;
	}

	// ---------------------------------------------------
	//       現在の行列から回転(反時計ラジアン)
	// ---------------------------------------------------
	public void Rotation(float _rot)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix33();
		}
		TemporaryMat.SetRotation(_rot);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	//            現在の行列から拡大/縮小
	// ---------------------------------------------------
	public void Scaling(float _x ,float _y)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix33();
		}
		TemporaryMat.SetScaling(_x ,_y);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	//               現在の行列から移動
	// ---------------------------------------------------
	public void Translate(float _x ,float _y)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix33();
		}
		TemporaryMat.SetTranslate(_x ,_y);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	//         回転行列セット(反時計ラジアン)
	// ---------------------------------------------------
	public void SetRotation(float _rot)
	{
		Identity();

		Matrix[0][0] = (float)Math.cos(_rot);
		Matrix[0][1] = (float)Math.sin(_rot);
		Matrix[1][0] = (float)-Math.sin(_rot);
		Matrix[1][1] = (float)Math.cos(_rot);
	}

	// ---------------------------------------------------
	//              拡大/縮小行列セット
	// ---------------------------------------------------
	public void SetScaling(float _x ,float _y)
	{
		Identity();

		Matrix[0][0] = _x;
		Matrix[1][1] = _y;
	}

	// ---------------------------------------------------
	//               移動行列セット
	// ---------------------------------------------------
	public void SetTranslate(float _x ,float _y)
	{
		Identity();

		Matrix[2][0] = _x;
		Matrix[2][1] = _y;
	}

	// ---------------------------------------------------
	//                 行列のかけ算
	// ---------------------------------------------------
	public Matrix33 Multiply(Matrix33 _src)
	{
		int i,j;

		if( MatrixWork == null )MatrixWork = new float [3][3];

		for(i=0;i<3;i++){
			for(j=0;j<3;j++){
				MatrixWork[i][j] = Matrix[i][0]*_src.Matrix[0][j] + Matrix[i][1]*_src.Matrix[1][j] + Matrix[i][2]*_src.Matrix[2][j];
			}
		}

		for(i=0;i<3;i++){
			for(j=0;j<3;j++){
				Matrix[i][j] = MatrixWork[i][j];
			}
		}

		return(this);
	}


	public Matrix33 Multiply(Matrix33 _src ,Matrix33 _dst)
	{
		int i,j;

		for(i=0;i<3;i++){
			for(j=0;j<3;j++){
				_dst.Matrix[i][j] = Matrix[i][0]*_src.Matrix[0][j] + Matrix[i][1]*_src.Matrix[1][j] + Matrix[i][2]*_src.Matrix[2][j];
			}
		}

		return(_dst);
	}

	// ---------------------------------------------------
	//                 アフィン変換
	// ---------------------------------------------------
	public void SetAffin(Vector2 _cpos ,float _rot ,Vector2 _scl ,Vector2 _trans)
	{
		if( _cpos != null ){
			// 中心まで移動
			SetTranslate(-_cpos.X ,-_cpos.Y);
		}else{
			Identity();
		}

		// 拡大
		Scaling(_scl.X ,_scl.Y);

		// 回転
		Rotation(_rot);

		if( _cpos != null ){
			// 中心を戻し、移動
			Translate(_cpos.X+_trans.X ,_cpos.Y+_trans.Y);
		}else{
			Translate(_trans.X ,_trans.Y);
		}
	}

	// ---------------------------------------------------
	// 座標をこの行列で変換(w=1)
	// ---------------------------------------------------
	public Vector2 TransformCoord(Vector2 _src)
	{
		return(TransformCoord(_src ,null));
	}
	public Vector2 TransformCoord(Vector2 _src ,Vector2 _dst)
	{
		float x,y;

		x = Matrix[0][0]*_src.X + Matrix[1][0]*_src.Y + Matrix[2][0];
		y = Matrix[0][1]*_src.X + Matrix[1][1]*_src.Y + Matrix[2][1];

		if( _dst == null ){
			_src.X = x;
			_src.Y = y;
			return(_src);
		}

		_dst.X = x;
		_dst.Y = y;

		return(_dst);
	}

	// ---------------------------------------------------
	// 座標をこの行列で変換(w=0)
	// ---------------------------------------------------
	public Vector2 Transform(Vector2 _src)
	{
		return(Transform(_src ,null));
	}
	public Vector2 Transform(Vector2 _src ,Vector2 _dst)
	{
		float x,y;

		x = Matrix[0][0]*_src.X + Matrix[1][0]*_src.Y;
		y = Matrix[0][1]*_src.X + Matrix[1][1]*_src.Y;

		if( _dst == null ){
			_src.X = x;
			_src.Y = y;
			return(_src);
		}

		_dst.X = x;
		_dst.Y = y;

		return(_dst);
	}


	// ---------------------------------------------------
	//                    逆行列を求める
	// ---------------------------------------------------
	public boolean Reverse()
	{
		return(Reverse(null));
	}
	public boolean Reverse(Matrix33 _dst)
	{
		float detA;

		if( MatrixWork == null )MatrixWork = new float [3][3];

		// 逆行列の存在チェック
		detA =	Matrix[0][0]*Matrix[1][1]*Matrix[2][2] +
				Matrix[1][0]*Matrix[2][1]*Matrix[0][2] +
				Matrix[2][0]*Matrix[0][1]*Matrix[1][2] -
				Matrix[0][0]*Matrix[2][1]*Matrix[1][2] -
				Matrix[2][0]*Matrix[1][1]*Matrix[0][2] -
				Matrix[1][0]*Matrix[0][1]*Matrix[2][2];

		if( detA == 0.0f )return(false);

		MatrixWork[0][0] = (Matrix[1][1]*Matrix[2][2] - Matrix[1][2]*Matrix[2][1]) / detA;
		MatrixWork[0][1] = (Matrix[0][2]*Matrix[2][1] - Matrix[0][1]*Matrix[2][2]) / detA;
		MatrixWork[0][2] = (Matrix[0][1]*Matrix[1][2] - Matrix[0][2]*Matrix[1][1]) / detA;

		MatrixWork[1][0] = (Matrix[1][2]*Matrix[2][0] - Matrix[1][0]*Matrix[2][2]) / detA;
		MatrixWork[1][1] = (Matrix[0][0]*Matrix[2][2] - Matrix[0][2]*Matrix[2][0]) / detA;
		MatrixWork[1][2] = (Matrix[0][2]*Matrix[1][0] - Matrix[0][0]*Matrix[1][2]) / detA;

		MatrixWork[2][0] = (Matrix[1][0]*Matrix[2][1] - Matrix[1][1]*Matrix[2][0]) / detA;
		MatrixWork[2][1] = (Matrix[0][1]*Matrix[2][0] - Matrix[0][0]*Matrix[2][1]) / detA;
		MatrixWork[2][2] = (Matrix[0][0]*Matrix[1][1] - Matrix[0][1]*Matrix[1][0]) / detA;

		if( _dst != null ){
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					_dst.Matrix[i][j] = MatrixWork[i][j];
				}
			}
		}else{
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					Matrix[i][j] = MatrixWork[i][j];
				}
			}
		}

		return(true);
	}

	// ---------------------------------------------------
	//                 行列のかけ算
	// ---------------------------------------------------
	public void MultiplyFloat(float _scl)
	{
		int i,j;

		for(i=0;i<3;i++){
			for(j=0;j<3;j++){
				Matrix[i][j] *= _scl;
			}
		}
	}

	// ---------------------------------------------------
	//                      コピー
	// ---------------------------------------------------
	public void Copy(Matrix33 _src)
	{
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				Matrix[i][j] = _src.Matrix[i][j];
			}
		}
	}
}
