package com.sirvja;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BudgetProgressCell<T extends BudgetCellItem> extends ListCell<T> {

    private final VBox container;
    private final Label nameLabel;
    private final Label hoursLabel;
    private final Label percentLabel;
    private final ProgressBar progressBar;
    private final ArrayList<T> allItems;
    private final ColorProvider colorProvider;

    @FunctionalInterface
    public interface ColorProvider {
        String colorFor(double progress);
    }

    public BudgetProgressCell(List<T> allItems) {
        this(allItems, BudgetProgressCell::defaultColor);
    }

    public BudgetProgressCell(List<T> allItems, ColorProvider colorProvider) {
        this.allItems = new ArrayList<>(allItems);
        this.colorProvider = colorProvider;

        nameLabel = new Label();
        nameLabel.setStyle("-fx-font-weight: bold;");

        hoursLabel = new Label();
        hoursLabel.setStyle("-fx-font-size: 10;");

        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(18);

        percentLabel = new Label();

        StackPane progressStack = new StackPane(progressBar, percentLabel);
        container = new VBox(2, nameLabel, progressStack, hoursLabel);
        container.setStyle("-fx-padding: 4 6 4 6;");
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        sortItems();
        if (empty || item == null || item.budgetMinutes().isEmpty()) {
            setGraphic(null);
        } else {
            double progress = (double) item.spentMinutes() / item.budgetMinutes().getAsLong();

            nameLabel.setText(item.name());
            hoursLabel.setText(item.hoursLabel());
            progressBar.setProgress(Math.min(progress, 1.0)); // Cap progressbar to 100%

            int percentage = (int) Math.round(progress * 100); // Round to nearest whole number
            percentLabel.setText(percentage + " %");
            percentLabel.setStyle("-fx-font-size: 10; -fx-font-weight: bold; -fx-text-fill: #FCFCFC;");
            progressBar.setStyle("-fx-accent: " + colorProvider.colorFor(progress) + "; -fx-control-inner-background: #2B2B2B;");

            setGraphic(container);
        }
    }

    private static String defaultColor(double progress) {
        double clamped = Math.min(progress, 1.0);
        int r, g;
        if (clamped <= 0.5) {
            r = (int) (clamped * 2 * 220);
            g = 200;
        } else {
            r = 220;
            g = (int) ((1.0 - clamped) * 2 * 200);
        }
        return String.format("#%02x%02x00", r, g);
    }

    private void sortItems() {
        allItems.sort(Comparator.comparingLong(BudgetCellItem::spentMinutes).reversed());
    }
}
