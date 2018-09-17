package com.joey.cheetah.sample.Api.exception

import java.io.IOException

/**
 * Description:
 * author:Joey
 * date:2018/9/4
 */
class ApiException(val code:Int, message:String?) : IOException(message)