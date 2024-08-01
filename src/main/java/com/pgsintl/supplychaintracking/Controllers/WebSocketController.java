package com.pgsintl.supplychaintracking.Controllers;

import com.pgsintl.supplychaintracking.Dto.OrdersTrackingDto;
import com.pgsintl.supplychaintracking.Services.OrdersIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller   @Slf4j @AllArgsConstructor
public class WebSocketController {

    private OrdersIService ordersIService;

    @MessageMapping("/getPosition/{roomId}")
    @SendTo("/setPosition/{roomId}")
    public OrdersTrackingDto trackingOrders(@DestinationVariable String roomId, OrdersTrackingDto ordersTrackingDto) {


        log.info("ID Order : "+ordersTrackingDto.getIdOrders()+"| ordersNowLat:" +ordersTrackingDto.getOrdersNowLat()+" | ordersNowLong:"+ordersTrackingDto.getOrdersNowLong());

        ordersIService.updatePosition(ordersTrackingDto);

        return  ordersTrackingDto;
    }

}
