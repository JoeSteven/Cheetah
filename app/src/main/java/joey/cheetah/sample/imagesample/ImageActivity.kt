package joey.cheetah.sample.imagesample

import com.joey.cheetah.core.mvp.AbsActivity
import com.joey.cheetah.core.ktextension.loadUrl
import joey.com.joey.cheetah.R
import kotlinx.android.synthetic.main.activity_image.*

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
class ImageActivity : AbsActivity(), IImageShowView {
    override fun initPresenter() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun show(url: String) {
        ivShow.loadUrl(url)
    }

    override fun initLayout(): Int {
        return R.layout.activity_image
    }

    override fun initView() {
//        btNext.setOnClickListener { mPresenter.next() }
//        btClear.setOnClickListener { mPresenter.clear() }
//        btSave.setOnClickListener {mPresenter.save()}
    }


    override fun initData() {
//        mPresenter.fetchData()
    }

}