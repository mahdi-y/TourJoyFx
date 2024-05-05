package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import services.ServiceClaims;

import java.sql.SQLException;
import java.util.Map;

public class StatsController {
    @FXML
    private PieChart categoryChart;

    private ServiceClaims ServiceClaims;
    @FXML
    void initialize() {
        ServiceClaims = new ServiceClaims();

        loadCategoryStatistics();
    }

    public void loadCategoryStatistics() {
        try {
            Map<String, Integer> stats = ServiceClaims.getCategoryStatistics();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            stats.forEach((category, count) -> {
                String label = String.format("%s: %d", category, count);
                PieChart.Data data = new PieChart.Data(label, count);
                pieChartData.add(data);
            });
            categoryChart.setData(pieChartData);
            categoryChart.getData().forEach(data -> {
                Tooltip.install(data.getNode(), new Tooltip(data.getName()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

}}
