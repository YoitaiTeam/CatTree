package com.yoitai.cattree;

public class MainThread extends Thread {
	MainThreadCore mCore;
	boolean mStopReqFlag;

	// コンストラクタ
	public MainThread(MainThreadCore _core)
	{
		mCore = _core;
		mStopReqFlag = false;
	}

	@Override
	public void run()
	{
		// 描画よりも若干高い優先度のスレッドにする
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DISPLAY-1);

		// 実行
		mCore.run(this);
	}

	// 停止リクエストフラグ制御
	public synchronized void setStopRequestFlag()
	{
		mStopReqFlag = true;
	}
	public synchronized boolean checkStopRequestFlag()
	{
		return(mStopReqFlag);
	}
}
