package com.yuyan.imemodule.view.keyboard.container

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuyan.imemodule.R
import com.yuyan.imemodule.adapter.PrefixAdapter
import com.yuyan.imemodule.entity.keyboard.SoftKey
import com.yuyan.imemodule.manager.InputModeSwitcherManager
import com.yuyan.imemodule.utils.KeyboardLoaderUtil.Companion.instance
import com.yuyan.imemodule.view.keyboard.InputView
import com.yuyan.imemodule.view.keyboard.TextKeyboard

/**
 * 数字键盘容器
 *
 * 包含输入键盘[TextKeyboard]及拼音选择界面。
 *
 * 与九宫格键盘容器[T9TextContainer]类似。
 */
@SuppressLint("ViewConstructor")
class NumberContainer(context: Context?, inputView: InputView) : InputBaseContainer(context, inputView) {
    // 键盘、候选词界面上符号(T9左侧、手写右侧)、候选拼音ListView
    private var mRVLeftPrefix : RecyclerView = inflate(getContext(), R.layout.sdk_view_rv_prefix, null) as RecyclerView

    /**
     * 更新软键盘布局
     */
    override fun updateSkbLayout() {
        if (null == mMajorView) {
            mMajorView = TextKeyboard(context)
            val params: ViewGroup.LayoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(mMajorView, params)
            mMajorView!!.setResponseKeyEvent(inputView)
        }
        val softKeyboard = instance.getSoftKeyboard(InputModeSwitcherManager.MASK_SKB_LAYOUT_NUMBER)
        mMajorView!!.setSoftKeyboard(softKeyboard)
        updateKeyboardView()
        mMajorView!!.invalidate()
    }

    // 更新键盘上侧边符号列表
    private fun updateKeyboardView() {
        val prefixLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRVLeftPrefix.setLayoutManager(prefixLayoutManager)
        if (mRVLeftPrefix.parent != null) {
            val parent = mRVLeftPrefix.parent as ViewGroup
            parent.removeView(mRVLeftPrefix)
        }
        addView(mRVLeftPrefix, createLayoutParams())
        val strs = resources.getStringArray(R.array.SymbolRealNumber)
        val adapter = PrefixAdapter(context, strs)
        adapter.setOnItemClickLitener { _, _, position ->
            val softKey = SoftKey(strs[position])
            inputView.responseKeyEvent(softKey)
        }
        mRVLeftPrefix.setAdapter(adapter)
    }

    private fun createLayoutParams(): LayoutParams {
        val softKeyboard = mMajorView!!.getSoftKeyboard()
        val softKeySymbolHolder =
            softKeyboard.getKeyByCode(InputModeSwitcherManager.USER_DEF_KEYCODE_LEFT_SYMBOL_12)
        val prefixLayoutParams = LayoutParams(
            softKeySymbolHolder!!.width(), LayoutParams.MATCH_PARENT
        )
        prefixLayoutParams.setMargins(
            softKeyboard.keyXMargin,
            softKeySymbolHolder.mTop + softKeyboard.keyYMargin,
            softKeyboard.keyXMargin,
            softKeyboard.skbCoreHeight - softKeySymbolHolder.mBottom + softKeyboard.keyYMargin
        )
        return prefixLayoutParams
    }
}
