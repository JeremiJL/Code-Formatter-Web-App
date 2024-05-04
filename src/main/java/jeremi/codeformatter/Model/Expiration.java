package jeremi.codeformatter.Model;

public class Expiration {

    private int numDays;
    private int numHours;
    private int numMinutes;
    private int numSeconds;

    public Expiration() {
        numDays = 0;
        numHours = 0;
        numMinutes = 0;
        numSeconds = 0;
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
