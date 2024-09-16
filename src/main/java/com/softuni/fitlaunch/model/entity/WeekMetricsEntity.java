package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="week_metrics")
@Getter
@Setter
public class WeekMetricsEntity extends BaseEntity{

    @Column(name = "number", nullable = false)
    private int number;

    @OneToMany(mappedBy = "week", cascade = CascadeType.ALL)
    private List<DailyMetricsEntity> dailyMetrics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name ="client_id")
    private ClientEntity client;
}
