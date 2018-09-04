package com.joey.cheetah.core.net.coverter;

import java.io.IOException;

/**
 * Description:
 * author:Joey
 * date:2018/9/3
 */
public interface IApiResponseParser {

    /**
     * parse origin response json ,you can handle status code ,error message here
     * @param responseJson origin response json
     * @return the data json in order to generate Java object
     */
    String parse(String responseJson) throws IOException;
}
