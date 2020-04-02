package com.example.atm.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "atms")
@Data
public class Atm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;

    @ElementCollection
    @CollectionTable(name = "atm_cash_mapping",
            joinColumns = {@JoinColumn(name = "atm_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "cash_nominal")
    @Column(name = "cash_sum")
    private Map<Cash, Long> cash;

    {
        cash = new HashMap<>();
        cash.put(Cash.NOTE100, 0L);
        cash.put(Cash.NOTE200, 0L);
        cash.put(Cash.NOTE500, 0L);
    }
}
