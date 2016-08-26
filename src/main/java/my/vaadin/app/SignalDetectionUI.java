package my.vaadin.app;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import my.vaadin.app.reports.SignalDetectionReport;
import my.vaadin.app.reports.SignalDetectionReportByDay;
import my.vaadin.app.reports.SignalDetectionReportByMonth;
import my.vaadin.app.reports.SignalDetectionReportByYear;
import my.vaadin.app.repository.SignalDetectionReportByDayRepository;
import my.vaadin.app.repository.SignalDetectionReportByMonthRepository;
import my.vaadin.app.repository.SignalDetectionReportByYearRepository;
import my.vaadin.app.util.Constants;
import my.vaadin.app.util.ResolutionType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static my.vaadin.app.util.ResolutionType.Monthly;
import static my.vaadin.app.util.ResolutionType.Yearly;

/**
 * Vaadin UI for our Signal Detection Chart application
 */
@SpringUI
@Theme("mytheme")
@Widgetset("my.vaadin.app.MyAppWidgetset")
public class SignalDetectionUI extends UI {

    /**
     * Daily report repository
     */
    @Autowired
    private SignalDetectionReportByDayRepository sdReportByDayRepo;

    /**
     * Monthly report repository
     */
    @Autowired
    private SignalDetectionReportByMonthRepository sdReportByMonthRepo;

    /**
     * Yearly report repository
     */
    @Autowired
    private SignalDetectionReportByYearRepository sdReportByYearRepo;

    /**
     * num signals chart by asset un identifier and resolution
     */
    private Chart chart;

    /**
     * asset un combo box that is used to populate the line chart
     */
    private ComboBox assetUNComboBox;

    /**
     * lists out resolution options to choose from
     */
    private ListSelect resolutionSelectList;

    /**
     * Selected Asset UN from the combo box
     */
    private String selectedAssetUN;

    /**
     * Selected resolution option
     */
    private ResolutionType selectedResolution = ResolutionType.Daily;

    /**
     * Called when the Vaadin application is viewed on the browser and initializes the combo boxes in the top bar and
     * the chart.
     * @param vaadinRequest vaadin request
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // add view port for responsive but not loading early enough so not displaying on first load
        // issues with spring boot and not having a web.xml file for configuration
        //getSession().addBootstrapListener(new ResponsiveBootstrapListener());

        // create a vertical layout that will contain everything and set the content to this layout
        VerticalLayout mainLayout = new VerticalLayout();
        setContent(mainLayout);

        //mainLayout.addStyleName("myresponsive");
        //Responsive.makeResponsive(mainLayout);

        // create a top bar that will contain the asset un combobox and the resolution chooser
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setSpacing(true);
        topBar.setMargin(new MarginInfo(false, true, false, true));
        topBar.setResponsive(true);

        // create an asset un combo box and add it to the top bar
        createAssetUNComboBox();
        topBar.addComponent(assetUNComboBox);

        // create an resolution list box and add it to the top bar
        createResolutionListBox();
        topBar.addComponent(resolutionSelectList);

        // add the top bar to the main layout
        mainLayout.addComponent(topBar);

        // create the chart and add it to the main layout below the top bar
        createChart();
        mainLayout.addComponent(chart);
    }

    /**
     * Initialize the chart with layout and configuration details
     */
    private void createChart() {
        // create a line chart
        chart = new Chart(ChartType.LINE);

        // make this chart responsive
        chart.setWidth("100%");
        chart.setHeight("450px");

        // set the titles for each axis and ensure the xaxis is of type DATETIME
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Number of Signals Over Time");
        conf.getxAxis().setTitle("Time");
        conf.getxAxis().setType(AxisType.DATETIME);
        conf.getyAxis().setTitle("# of Signals");
    }

    /**
     * Update the chart using the asset un identifier and resolution only if there is an asset
     * un selected. There are three database tables each mapped to the resolution type.
     *
     * Daily Resolution Report - signal_detection_report_by_day
     * Monthly Resolution Report - signal_detection_report_by_month
     * Yearly Resolution Report - signal_detection_report_by_year
     */
    private void updateChart() {
        // draw the chart only if there is an asset un selected
        // resolution will default to daily if it hasn't been changed
        if (selectedAssetUN != null) {
            // get the signals by asset un and selected resolution
            // note ALL will display all asset uns
            switch (selectedResolution) {
                case Monthly:
                    // monthly resolution  - add date and num signals to data series
                    if (!selectedAssetUN.equals(Constants.ALL)) {
                        // find by asset un
                        List<SignalDetectionReportByMonth> monthSignals = sdReportByMonthRepo.
                                findByAssetUNOrderByDate(selectedAssetUN);
                        singleAssetPlot(monthSignals);
                    } else {
                        // find all
                        List<SignalDetectionReportByMonth> monthSignals = sdReportByMonthRepo.
                                findByOrderByAssetUNAscDateAsc();
                        allAssetsPlot(monthSignals);
                    }
                    break;
                case Yearly:
                    // yearly resolution - add date and num signals to data series
                    if (!selectedAssetUN.equals(Constants.ALL)) {
                        // find by asset un
                        List<SignalDetectionReportByYear> yearSignals = sdReportByYearRepo.
                                findByAssetUNOrderByDate(selectedAssetUN);
                        singleAssetPlot(yearSignals);
                    } else {
                        // find all
                        List<SignalDetectionReportByYear> yearSignals = sdReportByYearRepo.
                                findByOrderByAssetUNAscDateAsc();
                        allAssetsPlot(yearSignals);
                    }
                    break;
                default:
                    // use the daily resolution by default - add date and num signals to data series
                    if (!selectedAssetUN.equals(Constants.ALL)) {
                        // find by asset un
                        List<SignalDetectionReportByDay> dailySignals = sdReportByDayRepo.
                                findByAssetUNOrderByDate(selectedAssetUN);
                        singleAssetPlot(dailySignals);
                    } else {
                        // find all
                        List<SignalDetectionReportByDay> dailySignals = sdReportByDayRepo.
                                findByOrderByAssetUNAscDateAsc();
                        allAssetsPlot(dailySignals);
                    }
                    break;
            }

            // redraw the chart
            chart.drawChart();
        }
    }

