package com.sirvja.listcell;

import java.util.OptionalLong;

public interface BudgetCellItem {
    String name();
    String hoursLabel();
    long spentMinutes();
    OptionalLong budgetMinutes();
}

