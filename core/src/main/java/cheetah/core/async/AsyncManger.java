package cheetah.core.async;

/**
 * Description: Manger to create or obtain AsyncExecutor
 * author:Joey
 * date:2018/8/1
 */
public class AsyncManger {
    private static Class<? extends IAsyncExecutor> sClazz;
    private static IAsyncExecutor sGlobalExecutor;

    /**
     * create a new IAsyncExecutor instance
     * 非全局任务，有内存泄露风险的使用该方法获取
     */
    public static IAsyncExecutor createNew() {
        IAsyncExecutor asyncExecutor = null;
        if (sClazz != null) {
            try {
                asyncExecutor = sClazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (asyncExecutor == null) {
            asyncExecutor = new RxJavaExecutor();
        }
        return asyncExecutor;
    }

    /**
     * get a global IAsyncExecutor
     * 全局任务，无内存泄露风险的使用该方法获取
     */
    public static IAsyncExecutor obtain() {
        if (sGlobalExecutor == null) {
            sGlobalExecutor = createNew();
        }
        return sGlobalExecutor;
    }

    /**
     * inject your own executor
     * 注入自定义的并发执行器
     */
    public static void inject(Class<? extends IAsyncExecutor> executor) {
        sClazz = executor;
    }
}
