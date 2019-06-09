package de.fhws.smartdisplay.database;

public class Settings {
    private Long id;
    private boolean eins;
    private int zwei;
    private String drei;

    public Settings(Long id, boolean eins, int zwei, String drei) {
        this.id = id;
        this.eins = eins;
        this.zwei = zwei;
        this.drei = drei;
    }

    public Settings(boolean eins, int zwei, String drei) {
        this.eins = eins;
        this.zwei = zwei;
        this.drei = drei;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEins() {return eins;}

    public void setEins(boolean eins) {this.eins = eins;}

    public int getZwei() {return zwei;}

    public void setZwei(int zwei) {this.zwei = zwei;}

    public String getDrei() {return drei;}

    public void setDrei(String drei) {this.drei = drei;}

    @Override
    public String toString() {
        return super.toString();
    }
}
