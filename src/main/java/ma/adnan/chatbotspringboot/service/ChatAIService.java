package ma.adnan.chatbotspringboot.service;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Dell Latitude 5420
 * IFRAH ADNAN
 **/
@BrowserCallable
@AnonymousAllowed

public class ChatAIService {
    private VectorStore vectorStore;
    private ChatClient chatClient;
    @Value("classpath:/prompt/prompt-template.st")

    private Resource promptresource;

    public ChatAIService(ChatClient.Builder builder,VectorStore vectorStore) {

        this.chatClient = builder.build();
        this.vectorStore=vectorStore;
    }
    public String ragChat(String question){
        List<Document> documentList=vectorStore.similaritySearch(question);
        List<String> context=documentList.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate=new PromptTemplate(promptresource);
        Prompt prompt=promptTemplate.create(Map.of("context",context,"question",question));


        return
                chatClient.prompt(prompt).call().content();
    }
}
