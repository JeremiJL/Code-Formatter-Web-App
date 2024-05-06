package jeremi.codeformatter.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SnippetDTO implements Serializable {

    private String originalContent;
    private String formattedContent;
    private LocalDateTime expirationDate;

    public SnippetDTO() {
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getFormattedContent() {
        return formattedContent;
    }

    public void setFormattedContent(String formattedContent) {
        this.formattedContent = formattedContent;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
