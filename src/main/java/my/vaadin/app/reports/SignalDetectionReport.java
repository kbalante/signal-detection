package my.vaadin.app.reports;

import java.util.Date;

/**
 * Interface to the reports that the different types of reports
 */
public interface SignalDetectionReport {

    /**
     * Gete the number of signals based on the date and the asset un
     * @return the number of signals based on the date and the asset un
     */
    public int getNumSignals();

    /**
     * Entry date
     * @return the entry datee
     */
    public Date getDate();

    /**
     * The asset un identifier for the field equipment
     * @return the asset un identifier
     */
    public String getAssetUN();
}
