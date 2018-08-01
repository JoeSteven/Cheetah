package joey.cheetah.sample.imagesample

import cheetah.core.ktextension.loadUrl
import cheetah.core.mvp.AbsActivity
import cheetah.core.mvp.AbsPresenter
import joey.cheetah.R
import kotlinx.android.synthetic.main.activity_image.*

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
class ImageActivity : AbsActivity(), IImageShowView {
    override fun show(url: String) {
        ivShow.loadUrl(url)
    }

    override fun initLayout(): Int {
        return R.layout.activity_image
    }

    override fun initView() {
        btNext.setOnClickListener { mPresenter.next() }
        btClear.setOnClickListener { mPresenter.clear() }
        btSave.setOnClickListener {mPresenter.save()}
    }

    private val mPresenter: ImagePresenter = ImagePresenter(this)

    override fun initPresenter(): AbsPresenter<*> {
        return mPresenter
    }

    override fun initData() {
        mPresenter.fetchData()
    }

}