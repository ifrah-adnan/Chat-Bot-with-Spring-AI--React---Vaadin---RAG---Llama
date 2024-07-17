package ma.adnan.chatbotspringboot.web;

import ma.adnan.chatbotspringboot.service.ChatAIService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

/**
 * @author Dell Latitude 5420
 * IFRAH ADNAN
 **/
@RestController
@RequestMapping("/chat")
public class ChatRestController {
private ChatAIService chatAIService;

    public ChatRestController(ChatAIService chatAIService) {
        this.chatAIService = chatAIService;
    }
    @GetMapping(value = "/ask",produces= MediaType.TEXT_PLAIN_VALUE)
    public String ask(String question){
        return chatAIService.ragChat(question);
    }
    @GetMapping("/hello")
    public String tst(){
        return "hello adnan";
    }
}
