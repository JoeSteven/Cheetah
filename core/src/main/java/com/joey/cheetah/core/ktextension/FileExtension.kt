package com.joey.cheetah.core.ktextension

import java.io.*

/**
 * Description:
 * author:Joey
 * date:2018/10/19
 */

fun File.toBytes():ByteArray? {
    if (!exists()) return null
    var s: ByteArray? = null
    var out: ByteArrayOutputStream? = null
    var input: FileInputStream? = null
    try {
        input = FileInputStream(this)
        out = ByteArrayOutputStream()
        val b = ByteArray(1024)
        while (input.read(b) != -1) {
            out.write(b, 0, b.size)
        }
        s = out.toByteArray()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            if (out != null) {
                out.close()
            }
            if (input != null) {
                input.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return s
}