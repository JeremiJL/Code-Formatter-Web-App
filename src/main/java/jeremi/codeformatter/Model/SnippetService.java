package jeremi.codeformatter.Model;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SnippetService {

    private SnippetDTO draft;
    private static final String snippetsDirectory = "src/main/resources/static/snippets/";

    // Serialization
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private FileInputStream fileIn;
    private FileOutputStream fileOut;

    // Formatting
    private static final Formatter formatter = new Formatter();

    // Expiration input boundaries
    private final ExpirationDTO minExpiration = new ExpirationDTO(0,0,0,10);
    private final ExpirationDTO maxExpiration = new ExpirationDTO(90,0,0,0);


    // Removal of expired snippets
    ExpiredSnippetsCollector collector = new ExpiredSnippetsCollector();

    public SnippetService() {

        // start the thread responsible for deleting expired snippets
        collector.start();
    }

    public SnippetDTO getDraft() {
        return draft;
    }

    public void setDraft(SnippetDTO draft) {
        this.draft = draft;
    }

    public boolean store(String id, ExpirationDTO expiration) throws IOException {

        // Check if snippet with corresponding id is already stored
        if (isPresent(id))
            return false;

        // Set snippet expiration date
        draft.setExpirationDate(calculateExpirationDate(LocalDateTime.now(), expiration));

        // Store serialized code snippet in file with name as snippet id
        fileOut = new FileOutputStream(snippetsDirectory + id);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(draft);
        objectOut.close();
        fileOut.close();

        // Set draft as null
        draft = null;

        return true;
    }

    public SnippetDTO retrieve(String id) throws IOException, ClassNotFoundException {

        if (isPresent(id)){

            // Deserialize the code snippet object from file
            fileIn = new FileInputStream(snippetsDirectory + id);
            objectIn = new ObjectInputStream(fileIn);

            SnippetDTO snippet = (SnippetDTO) objectIn.readObject();

            objectIn.close();
            fileIn.close();
            return  snippet;

        } else
            throw new NoSuchElementException();

    }

    public void format(SnippetDTO snippet) throws FormatterException {

        String formattedSource = formatter.formatSource(snippet.getOriginalContent());
        snippet.setFormattedContent(formattedSource);
    }

    public boolean isPresent(String id) throws IOException {
        // Check if there is a snippet with provided id
        return Files.walk(Paths.get(snippetsDirectory)).anyMatch(
                f -> f.getFileName().toString().equals(id)
        );
    }

    private LocalDateTime calculateExpirationDate(LocalDateTime startPoint, ExpirationDTO expiration){

        return startPoint.plusDays(expiration.getNumDays()).plusHours(expiration.getNumHours())
                .plusMinutes(expiration.getNumMinutes()).plusSeconds(expiration.getNumSeconds());
    }

    public Boolean validateExpiration(ExpirationDTO expirationDTO){

        LocalDateTime reference = LocalDateTime.of(1,1,1,1,1,1);

        LocalDateTime desiredDateOfExpiration = calculateExpirationDate(reference,expirationDTO);
        LocalDateTime minDateOfExpiration = calculateExpirationDate(reference,minExpiration);
        LocalDateTime maxDateOfExpiration = calculateExpirationDate(reference,maxExpiration);

        if (desiredDateOfExpiration.isBefore(minDateOfExpiration) || desiredDateOfExpiration.isAfter(maxDateOfExpiration))
            return false;

        return true;
    }

    private void deleteExpiredSnippets() throws IOException {

        // Iterate over serialized snippets and remove expired ones
        Map<String, SnippetDTO> snippets = Files.walk(Paths.get(snippetsDirectory)).filter(f -> f.getParent().equals(Paths.get(snippetsDirectory))).
                collect(Collectors.toMap( f -> f.getFileName().toString(), f -> {
                    try {
                        return retrieve(f.getFileName().toString());
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }));

        // Remove
        for (Map.Entry<String, SnippetDTO> entry : snippets.entrySet()){

            if (entry.getValue().getExpirationDate().isBefore(LocalDateTime.now()))
                new File(snippetsDirectory + entry.getKey()).delete();
        }
    }

    class ExpiredSnippetsCollector extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    deleteExpiredSnippets();
                    Thread.sleep(1_000);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
