package com.yoitai.glib;

//============================================================================
//                       ３次元座標クラス
//============================================================================
public class Vector3 {
	// ===================================================
	//                      置換文字
	// ===================================================

	// ===================================================
	//                    メンバー定義
	// ===================================================
	public float X;
	public float Y;
	public float Z;

	// ===================================================
	//                     メソッド
	// ===================================================
	// ---------------------------------------------------
	//                   コンストラクタ
	// ---------------------------------------------------
	public Vector3(float _x ,float _y ,float _z)
	{
		X = _x;
		Y = _y;
		Z = _z;
	}

	// ---------------------------------------------------
	//                   コンストラクタ
	// ---------------------------------------------------
	public Vector3()
	{
		X = 0;
		Y = 0;
		Z = 0;
	}

	//=========================================================
	// 長さ
	//=========================================================
	public float CalcLength()
	{
		return((float)Math.sqrt(X*X+Y*Y+Z*Z));
	}

	//=========================================================
	// 正規化
	//=========================================================
	public boolean Normalize()
	{
		float l;

		l = CalcLength();
		if( l == 0.0f )return(false);
		if( l == 1.0f )return(true);

		X /= l;
		Y /= l;
		Z /= l;

		return(true);
	}

	// ---------------------------------------------------
	//                       内積
	// ---------------------------------------------------
	public float Dot(Vector3 _src)
	{
		return(X*_src.X + Y*_src.Y + Z*_src.Z);
	}
	public float Dot(float _x ,float _y ,float _z)
	{
		return(X*_x + Y*_y + Z*_z);
	}

	// ---------------------------------------------------
	//                      外積
	// ---------------------------------------------------
	public Vector3 CrossProduct(Vector3 _src){
		return(CrossProduct(_src ,null));
	}
	public Vector3 CrossProduct(Vector3 _src ,Vector3 _dst)
	{
		float x,y,z;

		x = Y*_src.Z - Z*_src.Y;
		y = Z*_src.X - X*_src.Z;
		z = X*_src.Y - Y*_src.X;

		if( _dst != null ){
			_dst.Set(x, y, z);
			return(_dst);
		}

		X = x;
		Y = y;
		Z = z;
		return(this);
	}

	// ---------------------------------------------------
	//                   コピー
	// ---------------------------------------------------
	public void Copy(Vector3 _src)
	{
		X = _src.X;
		Y = _src.Y;
		Z = _src.Z;
	}

	// ---------------------------------------------------
	//                   設定
	// ---------------------------------------------------
	public void Set(float _x ,float _y ,float _z)
	{
		X = _x;
		Y = _y;
		Z = _z;
	}

	// ---------------------------------------------------
	//                   加算
	// ---------------------------------------------------
	public void Add(Vector3 _src)
	{
		Add(_src ,null);
	}
	public void Add(Vector3 _src ,Vector3 _dst)
	{
		if( _dst == null ){
			X += _src.X;
			Y += _src.Y;
			Z += _src.Z;
		}else{
			_dst.X = X + _src.X;
			_dst.Y = Y + _src.Y;
			_dst.Z = Z + _src.Z;
		}
	}

	// ---------------------------------------------------
	//                   減算
	// ---------------------------------------------------
	public void Subtract(Vector3 _src)
	{
		Subtract(_src ,null);
	}
	public void Subtract(Vector3 _src ,Vector3 _dst)
	{
		if( _dst == null ){
			X -= _src.X;
			Y -= _src.Y;
			Z -= _src.Z;
		}else{
			_dst.X = X - _src.X;
			_dst.Y = Y - _src.Y;
			_dst.Z = Z - _src.Z;
		}
	}

	// ---------------------------------------------------
	//                   乗算
	// ---------------------------------------------------
	public void Multiply(float _t)
	{
		Multiply(_t ,null);
	}

	public void Multiply(float _t ,Vector3 _dst)
	{
		if( _dst == null ){
			X *= _t;
			Y *= _t;
			Z *= _t;
		}else{
			_dst.X = X * _t;
			_dst.Y = Y * _t;
			_dst.Z = Z * _t;
		}
	}

	// ---------------------------------------------------
	//                   除算
	// ---------------------------------------------------
	public void Divide(float _t)
	{
		Divide(_t ,null);
	}
	public void Divide(float _t ,Vector3 _dst)
	{
		if( _dst == null ){
			X /= _t;
			Y /= _t;
			Z /= _t;
		}else{
			_dst.X = X / _t;
			_dst.Y = Y / _t;
			_dst.Z = Z / _t;
		}
	}

}