    /**
     * Add the signal data found to a data series to be displayed on the chart. This will plot one asset un.
     * @param signalsDetected list of signals detected that contains the chart plot points
     */
    private void singleAssetPlot(List signalsDetected) {
        // create a new data series
        DataSeries dataSeries = new DataSeries(selectedAssetUN);

        // iterate over the list of signals detected and add them to the data series
        Iterator iter = signalsDetected.iterator();
        while (iter.hasNext()) {
            SignalDetectionReport sdr = (SignalDetectionReport) iter.next();
            dataSeries.add(new DataSeriesItem(sdr.getDate(), sdr.getNumSignals()));
        }

        // clear and add data series
        Configuration conf = chart.getConfiguration();
        conf.setSeries(dataSeries);
    }

    /**
     * Get all the signals for all the asset uns associated to the resolution type selected.
     * This will plot all assets.
     *
     * @param signalsDetected list of all signals detected for the selected resolution type
     */
    private void allAssetsPlot(List signalsDetected) {
        // sort the signals into a map with the key being the asset un
        // and the value being the DataSeries
        // at the completion of the iteration the map will be fully populated
        TreeMap<String,DataSeries> allSignals = new TreeMap<>();
        Iterator iter = signalsDetected.iterator();
        while (iter.hasNext()) {
            // get asset un and date from report
            SignalDetectionReport sdr = (SignalDetectionReport) iter.next();
            String assetUN = sdr.getAssetUN();
            Date date = sdr.getDate();
            int numSignals = sdr.getNumSignals();

            // if key exists get data series and add plot point to the series
            if (allSignals.containsKey(assetUN)) {
                DataSeries dataSeries = allSignals.get(assetUN);
                dataSeries.add(new DataSeriesItem(date, numSignals));
            } else {
                // key does not exist so create a new data series associated to the asset un
                // and add the first item to the series
                DataSeries dataSeries = new DataSeries(assetUN);
                dataSeries.add(new DataSeriesItem(date, numSignals));
                allSignals.put(assetUN, dataSeries);
            }
        }

        // create a list of all the data series and update the chart with info
        ArrayList list =  new ArrayList(allSignals.values());
        Configuration conf = chart.getConfiguration();
        conf.setSeries(list);
    }

    /**
     * Create and populate the asset un combo box will all the distinct asset uns from the db.
     * This will be placed within the top bar.
     */
    private void createAssetUNComboBox() {
        assetUNComboBox = new ComboBox("Asset UN");
        assetUNComboBox.setFilteringMode(FilteringMode.CONTAINS);

        // get all unique asset un's from the db
        TreeMap<String,String> uniqueAssetUNs = new TreeMap<>();
        for (SignalDetectionReportByYear sd : sdReportByYearRepo.findAll()) {
            String assetUN = sd.getAssetUN();
            uniqueAssetUNs.put(assetUN, assetUN);
        }

        // the first option is the list all option for all of the asset uns
        assetUNComboBox.addItem("ALL");
        //assetUNComboBox.setDescription("Display chart by Asset UN or Select ALL to display all Asset UNs");

        // add found asset uns to the combobox so that the user can choose one of the options
        for (String assetUN : uniqueAssetUNs.keySet()) {
            assetUNComboBox.addItem(assetUN);
        }

        // setup a change listener to update the chart and plotting points using a new asset un identifier
        assetUNComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                // set the selected asset un and update the chart
                selectedAssetUN = (String) event.getProperty().getValue();
                updateChart();
            }
        });
    }

    /**
     * Create the resolution list box to be placed within the top bar. The default is daily and a change
     * to the resolution will update the chart
     */
    private void createResolutionListBox() {
        // add the resolution options
        resolutionSelectList = new ListSelect("Resolution Type");
        resolutionSelectList.addItems(
                ResolutionType.Daily.name(),
                Monthly.name(),
                Yearly.name());

        // set the default selection to Daily
        resolutionSelectList.setValue(ResolutionType.Daily.name());
        resolutionSelectList.setNullSelectionAllowed(false);
        resolutionSelectList.setRows(1);

        // add value change listener i.e. set the resolution and update the chart
        resolutionSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectedResolution = ResolutionType.valueOf((String) event.getProperty().getValue());
                // set the selected resolution and update the chart
                updateChart();
            }
        });
    }
}