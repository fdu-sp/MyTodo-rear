package com.zmark.mytodo.gpt;

import java.io.IOException;

/**
 * @author ZMark
 * @date 2024/7/23 下午1:27
 */
public interface IGptClient {

    String call(String userContent) throws IOException;

    String call(String agentContent, String userContent) throws IOException;
}
