package cheetah.core.mvp;

/**
 * Description: View in MVP, to control user interface
 * author:Joey
 * date:2018/7/25
 */
public interface IView {
    void toast(String msg);

    void toast(int stringRes);
}
