package my.vaadin.app.repository;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import my.vaadin.app.reports.SignalDetectionReportByDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the Daily Resolution Report - signal_detection_report_by_day table
 */
@SpringComponent
@UIScope
public interface SignalDetectionReportByDayRepository extends JpaRepository<SignalDetectionReportByDay, Long> {

	/**
	 * JPA query to find all the signals by asset un ordered by date
	 *
	 * @param assetUN the asset un identifiers
	 * @return the signal detection report by day data
	 */
	List<SignalDetectionReportByDay> findByAssetUNOrderByDate(String assetUN);

	/**
	 * JPA method name query to find all the signals ordered by asset un and date for the daily resolution
	 * type selected
	 * @return the list of all signals ordered by asset un and date
     */
	List<SignalDetectionReportByDay> findByOrderByAssetUNAscDateAsc();
}
