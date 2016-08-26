package my.vaadin.app.repository;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import my.vaadin.app.reports.SignalDetectionReportByMonth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the Monthly Resolution Report - signal_detection_report_by_month table
 */
@SpringComponent
@UIScope
public interface SignalDetectionReportByMonthRepository extends JpaRepository<SignalDetectionReportByMonth, Long> {

	/**
	 * JPA query to find all the signals by asset un ordered by date
	 *
	 * @param assetUN the asset un identifiers
	 * @return the signal detection report by month data
	 */
	List<SignalDetectionReportByMonth> findByAssetUNOrderByDate(String assetUN);

	/**
	 * JPA method name query to find all the signals ordered by asset un and date for the monthly resolution
	 * type selected
	 * @return the list of all signals ordered by asset un and date
	 */
	List<SignalDetectionReportByMonth> findByOrderByAssetUNAscDateAsc();
}
