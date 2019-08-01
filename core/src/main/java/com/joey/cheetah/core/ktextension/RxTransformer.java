package com.joey.cheetah.core.ktextension;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

public interface RxTransformer<Upstream, Downstream> extends
        ObservableTransformer<Upstream, Downstream>,
        FlowableTransformer<Upstream, Downstream>,
        MaybeTransformer<Upstream, Downstream>,
        SingleTransformer<Upstream, Downstream>,
        CompletableTransformer {
}
