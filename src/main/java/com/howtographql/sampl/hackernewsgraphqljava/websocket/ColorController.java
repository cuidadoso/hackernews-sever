package com.howtographql.sampl.hackernewsgraphqljava.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;

@RequiredArgsConstructor
@Controller
public class ColorController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/color")
    public void receiveColor(ColorMessage message){
        logInfo("message.getColorString() = " + message.getColorString());
    }


    /*@Scheduled(fixedDelay = 1000)
    private void bgColor(){
        Random r = new Random();
        Color rgb = Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextDouble());
        String m = rgb.toString();
        String color = m.replace("0x", "#");
        simpMessagingTemplate.convertAndSend("/topic/color", new ColorMessage(color));
        logInfo("Send color: " + color);
    }*/
}
