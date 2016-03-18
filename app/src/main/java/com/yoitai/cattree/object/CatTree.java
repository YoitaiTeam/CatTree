package com.yoitai.cattree.object;

import com.yoitai.cattree.CatTreeData;
import com.yoitai.cattree.Game;
import com.yoitai.cattree.Input;
import com.yoitai.cattree.MainView;
import com.yoitai.cattree.Menu;
import com.yoitai.cattree.Stage;

import java.util.HashSet;
import java.util.Set;

/**
 * 猫の出現を管理するクラス
 */
public class CatTree {

    CatTreeData mData;  // 諸々データ
    MainView mMainView; // MainView
    Input mInput;       // 入力
    Stage mStage;       // ステージ
    WateringPot mWateringPot; // ポット

    private final int CAT_MAX = 20;  // 画面に表示する猫の最大数
    private final int CAT_GROW_INTERVAL = 1;

    Cat[] mCat;
    Set mAliveCat = new HashSet();
    long mLastGrowTime;

    public CatTree() {
        mCat = new Cat[CAT_MAX];
        for (int i = 0; i < CAT_MAX; i++) {
            mCat[i] = new Cat();
            mCat[i].mPatternNo = grow();
        }
        mLastGrowTime = System.currentTimeMillis();
    }

    public void init() {
        int yieldsCount = CatTreeData.getInt(CatTreeData.CROP_YIELDS, 0);
        for (int i = 0; i < CAT_MAX; i++) {
            if (i < yieldsCount) {
                mCat[i].growUp(grow());
            }
        }
    }

    public int grow() {
        return (int) (Math.random() * Game.CAT_KIND_NUM) + Game.TEXNO_BASE_CAT;
    }

    // setter
    public void setView(MainView _view) {
        mMainView = _view;
        for (int i = 0; i < CAT_MAX; i++) {
            mCat[i].setView(mMainView);
        }
    }

    public void setInput(Input _input) {
        mInput = _input;
        for (int i = 0; i < CAT_MAX; i++) {
            mCat[i].setInput(mInput);
        }
    }

    public void setStage(Stage _stage) {
        mStage = _stage;
        for (int i = 0; i < CAT_MAX; i++) {
            mCat[i].setStage(mStage);
        }
    }

    public void setMenu(Menu _menu) {
        for (int i = 0; i < CAT_MAX; i++) {
            mCat[i].setMenu(_menu);
        }
    }

    public void setWateringPot(WateringPot _pot) {
        mWateringPot = _pot;
    }

    public void frameFunction() {
        for (int i = 0; i < CAT_MAX; i++) {
            mCat[i].frameFunction();
        }
        if (mWateringPot.residualQuantity() == 0.0f) return; // 水が空なら猫は生えない

        if ((System.currentTimeMillis() - mLastGrowTime) / 1000 > CAT_GROW_INTERVAL) {
            mLastGrowTime = System.currentTimeMillis();
            for (int i = 0; i < CAT_MAX; i++) {
                if (mCat[i].growUp(grow())) break;
                managedCrops(CatTreeData.CROP_ADD);
            }
        }
    }

    private void managedCrops(int _cal) {
        try {
            int yields = CatTreeData.getInt(CatTreeData.CROP_YIELDS, 0);
            CatTreeData.setInt(CatTreeData.CROP_YIELDS, yields + _cal);
        } catch (Exception e) {

        }
    }

    // 描画
    public void draw() {
        for (int i = 0; i < CAT_MAX; i++) {
            mCat[i].draw();
        }
    }
}
