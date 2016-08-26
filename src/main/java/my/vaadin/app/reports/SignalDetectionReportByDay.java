package my.vaadin.app.reports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Daily Resolution Report - signal_detection_report_by_day table
 */
@Entity
public class SignalDetectionReportByDay implements SignalDetectionReport {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "asset_un", nullable = false)
	private String assetUN;

	@Column(name = "num_signals", nullable = false)
	private int numSignals;

	@Column(name = "date", nullable = false)
	private Date date;

	protected SignalDetectionReportByDay() {
	}

	public SignalDetectionReportByDay(String assetUN, int numSignals, Date date) {
		this.assetUN = assetUN;
		this.numSignals = numSignals;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetUN() {
		return assetUN;
	}

	public void setAssetUN(String assetUN) {
		this.assetUN = assetUN;
	}

	public int getNumSignals() {
		return numSignals;
	}

	public void setNumSignals(int numSignals) {
		this.numSignals = numSignals;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SignalDetectionReportByDay{" +
				"id=" + id +
				", assetUN='" + assetUN + '\'' +
				", numSignals=" + numSignals +
				", date=" + date +
				'}';
	}
}
