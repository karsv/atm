package com.example.atm.model;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "atms")
@Data
public class Atm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "atm_cash_mapping",
            joinColumns = {@JoinColumn(name = "atm_id", referencedColumnName = "id")})
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "cash_sum")
    private Map<Cash, Long> cash;

    {
        cash = new LinkedHashMap<>();
        cash.put(Cash.NOTE500, 0L);
        cash.put(Cash.NOTE200, 0L);
        cash.put(Cash.NOTE100, 0L);
    }
}
