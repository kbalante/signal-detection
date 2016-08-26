package my.vaadin.app;

import my.vaadin.app.repository.SignalDetectionReportByDayRepository;
import my.vaadin.app.repository.SignalDetectionReportByMonthRepository;
import my.vaadin.app.repository.SignalDetectionReportByYearRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SignalDetectionApplication.class)
@WebAppConfiguration
public class SignalDetectionApplicationTests {

	@Autowired
	private SignalDetectionReportByDayRepository sdReportByDayRepo;

	@Autowired
	private SignalDetectionReportByMonthRepository sdReportByMonthRepo;

	@Autowired
	private SignalDetectionReportByYearRepository sdReportByYearRepo;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testDailyReportFindAllOrderByAssetUNDate() {
		Assert.notEmpty(sdReportByDayRepo.findByOrderByAssetUNAscDateAsc());
	}

	@Test
	public void testDailyReportFindAssetUNOrderByDate() {
		// 3106 is an asset un in the csv file
		Assert.notEmpty(sdReportByDayRepo.findByAssetUNOrderByDate("3106"));
	}

	@Test
	public void testMonthlyReportFindAllOrderByAssetUNDate() {
		Assert.notEmpty(sdReportByMonthRepo.findByOrderByAssetUNAscDateAsc());
	}

	@Test
	public void testMonthlyReportFindAssetUNOrderByDate() {
		// 3106 is an asset un in the csv file
		Assert.notEmpty(sdReportByMonthRepo.findByAssetUNOrderByDate("3106"));
	}

	@Test
	public void testYearlyReportFindAllOrderByAssetUNDate() {
		Assert.notEmpty(sdReportByYearRepo.findByOrderByAssetUNAscDateAsc());
	}

	@Test
	public void testYearlyReportFindAssetUNOrderByDate() {
		// 3106 is an asset un in the csv file
		Assert.notEmpty(sdReportByYearRepo.findByAssetUNOrderByDate("3106"));
	}
}
