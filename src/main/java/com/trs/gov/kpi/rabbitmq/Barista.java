package com.trs.gov.kpi.rabbitmq;

import com.trs.gov.kpi.constant.Constants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by he.lang on 2017/6/22.
 */
public interface Barista {

    @Input(Constants.INPUT_CHANNEL)
    SubscribableChannel logInput();

    @Output(Constants.OUTPUT_CHANNEL)
    MessageChannel logOutPut();

    @Input(Constants.INPUT)
    SubscribableChannel defaultInput();

    @Output(Constants.OUTPUT)
    MessageChannel defaultOutput();
}
