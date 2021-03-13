package org.inego.multisrs.ui.focus

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*

class FocusUtil {
    private val requesters = mutableListOf<FocusRequesterWithOrder>()

    fun moveNext(requester: FocusRequester?) {
        var i = if (requester == null) 0
        else 1 + requesters.indexOfFirst { it.focusRequester == requester }

        if (i >= requesters.size) {
            i = 0
        }
        requesters[i].focusRequester.requestFocus()
    }

    fun movePrev(requester: FocusRequester?) {
        var i = if (requester == null) 0
        else requesters.indexOfFirst { it.focusRequester == requester } - 1

        if (i < 0) {
            i = requesters.size - 1
        }
        requesters[i].focusRequester.requestFocus()
    }

    fun requester(order: Int): FocusRequester {
        val focusRequester = FocusRequester()
        requesters.add(FocusRequesterWithOrder(focusRequester, order))
        requesters.sortBy { it.order }
        return focusRequester
    }
}


class FocusRequesterWithOrder(
    val focusRequester: FocusRequester,
    val order: Int
)


fun Modifier.withFocus(focusUtil: FocusUtil, order: Int): Modifier {

    val focusRequester = focusUtil.requester(order)

    return this
        .focusRequester(focusRequester)
        .onPreviewKeyEvent {
            if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
                if (it.isShiftPressed) {
                    focusUtil.movePrev(focusRequester)
                } else {
                    focusUtil.moveNext(focusRequester)
                }
                true
            } else {
                false
            }
        }
}
