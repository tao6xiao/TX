package com.trs.gov.kpi.rabbitmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by he.lang on 2017/6/22.
 */
public interface Barista {
    String INPUT_CHANNEL = "input_channel";
    String OUTPUT_CHANNEL = "output_channel";
    String INPUT = "input";
    String OUTPUT = "output";

    @Input
    SubscribableChannel logInput();

    @Output
    MessageChannel logOutPut();

    @Input(Barista.INPUT)
    SubscribableChannel input();

    @Output(Barista.OUTPUT)
    MessageChannel output();
}
