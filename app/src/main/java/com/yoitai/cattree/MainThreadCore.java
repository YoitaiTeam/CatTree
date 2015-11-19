package com.yoitai.cattree;

import java.util.concurrent.TimeUnit;

/**
 * Created by DiskK on 2015/10/19.
 */
public class MainThreadCore {
    // メンバー変数
    MainView mMainView;
    static final int FPS = 60;

    // コンストラクタ
    public MainThreadCore(MainView _con)
    {
        mMainView = _con;
    }

    // スレッド処理
    public void run(MainThread _thread)
    {
        long next;
        long lasttick;
        long bet = 1000000000L / FPS;           // フレームレートに合わせたループ間隔を計算(nano)

        lasttick = System.nanoTime();
        next = lasttick + bet;

        while(true){
            // 初期化できていなければ無視
            if( mMainView.checkInitialized() == false ){
                try {
                    TimeUnit.NANOSECONDS.sleep(10000);	// 10μsec毎にチェック
                } catch (InterruptedException e) {
                }
                continue;
            }

            // 毎フレーム処理
            mMainView.frameFunction(mMainView.getMainRenderer().getTargetDrawParams());

            // FPS経過を待つ
            while(true){
                long timetmp;
                timetmp = System.nanoTime();
                if( next < timetmp ){
                    next += bet;
                    if( timetmp >= next )next = timetmp + bet;
                    break;
                }

                // まだFPSの時間経っていないのでスリープ
                try {
                    TimeUnit.NANOSECONDS.sleep(10000);	// 10μsec毎にチェック
                } catch (InterruptedException e) {
                }
            }

            // 描画できれば描画処理を行う
            mMainView.getMainRenderer().drawRequestAndFlip();

            // スレッドの終了リクエストがあればループを抜ける
            if( _thread.checkStopRequestFlag() == true )break;
        }
    }
}
