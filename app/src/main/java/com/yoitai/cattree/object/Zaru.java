package com.yoitai.cattree.object;

public class Zaru {

    public static final float POS_X = 96.0f;
    public static final float POS_Y = 515.0f;
    public static final float SCL_X = 0.5f;
    public static final float SCL_Y = 0.5f;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 230;

    public static boolean histTest(float _posX, float _posY, float _width, float _height, float _sclX, float _sclY) {
        float min_x = POS_X - WIDTH * SCL_X / 2;
        float max_x = POS_X + WIDTH * SCL_X / 2;
        float min_y = POS_Y - HEIGHT * SCL_Y / 2;
        float max_y = POS_Y + HEIGHT * SCL_Y / 2;
        float left_x = _posX - _width * _sclX / 2;
        float right_x = _posX + _width * _sclX / 2;
        float y = _posY + _height * _sclY / 2;

        boolean lx = left_x > min_x && left_x < max_x;
        boolean rx = right_x > min_x && right_x < max_x;
        boolean by = y > min_y && y < max_y;
        return (lx || rx) && by;
    }
}
