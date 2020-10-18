package com.donfyy.main_module

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.donfyy.common_module.base.BaseActivity
import com.donfyy.market_api.MarketRouterTable
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.ArrayList

class MainActivity : BaseActivity() {
    private lateinit var fragmentsList: ArrayList<Fragment>
    private lateinit var bottomNavigationView: BottomNavigationView
    private val fragmentMap = hashMapOf<Int, Fragment>()
    private val fragmentClassMap = hashMapOf(
        R.id.fragment_1 to MarketRouterTable.MAIN_FRAGMENT,
        R.id.fragment_2 to MarketRouterTable.MAIN_FRAGMENT,
        R.id.fragment_3 to MarketRouterTable.MAIN_FRAGMENT,
        R.id.fragment_4 to MarketRouterTable.MAIN_FRAGMENT,
        R.id.fragment_5 to MarketRouterTable.MAIN_FRAGMENT
    )
    var currentItemId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { show(it.itemId) }
        show(R.id.fragment_1)
    }

    private fun show(newItemId: Int): Boolean {
        if (newItemId == currentItemId) return false
        val oldItemId = currentItemId
        supportFragmentManager.beginTransaction().apply {
            val sb = StringBuilder()
            fragmentMap[oldItemId]?.let {
                hide(it)
                setMaxLifecycle(it, Lifecycle.State.STARTED)
                sb.append("hide ${it.javaClass.simpleName} ")
            }
            var f = fragmentMap[newItemId]
            if (f == null) {
                f = ARouter.getInstance().build(fragmentClassMap[newItemId]).navigation() as Fragment
                fragmentMap[newItemId] = f
                add(R.id.content, f)
                sb.append("add ${f.javaClass.simpleName} ")
            } else {
                show(f)
                setMaxLifecycle(f, Lifecycle.State.RESUMED)
                sb.append("show ${f.javaClass.simpleName} ")
            }
            commitAllowingStateLoss()
            LogUtils.d(sb.toString())
        }
        currentItemId = newItemId
        return true
    }
}