package com.yoitai.glib;

//============================================================================
//                      クォータニオンクラス
//============================================================================
public class Quaternion {
	// ===================================================
	//                      置換文字
	// ===================================================

	// ===================================================
	//                    メンバー定義
	// ===================================================
	public float X;
	public float Y;
	public float Z;
	public float W;

	// ===================================================
	//                     メソッド
	// ===================================================
	// ---------------------------------------------------
	//                   コンストラクタ
	// ---------------------------------------------------
	public Quaternion()
	{
		Identity();
	}

	public Quaternion(float _x ,float _y ,float _z ,float _w)
	{
		X = _x;
		Y = _y;
		Z = _z;
		W = _w;
	}

	//=========================================================
	//  単位クォータニオン
	//=========================================================
	public void Identity()
	{
		X = 0.0f;
		Y = 0.0f;
		Z = 0.0f;
		W = 1.0f;
	}

	//=========================================================
	// 指定軸の回転クォータニオン作成
	//=========================================================
	public void SetRotationAxis(Vector3 _axis ,float _rot)
	{
		SetRotationAxis(_axis.X ,_axis.Y ,_axis.Z ,_rot);
	}

	public void SetRotationAxis(float _ax ,float _ay ,float _az ,float _rot)
	{
		float rad = _rot * 0.5f;
		float s = (float)Math.sin(rad);
		float x,y,z;
		float len;

		x = _ax;
		y = _ay;
		z = _az;
		len = (float)Math.sqrt(x*x+y*y+z*z);

		W = (float)Math.cos(rad);

		X = x/len * s;
		Y = y/len * s;
		Z = z/len * s;
	}

	//=========================================================
	// クォータニオンを指定軸で回転
	//=========================================================
	public void RotationAxis(Vector3 _axis ,float _rot ,Quaternion _work)
	{
		if( _work == null ){
			_work = new Quaternion();
		}
		_work.SetRotationAxis(_axis ,_rot);
		Multiply(_work ,null);
		_work = null;
	}

	public void RotationAxis(float _ax ,float _ay ,float _az ,float _rot ,Quaternion _work)
	{
		if( _work == null ){
			_work = new Quaternion();
		}
		_work.SetRotationAxis(_ax ,_ay ,_az ,_rot);
		Multiply(_work ,null);
		_work = null;
	}

	public void RotationAxis(Vector3 _axis ,float _rot)
	{
		RotationAxis(_axis ,_rot ,null);
	}

	public void RotationAxis(float _ax ,float _ay ,float _az ,float _rot)
	{
		RotationAxis(_ax ,_ay ,_az ,_rot ,null);
	}

	//=========================================================
	// 乗算
	//=========================================================
	public Quaternion Multiply(Quaternion _src ,Quaternion _dst)
	{
		float x,y,z,w;

		x = _src.W * X + _src.X * W + _src.Y * Z - _src.Z * Y;
	    y = _src.W * Y - _src.X * Z + _src.Y * W + _src.Z * X;
	    z = _src.W * Z + _src.X * Y - _src.Y * X + _src.Z * W;
	    w = _src.W * W - _src.X * X - _src.Y * Y - _src.Z * Z;

		if( _dst != null ){
			_dst.X = x;
			_dst.Y = y;
			_dst.Z = z;
			_dst.W = w;
			return(_dst);
		}

		X = x;
		Y = y;
		Z = z;
		W = w;
		return(this);
	}

	//=========================================================
	// クォータニオンから回転行列へ変換
	//=========================================================
	public void ConvToMatrix(Matrix44 _dst)
	{
		float sx = X * X;
	    float sy = Y * Y;
	    float sz = Z * Z;
	    float cx = Y * Z;
	    float cy = X * Z;
	    float cz = X * Y;
	    float wx = W * X;
	    float wy = W * Y;
	    float wz = W * Z;

		_dst.Matrix[0][0] = 1.0f - 2.0f * (sy + sz);
		_dst.Matrix[0][1] = 2.0f * (cz + wz);
		_dst.Matrix[0][2] = 2.0f * (cy - wy);
		_dst.Matrix[0][3] = 0.0f;
		_dst.Matrix[1][0] = 2.0f * (cz - wz);
		_dst.Matrix[1][1] = 1.0f - 2.0f * (sx + sz);
		_dst.Matrix[1][2] = 2.0f * (cx + wx);
		_dst.Matrix[1][3] = 0.0f;
		_dst.Matrix[2][0] = 2.0f * (cy + wy);
		_dst.Matrix[2][1] = 2.0f * (cx - wx);
		_dst.Matrix[2][2] = 1.0f - 2.0f * (sx + sy);
		_dst.Matrix[2][3] = 0.0f;
		_dst.Matrix[3][0] = 0.0f;
		_dst.Matrix[3][1] = 0.0f;
		_dst.Matrix[3][2] = 0.0f;
		_dst.Matrix[3][3] = 1.0f;
	}


	//=========================================================
	// 大きさ計算
	//=========================================================
	public float CalcLength()
	{
		return((float)Math.sqrt(X*X+Y*Y+Z*Z+W*W));
	}

