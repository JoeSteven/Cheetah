package joey.cheetah.sample.imagesample

import com.joey.cheetah.core.mvp.IView

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
interface IImageShowView : IView {
    fun show(url: String)
}