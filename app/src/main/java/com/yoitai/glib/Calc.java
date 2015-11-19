package com.yoitai.glib;

public class Calc {
	// 円周率
	public static final float PI = 3.141592654f;

	//===========================================
	// 角度を１サイクル(0～<PIx2)に変換 ラジアン
	//===========================================
	static public float RevAngleOneCycleRad(float _angle)
	{
		if( _angle < 0.0 ){
			_angle += (Math.floor(Math.abs(_angle) / (PI*2)) + 1.0f) * PI*2;
		}else{
			if( _angle >= PI*2 ){
				_angle -= Math.floor(_angle / (PI*2)) * PI*2;
			}
		}
		return(_angle);
	}

	//===========================================
	//   角度を１サイクル(0～<360)に変換 度数
	//===========================================
	static public float RevAngleOneCycle(float _angle)
	{
		if( _angle < 0.0 ){
			_angle += (Math.floor(Math.abs(_angle) / 360.0f) + 1.0f) * 360.0f;
		}else{
			if( _angle >= 360.0f ){
				_angle -= Math.floor(_angle / 360.0f) * 360.0f;
			}
		}
		return(_angle);
	}

	//===========================================
	// 角度計算 ラジアン
	//===========================================
	static public float CalcAngleRad(Vector2 _src ,Vector2 _dst ,boolean _rev)
	{
		float x;
		float y;
		float len;
		float a;
		float r;

		if( _rev == true ){
			// Yは逆転
			x = _dst.X - _src.X;
			y = _src.Y - _dst.Y;
		}else{
			// Yは逆転しない
			x = _dst.X - _src.X;
			y = _dst.Y - _src.Y;
		}

		len = (float) Math.sqrt( x*x + y*y );

		// 同じ位置で角度計算は不可能
		if( len == 0 )return(-1.0f);

		x /= len;
		y /= len;
		a = x * 1.0f + y * 0.0f;			// (1,0)との内積

		if( y < 0 ){
			r = (float)(PI*2.0f-Math.acos(a));
		}else{
			r =(float)Math.acos(a);
		}

		return(r);
	}

	//===========================================
	// 角度計算 ラジアン
	//===========================================
	static public float CalcAngleRad(float _x ,float _y ,boolean _rev)
	{
		float len;
		float a;
		float r;

		if( _rev )_y *= -1.0f;

		len = (float) Math.sqrt( _x*_x + _y*_y );

		// 同じ位置で角度計算は不可能
		if( len == 0 )return(-1.0f);

		_x /= len;
		_y /= len;
		a = _x * 1.0f + _y * 0.0f;			// (1,0)との内積

		if( _y < 0 ){
			r = (float)(PI*2.0f-Math.acos(a));
		}else{
			r =(float)Math.acos(a);
		}

		return(r);
	}

	//===========================================
	// 角度計算 度数
	//===========================================
	static public float CalcAngle(Vector2 _src ,Vector2 _dst ,boolean _revy)
	{
		float ret;

		ret = CalcAngleRad(_src ,_dst ,_revy);
		if( ret == -1.0f )return(-1.0f);

		return(ret*180.0f/PI);
	}

	//===========================================
	// 角度計算 度数
	//===========================================
	static public float CalcAngle(float _x ,float _y ,boolean _revy)
	{
		float ret;

		ret = CalcAngleRad(_x ,_y ,_revy);
		if( ret == -1.0f )return(-1.0f);

		return(ret*180.0f/PI);
	}

	//===========================================
	// 角度差計算 ラジアン
	//===========================================
	static public float CalcSubDirectionRad(float _sd ,float _dd)
	{
		float sd;
		float dd;
		float d;

		sd = RevAngleOneCycleRad(_sd);
		dd = RevAngleOneCycleRad(_dd);

		if( sd > dd )d = sd - dd;
		else d = dd - sd;

		if( d > PI ){
			d = PI*2.0f - d;
		}

		return(d);
	}

	//===========================================
	// 角度差計算 度数
	//===========================================
	static public float CalcSubDirection(float _sd ,float _dd)
	{
		float sd;
		float dd;
		float d;

		sd = RevAngleOneCycle(_sd);
		dd = RevAngleOneCycle(_dd);

		if( sd > dd )d = sd - dd;
		else d = dd - sd;

		if( d > 180.0f ){
			d = 360.0f - d;
		}

		return(d);
	}

	//===========================================
	// 角度差計算 ラジアン
	//===========================================
	static public float CalcVecAngleRad(Vector2 _a ,Vector2 _b)
	{
		if( (_a.X == 0.0f) && (_a.Y == 0.0f) )return(PI);
		if( (_b.X == 0.0f) && (_b.Y == 0.0f) )return(PI);

		float lena,lenb;

		lena = _a.CalcLength();
		lenb = _b.CalcLength();

		float c = (_a.X * _b.X + _a.Y*_b.Y) / (lena*lenb);

		return((float)Math.acos(c));
	}

	//===========================================
	// 角度差計算 度数
	//===========================================
	static public float CalcVecAngle(Vector2 _a ,Vector2 _b)
	{
		float ret;

		ret = CalcVecAngleRad(_a ,_b);

		return(ret*180.0f/PI);
	}

	//===========================================
	// テクスチャサイズ計算
	//===========================================
	static public int CalcTexSize(int _v)
	{
		int v = 2;
		if( _v == 1 )return(1);

		while(_v > v)v <<= 1;

		return((int)v);
	}
}
