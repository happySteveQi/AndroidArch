package com.steve.commonarch.livedata

data class NeedRefreshEvent(
    val from: Int
)

//登录事件
class LoginEvent

class LivenessEvent

//退出事件
data class LogoutEvent(
    val exit: Boolean = true
)

//无效token事件
class InvalidTokenEvent
