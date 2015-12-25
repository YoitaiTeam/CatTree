package com.yoitai.cattree;

// ステージ管理クラス
public class Stage {
    public class BlockParams {
        public static final int TYPE_TOP = 0;       // 上障害物
        public static final int TYPE_BOTTOM = 1;    // 下障害物

        public int mType;                           // ブロックのタイプ (TYPE_xxxx)
        public float mPosX;                         // ステージ上のＸ座標
        public float mPosY;                         // ステージ上のＹ座標

        public BlockParams(int _type, float _x, float _y) {
            mType = _type;
            mPosX = _x;
            mPosY = _y;
        }
    }

    BlockParams mBlockParams[];
    MainView mMainView;

    public Stage() {
        int count = 100;
        // 障害物を置く
        mBlockParams = new BlockParams[count];
        for (int i = 0; i < 50; i++) {
            mBlockParams[i * 2 + 0] = new BlockParams(BlockParams.TYPE_BOTTOM, 512.0f + 512.0f * i, 280.0f);
            mBlockParams[i * 2 + 1] = new BlockParams(BlockParams.TYPE_TOP, 768.0f + 512.0f * i, 200.0f);
        }
    }

    public void setView(MainView _view) {
        mMainView = _view;
    }

    // 毎フレーム処理
    public void frameFunction() {
    }

    // 描画処理
    public void draw(float _x) {
        DrawParams params;

        // 背景描画
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(Game.TEXNO_BACK);
        params.getScl().X = 0.5f;
        params.getScl().Y = 0.5f;

        // CATTREE描画
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(Game.TEXNO_CATTREE);

        // MENU描画
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(Game.MENU01);
        params = mMainView.getMainRenderer().allocDrawParams();
        params.setSprite(Game.MENU02);

        // 障害物描画
        /*int cnt = mBlockParams.length;
        for (int i = 0; i < cnt; i++) {
            BlockParams bp = mBlockParams[i];
            float x = bp.mPosX - _x;
            if ((x >= -109.0f) && (x <= MainRenderer.CONTENTS_W)) {
                switch (bp.mType) {
                    case BlockParams.TYPE_TOP: {
                        // 上障害物
                        params = mMainView.getMainRenderer().allocDrawParams();
                        params.setSprite(Game.TEXNO_TBLOCK);
                        params.getPos().X = bp.mPosX - _x;
                        params.getPos().Y = bp.mPosY;
                    }
                    break;
                    case BlockParams.TYPE_BOTTOM: {
                        // 下障害物
                        params = mMainView.getMainRenderer().allocDrawParams();
                        params.setSprite(Game.TEXNO_BBLOCK);
                        params.getPos().X = bp.mPosX - _x;
                        params.getPos().Y = bp.mPosY;
                    }
                    break;
                }
            }
        }*/
    }

    public boolean hitTest(float _x, float _y, float _w, float _h) {
        float ax0 = _x - _w / 2;
        float ax1 = _x + _w / 2;
        float ay0 = _y - _h / 2;
        float ay1 = _y + _h / 2;

        if (ay0 < 0) return (true);
        if (ay1 >= MainRenderer.CONTENTS_H) return (true);
        if (1 == 1) return false;

        // 障害物描画
        float bx0, bx1, by0 = 0.0f, by1 = 0.0f;
        int cnt = mBlockParams.length;
        for (int i = 0; i < cnt; i++) {
            BlockParams bp = mBlockParams[i];
            bx0 = bp.mPosX;
            bx1 = bp.mPosX + 109.0f;
            switch (bp.mType) {
                case BlockParams.TYPE_TOP: {
                    // 上障害物
                    by0 = 0.0f;
                    by1 = bp.mPosY;
                }
                break;
                case BlockParams.TYPE_BOTTOM: {
                    // 下障害物
                    by0 = bp.mPosY;
                    by1 = MainRenderer.CONTENTS_H;
                }
                break;
            }

            if (ax1 < bx0) continue;
            if (ax0 > bx1) continue;
            if (ay1 < by0) continue;
            if (ay0 > by1) continue;

            return (true);
        }

        return (false);
    }
}
