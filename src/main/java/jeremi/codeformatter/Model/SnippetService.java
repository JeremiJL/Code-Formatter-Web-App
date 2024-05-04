package jeremi.codeformatter.Model;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.springframework.stereotype.Service;
import com.google.googlejavaformat.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class SnippetService {

    private Snippet draft;
    private static final String snippetsDirectory = "src/main/resources/static/snippets/";

    // Serialization
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private FileInputStream fileIn;
    private FileOutputStream fileOut;

    // Formatting
    private static final Formatter formatter = new Formatter();

    public SnippetService() {
    }

    public Snippet getDraft() {
        return draft;
    }

    public void setDraft(Snippet draft) {
        this.draft = draft;
    }

    public boolean store(String id, Expiration expiration) throws IOException {

        // Check if snippet with corresponding id is already stored
        if (isPresent(id))
            return false;

        // Set snippet expiration date
        draft.setExpirationDate(calculateExpirationDate(expiration));

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

    public Snippet retrieve(String id) throws IOException, ClassNotFoundException {

        if (isPresent(id)){

            // Deserialize the code snippet object from file
            fileIn = new FileInputStream(snippetsDirectory + id);
            objectIn = new ObjectInputStream(fileIn);

            Snippet snippet = (Snippet) objectIn.readObject();

            objectIn.close();
            objectOut.close();
            return  snippet;

        } else
            throw new NoSuchElementException();

    }

    public void format(Snippet snippet) throws FormatterException {

        String formattedSource = formatter.formatSource(snippet.getOriginalContent());
        snippet.setFormattedContent(formattedSource);
    }

    public boolean isPresent(String id) throws IOException {
        // Check if there is a snippet with provided id
        return Files.walk(Paths.get(snippetsDirectory)).anyMatch(
                f -> f.getFileName().toString().equals(id)
        );
    }

    private LocalDateTime calculateExpirationDate(Expiration expiration){
        LocalDateTime dateOfExpiration = LocalDateTime.now();
        dateOfExpiration.plusDays(expiration.getNumDays());
        dateOfExpiration.plusHours(expiration.getNumHours());
        dateOfExpiration.plusMinutes(expiration.getNumMinutes());
        dateOfExpiration.plusSeconds(expiration.getNumSeconds());

        return dateOfExpiration;
    }


}
