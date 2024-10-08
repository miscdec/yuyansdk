package com.yuyan.imemodule.entity.keyboard

/**
 * 按键的可切换状态
 */
class ToggleState {
    var stateId = 0
        get() = field
        set
    var label: String? = null

    constructor(stateId: Int) {
        this.stateId = stateId
    }

    constructor(keyLabel: String?, stateId: Int) {
        this.stateId = stateId
        label = keyLabel
    }
}
