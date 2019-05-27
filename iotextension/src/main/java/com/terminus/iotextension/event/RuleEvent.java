package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/13
 */
public class RuleEvent extends BaseEvent {

    public final String personId;
    public final String projectId;

    public RuleEvent(String personId, String projectId) {
        this.personId = personId;
        this.projectId = projectId;
    }
}
