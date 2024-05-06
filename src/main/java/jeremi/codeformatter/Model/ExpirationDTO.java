package jeremi.codeformatter.Model;

public class ExpirationDTO {

    private int numDays;
    private int numHours;
    private int numMinutes;
    private int numSeconds;

    public ExpirationDTO() {
    }

    public ExpirationDTO(int numDays, int numHours, int numMinutes, int numSeconds) {
        this.numDays = numDays;
        this.numHours = numHours;
        this.numMinutes = numMinutes;
        this.numSeconds = numSeconds;
    }

    public int getNumDays() {
        return numDays;
    }

    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }

    public int getNumHours() {
        return numHours;
    }

    public void setNumHours(int numHours) {
        this.numHours = numHours;
    }

    public int getNumMinutes() {
        return numMinutes;
    }

    public void setNumMinutes(int numMinutes) {
        this.numMinutes = numMinutes;
    }

    public int getNumSeconds() {
        return numSeconds;
    }

    public void setNumSeconds(int numSeconds) {
        this.numSeconds = numSeconds;
    }
}
