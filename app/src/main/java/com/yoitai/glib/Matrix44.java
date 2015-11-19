package com.yoitai.glib;


//============================================================================
//                           4x4行列クラス
//============================================================================
public class Matrix44 {
	// ===================================================
	//                      置換文字
	// ===================================================

	// ===================================================
	//                    メンバー定義
	// ===================================================
	public float Matrix[][];									// 行列
	private float MatrixWork[][];								// GC対策行列計算用
	private Matrix44 TemporaryMat;								// テンポラリ行列
	private Vector3 XaxisWork;
	private Vector3 YaxisWork;
	private Vector3 ZaxisWork;

	// ===================================================
	//                     メソッド
	// ===================================================
	// ---------------------------------------------------
	//                   コンストラクタ
	// ---------------------------------------------------
	public Matrix44()
	{
		// 4x4行列配列作成
		Matrix = new float [4][4];
		MatrixWork = null;
		TemporaryMat = null;
		XaxisWork = null;
		YaxisWork = null;
		ZaxisWork = null;
	}

	// ---------------------------------------------------
	//            Floatリストからセット
	// ---------------------------------------------------
	public void SetToFloatList(float [] _list)
	{
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				_list[i*4+j] = Matrix[i][j];
			}
		}
	}

	// ---------------------------------------------------
	//                  単位行列をセット
	// ---------------------------------------------------
	public void Identity()
	{
		// 単位行列
		Matrix[0][0] = 1.0f;
		Matrix[0][1] = 0.0f;
		Matrix[0][2] = 0.0f;
		Matrix[0][3] = 0.0f;

		Matrix[1][0] = 0.0f;
		Matrix[1][1] = 1.0f;
		Matrix[1][2] = 0.0f;
		Matrix[1][3] = 0.0f;

		Matrix[2][0] = 0.0f;
		Matrix[2][1] = 0.0f;
		Matrix[2][2] = 1.0f;
		Matrix[2][3] = 0.0f;


		Matrix[3][0] = 0.0f;
		Matrix[3][1] = 0.0f;
		Matrix[3][2] = 0.0f;
		Matrix[3][3] = 1.0f;
	}

	// ---------------------------------------------------
	// クォータニオンで回転
	// ---------------------------------------------------
	public void Rotation(Quaternion _qtan)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix44 ();
		}
		TemporaryMat.SetRotation(_qtan);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	// Ｘ軸回転(ラジアン)
	// ---------------------------------------------------
	public void RotationX(float _rot)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix44 ();
		}
		TemporaryMat.SetRotationX(_rot);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	// Ｙ軸回転(ラジアン)
	// ---------------------------------------------------
	public void RotationY(float _rot)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix44 ();
		}
		TemporaryMat.RotationY(_rot);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	// Ｚ軸回転(ラジアン)
	// ---------------------------------------------------
	public void RotationZ(float _rot)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix44 ();
		}
		TemporaryMat.RotationZ(_rot);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	// 拡大/縮小
	// ---------------------------------------------------
	public void Scaling(float _x ,float _y ,float _z)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix44 ();
		}
		TemporaryMat.SetScaling(_x, _y, _z);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	// 移動
	// ---------------------------------------------------
	public void Translate(float _x ,float _y ,float _z)
	{
		if( TemporaryMat == null ){
			TemporaryMat = new Matrix44 ();
		}
		TemporaryMat.SetTranslate(_x, _y, _z);
		Multiply(TemporaryMat);
	}

	// ---------------------------------------------------
	// 拡大/縮小
	// ---------------------------------------------------
	public void Scaling(Vector3 _scl)
	{
		Scaling(_scl.X ,_scl.Y ,_scl.Z);
	}

	// ---------------------------------------------------
	// 移動
	// ---------------------------------------------------
	public void Translate(Vector3 _pos)
	{
		Translate(_pos.X ,_pos.Y ,_pos.Z);
	}

	// ---------------------------------------------------
	// 拡大/縮小行列セット
	// ---------------------------------------------------
	public void SetScaling(Vector3 _scl)
	{
		SetScaling(_scl.X ,_scl.Y ,_scl.Z);
	}

	// ---------------------------------------------------
	// 移動行列セット
	// ---------------------------------------------------
	public void SetTranslate(Vector3 _pos)
	{
		SetTranslate(_pos.X ,_pos.Y ,_pos.Z);
	}

	// ---------------------------------------------------
	// Ｘ軸回転行列セット(ラジアン)
	// ---------------------------------------------------
	public void SetRotationX(float _rot)
	{
		Matrix[0][0] = 1.0f;
		Matrix[1][0] = 0.0f;
		Matrix[2][0] = 0.0f;
		Matrix[3][0] = 0.0f;

		Matrix[0][1] = 0.0f;
		Matrix[1][1] = (float)Math.cos(_rot);
		Matrix[2][1] = (float)-Math.sin(_rot);
		Matrix[3][1] = 0.0f;

		Matrix[0][2] = 0.0f;
		Matrix[1][2] = (float)Math.sin(_rot);
		Matrix[2][2] = (float)Math.cos(_rot);
		Matrix[3][2] = 0.0f;

		Matrix[0][3] = 0.0f;
		Matrix[1][3] = 0.0f;
		Matrix[2][3] = 0.0f;
		Matrix[3][3] = 1.0f;
	}

	// ---------------------------------------------------
	// Ｙ軸回転行列セット(ラジアン)
	// ---------------------------------------------------
	public void SetRotationY(float _rot)
	{
		Matrix[0][0] = (float)Math.cos(_rot);
		Matrix[1][0] = 0.0f;
		Matrix[2][0] = (float)Math.sin(_rot);
		Matrix[3][0] = 0.0f;

		Matrix[0][1] = 0.0f;
		Matrix[1][1] = 1.0f;
		Matrix[2][1] = 0.0f;
		Matrix[3][1] = 0.0f;

		Matrix[0][2] = (float)-Math.sin(_rot);
		Matrix[1][2] = 0.0f;
		Matrix[2][2] = (float)Math.cos(_rot);
		Matrix[3][2] = 0.0f;

		Matrix[0][3] = 0.0f;
		Matrix[1][3] = 0.0f;
		Matrix[2][3] = 0.0f;
		Matrix[3][3] = 1.0f;
	}

	// ---------------------------------------------------
	// Ｚ軸回転行列セット(ラジアン)
	// ---------------------------------------------------
	public void SetRotationZ(float _rot)
	{
		Matrix[0][0] = (float)Math.cos(_rot);
		Matrix[1][0] = (float)-Math.sin(_rot);
		Matrix[2][0] = 0.0f;
		Matrix[3][0] = 0.0f;

		Matrix[0][1] = (float)Math.sin(_rot);
		Matrix[1][1] = (float)Math.cos(_rot);
		Matrix[2][1] = 0.0f;
		Matrix[3][1] = 0.0f;

		Matrix[0][2] = 0.0f;
		Matrix[1][2] = 0.0f;
		Matrix[2][2] = 1.0f;
		Matrix[3][2] = 0.0f;

		Matrix[0][3] = 0.0f;
		Matrix[1][3] = 0.0f;
		Matrix[2][3] = 0.0f;
		Matrix[3][3] = 1.0f;
	}

	// ---------------------------------------------------
	// 拡大/縮小行列セット
	// ---------------------------------------------------
	public void SetScaling(float _x ,float _y ,float _z)
	{
		Identity();

		Matrix[0][0] = _x;
		Matrix[1][1] = _y;
		Matrix[2][2] = _z;
	}


	// ---------------------------------------------------
	// 移動行列セット
	// ---------------------------------------------------
	public void SetTranslate(float _x ,float _y ,float _z)
	{
		Identity();

		Matrix[3][0] = _x;
		Matrix[3][1] = _y;
		Matrix[3][2] = _z;
	}

	// ---------------------------------------------------
	// クォータニオン行列セット
	// ---------------------------------------------------
	public void SetRotation(Quaternion _qtan)
	{
		// クォータニオンから４ｘ４回転行列へ変換
		_qtan.ConvToMatrix(this);
	}

	// ---------------------------------------------------
	// アフィン変換
	// ---------------------------------------------------
	public void SetAffin(Vector3 _cpos ,Quaternion _rot ,Vector3 _scl ,Vector3 _trans)
	{
		// 中心まで移動
		if( _cpos != null ){
			SetTranslate(-_cpos.X ,-_cpos.Y ,-_cpos.Z);
		}else{
			Identity();
		}

		// 回転
		Rotation(_rot);

		// 拡大
		Scaling(_scl.X ,_scl.Y ,_scl.Z);

		// 中心を戻し、移動
		if( _cpos != null ){
			Translate(_cpos.X+_trans.X ,_cpos.Y+_trans.Y ,_cpos.Z+_trans.Z);
		}else{
			Translate(_trans.X ,_trans.Y ,_trans.Z);
		}
	}

	// ---------------------------------------------------
	// 座標をこの行列で変換(w=1)
	// ---------------------------------------------------
	public Vector3 TransformCoord(Vector3 _src)
	{
		return(TransformCoord(_src ,null));
	}
	public Vector3 TransformCoord(Vector3 _src ,Vector3 _dst)
	{
		float x,y,z;

		x = Matrix[0][0]*_src.X + Matrix[1][0]*_src.Y + Matrix[2][0]*_src.Z + Matrix[3][0];
		y = Matrix[0][1]*_src.X + Matrix[1][1]*_src.Y + Matrix[2][1]*_src.Z + Matrix[3][1];
		z = Matrix[0][2]*_src.X + Matrix[1][2]*_src.Y + Matrix[2][2]*_src.Z + Matrix[3][2];

		if( _dst == null ){
			_src.X = x;
			_src.Y = y;
			_src.Z = z;
			return(_src);
		}

		_dst.X = x;
		_dst.Y = y;
		_dst.Z = z;

		return(_dst);
	}

	// ---------------------------------------------------
	// 座標をこの行列で変換(w=0)
	// ---------------------------------------------------
	public Vector3 Transform(Vector3 _src)
	{
		return(Transform(_src ,null));
	}
	public Vector3 Transform(Vector3 _src ,Vector3 _dst)
	{
		float x,y,z;

		x = Matrix[0][0]*_src.X + Matrix[1][0]*_src.Y + Matrix[2][0]*_src.Z;
		y = Matrix[0][1]*_src.X + Matrix[1][1]*_src.Y + Matrix[2][1]*_src.Z;
		z = Matrix[0][2]*_src.X + Matrix[1][2]*_src.Y + Matrix[2][2]*_src.Z;

		if( _dst == null ){
			_src.X = x;
			_src.Y = y;
			_src.Z = z;
			return(_src);
		}

		_dst.X = x;
		_dst.Y = y;
		_dst.Z = z;

		return(_dst);
	}

	// ---------------------------------------------------
	// 座標をこの行列で変換
	// ---------------------------------------------------
	public Vector4 Transform(Vector4 _src)
	{
		return(Transform(_src ,null));
	}
	public Vector4 Transform(Vector4 _src ,Vector4 _dst)
	{
		float x,y,z,w;

		x = Matrix[0][0]*_src.X + Matrix[1][0]*_src.Y + Matrix[2][0]*_src.Z + Matrix[3][0]*_src.W;
		y = Matrix[0][1]*_src.X + Matrix[1][1]*_src.Y + Matrix[2][1]*_src.Z + Matrix[3][1]*_src.W;
		z = Matrix[0][2]*_src.X + Matrix[1][2]*_src.Y + Matrix[2][2]*_src.Z + Matrix[3][2]*_src.W;
		w = Matrix[0][3]*_src.X + Matrix[1][3]*_src.Y + Matrix[2][3]*_src.Z + Matrix[3][3]*_src.W;

		if( _dst == null ){
			_src.X = x;
			_src.Y = y;
			_src.Z = z;
			_src.W = w;
			return(_src);
		}

		_dst.X = x;
		_dst.Y = y;
		_dst.Z = z;
		_dst.W = w;
		return(_dst);
	}

	// ---------------------------------------------------
	// 乗算
	// ---------------------------------------------------
	public Matrix44 Multiply(Matrix44 _src)
	{
		return(Multiply(_src ,null));
	}
	public Matrix44 Multiply(Matrix44 _src ,Matrix44 _dst)
	{
		int i,j;
		if( _dst != null ){
			for(i=0;i<4;i++){
				for(j=0;j<4;j++){
					_dst.Matrix[i][j] = Matrix[i][0]*_src.Matrix[0][j] + Matrix[i][1]*_src.Matrix[1][j] + Matrix[i][2]*_src.Matrix[2][j] + Matrix[i][3]*_src.Matrix[3][j];
				}
			}

			return(_dst);
		}

		if( MatrixWork == null )MatrixWork = new float [4][4];

		for(i=0;i<4;i++){
			for(j=0;j<4;j++){
				MatrixWork[i][j] = Matrix[i][0]*_src.Matrix[0][j] + Matrix[i][1]*_src.Matrix[1][j] + Matrix[i][2]*_src.Matrix[2][j] + Matrix[i][3]*_src.Matrix[3][j];
			}
		}

		for(i=0;i<4;i++){
			for(j=0;j<4;j++){
				Matrix[i][j] = MatrixWork[i][j];
			}
		}

		return(this);
	}

	// ---------------------------------------------------
	// 逆行列を求める
	// ---------------------------------------------------
	public boolean Reverse()
	{
		return(Reverse(null));
	}

	public boolean Reverse(Matrix44 _dst)
	{
		float detA;

		detA =    Matrix[0][0]*Matrix[1][1]*Matrix[2][2]*Matrix[3][3] + Matrix[0][0]*Matrix[2][1]*Matrix[3][2]*Matrix[1][3] + Matrix[0][0]*Matrix[3][1]*Matrix[1][2]*Matrix[2][3]
				+ Matrix[1][0]*Matrix[0][1]*Matrix[3][2]*Matrix[2][3] + Matrix[1][0]*Matrix[2][1]*Matrix[0][2]*Matrix[3][3] + Matrix[1][0]*Matrix[3][1]*Matrix[2][2]*Matrix[0][3]
				+ Matrix[2][0]*Matrix[0][1]*Matrix[1][2]*Matrix[3][3] + Matrix[2][0]*Matrix[1][1]*Matrix[3][2]*Matrix[0][3] + Matrix[2][0]*Matrix[3][1]*Matrix[0][2]*Matrix[1][3]
				+ Matrix[3][0]*Matrix[0][1]*Matrix[2][2]*Matrix[1][3] + Matrix[3][0]*Matrix[1][1]*Matrix[0][2]*Matrix[2][3] + Matrix[3][0]*Matrix[2][1]*Matrix[1][2]*Matrix[0][3]
				- Matrix[0][0]*Matrix[1][1]*Matrix[3][2]*Matrix[2][3] - Matrix[0][0]*Matrix[2][1]*Matrix[1][2]*Matrix[3][3] - Matrix[0][0]*Matrix[3][1]*Matrix[2][2]*Matrix[1][3]
				- Matrix[1][0]*Matrix[0][1]*Matrix[2][2]*Matrix[3][3] - Matrix[1][0]*Matrix[2][1]*Matrix[3][2]*Matrix[0][3] - Matrix[1][0]*Matrix[3][1]*Matrix[0][2]*Matrix[2][3]
				- Matrix[2][0]*Matrix[0][1]*Matrix[3][2]*Matrix[1][3] - Matrix[2][0]*Matrix[1][1]*Matrix[0][2]*Matrix[3][3] - Matrix[2][0]*Matrix[3][1]*Matrix[1][2]*Matrix[0][3]
				- Matrix[3][0]*Matrix[0][1]*Matrix[1][2]*Matrix[2][3] - Matrix[3][0]*Matrix[1][1]*Matrix[2][2]*Matrix[0][3] - Matrix[3][0]*Matrix[2][1]*Matrix[0][2]*Matrix[1][3];

		if( detA == 0.0f )return(false);

		if( MatrixWork == null )MatrixWork = new float [4][4];

		MatrixWork[0][0] = (Matrix[1][1]*Matrix[2][2]*Matrix[3][3] + Matrix[2][1]*Matrix[3][2]*Matrix[1][3] + Matrix[3][1]*Matrix[1][2]*Matrix[2][3] - Matrix[1][1]*Matrix[3][2]*Matrix[2][3] - Matrix[2][1]*Matrix[1][2]*Matrix[3][3] - Matrix[3][1]*Matrix[2][2]*Matrix[1][3]) / detA;
		MatrixWork[1][0] = (Matrix[1][0]*Matrix[3][2]*Matrix[2][3] + Matrix[2][0]*Matrix[1][2]*Matrix[3][3] + Matrix[3][0]*Matrix[2][2]*Matrix[1][3] - Matrix[1][0]*Matrix[2][2]*Matrix[3][3] - Matrix[2][0]*Matrix[3][2]*Matrix[1][3] - Matrix[3][0]*Matrix[1][2]*Matrix[2][3]) / detA;
		MatrixWork[2][0] = (Matrix[1][0]*Matrix[2][1]*Matrix[3][3] + Matrix[2][0]*Matrix[3][1]*Matrix[1][3] + Matrix[3][0]*Matrix[1][1]*Matrix[2][3] - Matrix[1][0]*Matrix[3][1]*Matrix[2][3] - Matrix[2][0]*Matrix[1][1]*Matrix[3][3] - Matrix[3][0]*Matrix[2][1]*Matrix[1][3]) / detA;
		MatrixWork[3][0] = (Matrix[1][0]*Matrix[3][1]*Matrix[2][2] + Matrix[2][0]*Matrix[1][1]*Matrix[3][2] + Matrix[3][0]*Matrix[2][1]*Matrix[1][2] - Matrix[1][0]*Matrix[2][1]*Matrix[3][2] - Matrix[2][0]*Matrix[3][1]*Matrix[1][2] - Matrix[3][0]*Matrix[1][1]*Matrix[2][2]) / detA;

		MatrixWork[0][1] = (Matrix[0][1]*Matrix[3][2]*Matrix[2][3] + Matrix[2][1]*Matrix[0][2]*Matrix[3][3] + Matrix[3][1]*Matrix[2][2]*Matrix[0][3] - Matrix[0][1]*Matrix[2][2]*Matrix[3][3] - Matrix[2][1]*Matrix[3][2]*Matrix[0][3] - Matrix[3][1]*Matrix[0][2]*Matrix[2][3]) / detA;
		MatrixWork[1][1] = (Matrix[0][0]*Matrix[2][2]*Matrix[3][3] + Matrix[2][0]*Matrix[3][2]*Matrix[0][3] + Matrix[3][0]*Matrix[0][2]*Matrix[2][3] - Matrix[0][0]*Matrix[3][2]*Matrix[2][3] - Matrix[2][0]*Matrix[0][2]*Matrix[3][3] - Matrix[3][0]*Matrix[2][2]*Matrix[0][3]) / detA;
		MatrixWork[2][1] = (Matrix[0][0]*Matrix[3][1]*Matrix[2][3] + Matrix[2][0]*Matrix[0][1]*Matrix[3][3] + Matrix[3][0]*Matrix[2][1]*Matrix[0][3] - Matrix[0][0]*Matrix[2][1]*Matrix[3][3] - Matrix[2][0]*Matrix[3][1]*Matrix[0][3] - Matrix[3][0]*Matrix[0][1]*Matrix[2][3]) / detA;
		MatrixWork[3][1] = (Matrix[0][0]*Matrix[2][1]*Matrix[3][2] + Matrix[2][0]*Matrix[3][1]*Matrix[0][2] + Matrix[3][0]*Matrix[0][1]*Matrix[2][2] - Matrix[0][0]*Matrix[3][1]*Matrix[2][2] - Matrix[2][0]*Matrix[0][1]*Matrix[3][2] - Matrix[3][0]*Matrix[2][1]*Matrix[0][2]) / detA;

		MatrixWork[0][2] = (Matrix[0][1]*Matrix[1][2]*Matrix[3][3] + Matrix[1][1]*Matrix[3][2]*Matrix[0][3] + Matrix[3][1]*Matrix[0][2]*Matrix[1][3] - Matrix[0][1]*Matrix[3][2]*Matrix[1][3] - Matrix[1][1]*Matrix[0][2]*Matrix[3][3] - Matrix[3][1]*Matrix[1][2]*Matrix[0][3]) / detA;
		MatrixWork[1][2] = (Matrix[0][0]*Matrix[3][2]*Matrix[1][3] + Matrix[1][0]*Matrix[0][2]*Matrix[3][3] + Matrix[3][0]*Matrix[1][2]*Matrix[0][3] - Matrix[0][0]*Matrix[1][2]*Matrix[3][3] - Matrix[1][0]*Matrix[3][2]*Matrix[0][3] - Matrix[3][0]*Matrix[0][2]*Matrix[1][3]) / detA;
		MatrixWork[2][2] = (Matrix[0][0]*Matrix[1][1]*Matrix[3][3] + Matrix[1][0]*Matrix[3][1]*Matrix[0][3] + Matrix[3][0]*Matrix[0][1]*Matrix[1][3] - Matrix[0][0]*Matrix[3][1]*Matrix[1][3] - Matrix[1][0]*Matrix[0][1]*Matrix[3][3] - Matrix[3][0]*Matrix[1][1]*Matrix[0][3]) / detA;
		MatrixWork[3][2] = (Matrix[0][0]*Matrix[3][1]*Matrix[1][2] + Matrix[1][0]*Matrix[0][1]*Matrix[3][2] + Matrix[3][0]*Matrix[1][1]*Matrix[0][2] - Matrix[0][0]*Matrix[1][1]*Matrix[3][2] - Matrix[1][0]*Matrix[3][1]*Matrix[0][2] - Matrix[3][0]*Matrix[0][1]*Matrix[1][2]) / detA;

		MatrixWork[0][3] = (Matrix[0][1]*Matrix[2][2]*Matrix[1][3] + Matrix[1][1]*Matrix[0][2]*Matrix[2][3] + Matrix[2][1]*Matrix[1][2]*Matrix[0][3] - Matrix[0][1]*Matrix[1][2]*Matrix[2][3] - Matrix[1][1]*Matrix[2][2]*Matrix[0][3] - Matrix[2][1]*Matrix[0][2]*Matrix[1][3]) / detA;
		MatrixWork[1][3] = (Matrix[0][0]*Matrix[1][2]*Matrix[2][3] + Matrix[1][0]*Matrix[2][2]*Matrix[0][3] + Matrix[2][0]*Matrix[0][2]*Matrix[1][3] - Matrix[0][0]*Matrix[2][2]*Matrix[1][3] - Matrix[1][0]*Matrix[0][2]*Matrix[2][3] - Matrix[2][0]*Matrix[1][2]*Matrix[0][3]) / detA;
		MatrixWork[2][3] = (Matrix[0][0]*Matrix[2][1]*Matrix[1][3] + Matrix[1][0]*Matrix[0][1]*Matrix[2][3] + Matrix[2][0]*Matrix[1][1]*Matrix[0][3] - Matrix[0][0]*Matrix[1][1]*Matrix[2][3] - Matrix[1][0]*Matrix[2][1]*Matrix[0][3] - Matrix[2][0]*Matrix[0][1]*Matrix[1][3]) / detA;
		MatrixWork[3][3] = (Matrix[0][0]*Matrix[1][1]*Matrix[2][2] + Matrix[1][0]*Matrix[2][1]*Matrix[0][2] + Matrix[2][0]*Matrix[0][1]*Matrix[1][2] - Matrix[0][0]*Matrix[2][1]*Matrix[1][2] - Matrix[1][0]*Matrix[0][1]*Matrix[2][2] - Matrix[2][0]*Matrix[1][1]*Matrix[0][2]) / detA;

		if( _dst != null ){
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					_dst.Matrix[i][j] = MatrixWork[i][j];
				}
			}
		}else{
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					Matrix[i][j] = MatrixWork[i][j];
				}
			}
		}

		return(true);
	}

	// ---------------------------------------------------
	// 射影行列
	// ---------------------------------------------------
	public void SetProjection(float _fovw ,float _fovh ,float _near ,float _far)
	{
	    float h, w, q;

		Identity();

	    /*w = (float)1.0f/(float)Math.tan(_fovw*Graphics.PI/180.0f*0.5f);  // 1/tan(x) == cot(x)
	    h = (float)1.0f/(float)Math.tan(_fovh*Graphics.PI/180.0f*0.5f);  // 1/tan(x) == cot(x)*/
		float p = (Calc.PI/90.0f);
		w = (float)(1.0/Math.tan(_fovw*p));  // 1/tan(x) == cot(x)
	    h = (float)(1.0/Math.tan(_fovh*p));  // 1/tan(x) == cot(x)
	    q = _far/(_far - _near);

	    Matrix[0][0] = w;
	    Matrix[1][1] = h;
	    Matrix[2][2] = q;
	    Matrix[3][2] = -q*_near;
	    Matrix[2][3] = 1.0f;
	/*
		D3DXMATRIX mat;
		D3DXMatrixPerspectiveFovLH( (D3DXMATRIX *)&mat ,_fovh*DWM_GPI/180.0f ,1.0f ,_near ,_far );
	*/
	}

	// ---------------------------------------------------
	// カメラ行列
	// ---------------------------------------------------
	public void SetCamera(Vector3 _pos ,Vector3 _targetpos ,Vector3 _upvec)
	{
		if( XaxisWork == null )XaxisWork = new Vector3();
		if( YaxisWork == null )YaxisWork = new Vector3();
		if( ZaxisWork == null )ZaxisWork = new Vector3();

		_targetpos.Subtract(_pos ,ZaxisWork);		// ZaxisWork = _targetpos - _pos
		ZaxisWork.Normalize();
		if( _upvec != null ){
			XaxisWork.Copy(_upvec);
		}else{
			XaxisWork.X = 0.0f;
			XaxisWork.Y = 1.0f;
			XaxisWork.Z = 0.0f;
		}
		XaxisWork.CrossProduct(ZaxisWork ,null);
		XaxisWork.Normalize();
		YaxisWork = ZaxisWork;
		YaxisWork.CrossProduct(XaxisWork ,null);

		/*Matrix[0][0] = XaxisWork.X;
		Matrix[1][0] = XaxisWork.Y;
		Matrix[2][0] = XaxisWork.Z;
		Matrix[3][0] = -XaxisWork.Dot(_pos);

		Matrix[0][1] = YaxisWork.X;
		Matrix[1][1] = YaxisWork.Y;
		Matrix[2][1] = YaxisWork.Z;
		Matrix[3][1] = -YaxisWork.Dot(_pos);

		Matrix[0][2] = ZaxisWork.X;
		Matrix[1][2] = ZaxisWork.Y;
		Matrix[2][2] = ZaxisWork.Z;
		Matrix[3][2] = -ZaxisWork.Dot(_pos);

		Matrix[0][3] = ZaxisWork.X;
		Matrix[1][3] = ZaxisWork.Y;
		Matrix[2][3] = ZaxisWork.Z;
		Matrix[3][3] = 1.0f;*/

		Matrix[0][0] = XaxisWork.X;
		Matrix[0][1] = YaxisWork.X;
		Matrix[0][2] = ZaxisWork.X;
		Matrix[0][3] = ZaxisWork.X;

		Matrix[1][0] = XaxisWork.Y;
		Matrix[1][1] = YaxisWork.Y;
		Matrix[1][2] = ZaxisWork.Y;
		Matrix[1][3] = ZaxisWork.Y;

		Matrix[2][0] = XaxisWork.Z;
		Matrix[2][1] = YaxisWork.Z;
		Matrix[2][2] = ZaxisWork.Z;
		Matrix[2][3] = ZaxisWork.Z;

		Matrix[3][0] = -XaxisWork.Dot(_pos);
		Matrix[3][1] = -YaxisWork.Dot(_pos);
		Matrix[3][2] = -ZaxisWork.Dot(_pos);
		Matrix[3][3] = 1.0f;

	}

	// ---------------------------------------------------
	// カメラ行列
	// ---------------------------------------------------
	public void SetCamera(Vector3 _pos ,Quaternion _rot)
	{
		// 通常状態の方向(Z軸向きの視点)をクォータニオンに従って回転
		_rot.ConvToMatrix(this);

		Matrix[3][0] = _pos.X;
		Matrix[3][1] = _pos.Y;
		Matrix[3][2] = _pos.Z;

		Reverse(null);
	}

	// ---------------------------------------------------
	// ビューポート行列
	// ---------------------------------------------------
	public void SetViewPort(float _w ,float _h ,float _near ,float _far ,float _offx ,float _offy)
	{
		Identity();

		/*Matrix[0][0] =  (float)_w / 2.0f;
		Matrix[1][1] = -(float)_h / 2.0f;
		Matrix[2][2] = _far-_near;
		Matrix[3][0] = _offx + (float)_w / 2.0f;
		Matrix[3][1] = _offy + (float)_h / 2.0f;
		Matrix[3][2] = _near;*/

		Matrix[0][0] =  _w * 0.5f;
		Matrix[1][1] = -_h * 0.5f;
		Matrix[2][2] = _far-_near;
		Matrix[3][0] = _offx + _w * 0.5f;
		Matrix[3][1] = _offy + _h * 0.5f;
		Matrix[3][2] = _near;
	}

	// ---------------------------------------------------
	//                 行列のかけ算
	// ---------------------------------------------------
	public void MultiplyFloat(float _scl)
	{
		Matrix[0][0] *= _scl;
		Matrix[0][1] *= _scl;
		Matrix[0][2] *= _scl;
		Matrix[0][3] *= _scl;

		Matrix[1][0] *= _scl;
		Matrix[1][1] *= _scl;
		Matrix[1][2] *= _scl;
		Matrix[1][3] *= _scl;

		Matrix[2][0] *= _scl;
		Matrix[2][1] *= _scl;
		Matrix[2][2] *= _scl;
		Matrix[2][3] *= _scl;

		Matrix[3][0] *= _scl;
		Matrix[3][1] *= _scl;
		Matrix[3][2] *= _scl;
		Matrix[3][3] *= _scl;

	}

	// ---------------------------------------------------
	//                      コピー
	// ---------------------------------------------------
	public void Copy(Matrix44 _src)
	{
		Matrix[0][0] = _src.Matrix[0][0];
		Matrix[0][1] = _src.Matrix[0][1];
		Matrix[0][2] = _src.Matrix[0][2];
		Matrix[0][3] = _src.Matrix[0][3];

		Matrix[1][0] = _src.Matrix[1][0];
		Matrix[1][1] = _src.Matrix[1][1];
		Matrix[1][2] = _src.Matrix[1][2];
		Matrix[1][3] = _src.Matrix[1][3];

		Matrix[2][0] = _src.Matrix[2][0];
		Matrix[2][1] = _src.Matrix[2][1];
		Matrix[2][2] = _src.Matrix[2][2];
		Matrix[2][3] = _src.Matrix[2][3];

		Matrix[3][0] = _src.Matrix[3][0];
		Matrix[3][1] = _src.Matrix[3][1];
		Matrix[3][2] = _src.Matrix[3][2];
		Matrix[3][3] = _src.Matrix[3][3];
	}
}
