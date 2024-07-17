package ma.adnan.chatbotspringboot.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import  org.springframework.ai.reader.pdf.PagePdfDocumentReader;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
public class RagDataLoader {
    @Value("classpath:/pdfs/cv.pdf")
    private Resource pdfResource;
    @Value("store-data-v1.json")
    private String storeFile;

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        String fileStore = System.getProperty("user.home") + File.separator + "chatbot_store" + File.separator + storeFile;
        File file = new File(fileStore);
        System.out.println("Chemin du fichier de stockage : " + file.getAbsolutePath());

        try {
            if (!file.exists()) {
                System.out.println("Le fichier n'existe pas, création en cours...");
                file.getParentFile().mkdirs();

                PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(pdfResource);
                List<Document> documents = pagePdfDocumentReader.get();
                System.out.println("Nombre de documents lus : " + documents.size());

                TextSplitter textSplitter = new TokenTextSplitter();
                List<Document> chunks = textSplitter.split(documents);
                System.out.println("Nombre de chunks créés : " + chunks.size());

                System.out.println("Début de l'embedding des chunks...");
                vectorStore.accept(chunks);
                System.out.println("Fin de l'embedding des chunks");

                vectorStore.save(file);
                System.out.println("Fichier de stockage sauvegardé : " + file.exists());
                if (file.exists()) {
                    System.out.println("Taille du fichier : " + file.length() + " bytes");
                } else {
                    System.out.println("Échec de la sauvegarde du fichier");
                }
            } else {
                System.out.println("Chargement du fichier de stockage existant...");
                vectorStore.load(file);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement : " + e.getMessage());
            e.printStackTrace();
        }
        return vectorStore;
    }
}