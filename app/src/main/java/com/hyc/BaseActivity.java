package com.hyc;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by hyc on 2017/4/20 18:53
 */

public class BaseActivity  extends SwipeBackActivity{
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mSwipeBackLayout = getSwipeBackLayout( );
        //其中EDGE_方向   (Left, Top, Right, Bottom)分别代表上下左右， All代表全部都支持
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }
}
