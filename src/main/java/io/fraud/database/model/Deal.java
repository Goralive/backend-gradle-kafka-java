package io.fraud.database.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Deal {

    int id;
    double amount;
    String currency;
    Double rate;
    String baseCurrency;
    String source;
    String target;
}