	//=========================================================
	// 回転行列からクォータニオン変換
	//=========================================================
	public void ConvFromMatrix(Matrix44 _dst)
	{
	    float s;
	    float tr = _dst.Matrix[0][0] + _dst.Matrix[1][1] + _dst.Matrix[2][2] + 1.0f;

		if( tr >= 1.0f ){
			s = 0.5f / (float)Math.sqrt(tr);
			W = 0.25f / s;
			X = (_dst.Matrix[1][2] - _dst.Matrix[2][1]) * s;
			Y = (_dst.Matrix[2][0] - _dst.Matrix[0][2]) * s;
			Z = (_dst.Matrix[0][1] - _dst.Matrix[1][0]) * s;
		}else{
			float max;
			if( _dst.Matrix[1][1] > _dst.Matrix[2][2] )max = _dst.Matrix[1][1];
			else max = _dst.Matrix[2][2];

			if( max < _dst.Matrix[0][0] ){
				s = (float)Math.sqrt(_dst.Matrix[0][0] - (_dst.Matrix[1][1] + _dst.Matrix[2][2]) + 1.0f);
				float x = s * 0.5f;
				s = 0.5f / s;

				X = x;
				Y = (_dst.Matrix[0][1] + _dst.Matrix[1][0]) * s;
				Z = (_dst.Matrix[2][0] + _dst.Matrix[0][2]) * s;
				W = (_dst.Matrix[1][2] - _dst.Matrix[2][1]) * s;
			}else if (max == _dst.Matrix[1][1]) {
				s = (float)Math.sqrt(_dst.Matrix[1][1] - (_dst.Matrix[2][2] + _dst.Matrix[0][0]) + 1.0f);
				float y = s * 0.5f;
				s = 0.5f / s;

				X = (_dst.Matrix[0][1] + _dst.Matrix[1][0]) * s;
				Y = y;
				Z = (_dst.Matrix[1][2] + _dst.Matrix[2][1]) * s;
				W = (_dst.Matrix[2][0] - _dst.Matrix[0][2]) * s;
			}else{
				s = (float)Math.sqrt(_dst.Matrix[2][2] - (_dst.Matrix[0][0] + _dst.Matrix[1][1]) + 1.0f);
				float z = s * 0.5f;
				s = 0.5f / s;

				X = (_dst.Matrix[2][0] + _dst.Matrix[0][2]) * s;
				Y = (_dst.Matrix[1][2] + _dst.Matrix[2][1]) * s;
				Z = z;
				W = (_dst.Matrix[0][1] - _dst.Matrix[1][0]) * s;
			}
		}
	}

	//=========================================================
	// ベクトルの回転
	//=========================================================
	public Vector3 RotateVec(Vector3 _src ,Vector3 _dst ,Matrix44 _work)
	{
		if( _work == null ){
			_work = new Matrix44();
		}
		ConvToMatrix(_work);
		_work.TransformCoord(_src ,_dst);
		_work = null;
		return(_dst);
	}

	//=========================================================
	// 球面補間
	//=========================================================
	public void Slerp(Quaternion _src ,float _t ,Quaternion _dst)
	{
		float qr = _src.W * W + _src.X * X + _src.Y * Y + _src.Z * Z;

		// 誤差などで越えてしまっているものを対処
		if( qr > 1.0f  )qr = 1.0f;
		if( qr < -1.0f )qr = -1.0f;

		float ss = 1.0f - (qr * qr);

		if( _dst == null )_dst = this;

		if( ss == 0.0f ){
			_dst.X = _src.X;
			_dst.Y = _src.Y;
			_dst.Z = _src.Z;
			_dst.W = _src.W;
		}else{
			float ph = (float)Math.acos(qr);

			if( (qr < 0.0f) && (ph > Calc.PI / 2.0f) ){
				float s1,s2;
				qr = - _src.W * W - _src.X * X - _src.Y * Y - _src.Z * Z;
				ph = (float)Math.acos(qr);
				s1 = (float)Math.sin(ph * (1.0f - _t)) / (float)Math.sin(ph);
				s2 = (float)Math.sin(ph *  _t) / (float)Math.sin(ph);

				_dst.X = _src.X * s1 - X * s2;
				_dst.Y = _src.Y * s1 - Y * s2;
				_dst.Z = _src.Z * s1 - Z * s2;
				_dst.W = _src.W * s1 - W * s2;
			}else{
				float s1,s2;
				s1 = (float)Math.sin(ph * (1.0f - _t)) / (float)Math.sin(ph);
				s2 = (float)Math.sin(ph *  _t) / (float)Math.sin(ph);

				_dst.X = _src.X * s1 + X * s2;
				_dst.Y = _src.Y * s1 + Y * s2;
				_dst.Z = _src.Z * s1 + Z * s2;
				_dst.W = _src.W * s1 + W * s2;
			}
		}
	}

	//=========================================================
	// 逆転
	//=========================================================
	public void Inverse()
	{
		Inverse(null);
	}
	public void Inverse(Quaternion _dst)
	{
		if( _dst == null ){
			X *= -1.0f;
			Y *= -1.0f;
			Z *= -1.0f;
		}else{
			_dst.X = X * -1.0f;
			_dst.Y = Y * -1.0f;
			_dst.Z = Z * -1.0f;
			_dst.W = W;
		}
	}

	//=========================================================
	// コピー
	//=========================================================
	public void Copy(Quaternion _src)
	{
		X = _src.X;
		Y = _src.Y;
		Z = _src.Z;
		W = _src.W;
	}
}
