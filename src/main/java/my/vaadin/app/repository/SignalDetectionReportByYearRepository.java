package my.vaadin.app.repository;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import my.vaadin.app.reports.SignalDetectionReportByYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the Yearly Resolution Report - signal_detection_report_by_year table
 */
@SpringComponent
@UIScope
public interface SignalDetectionReportByYearRepository extends JpaRepository<SignalDetectionReportByYear, Long> {

	/**
	 * JPA query to find all the signals by asset un ordered by date
	 *
	 * @param assetUN the asset un identifier
	 * @return the signal detection report by year data
     */
	List<SignalDetectionReportByYear> findByAssetUNOrderByDate(String assetUN);

	/**
	 * JPA method name query to find all the signals ordered by asset un and date for the yearly resolution
	 * type selected
	 * @return the list of all signals ordered by asset un and date
	 */
	List<SignalDetectionReportByYear> findByOrderByAssetUNAscDateAsc();
}
