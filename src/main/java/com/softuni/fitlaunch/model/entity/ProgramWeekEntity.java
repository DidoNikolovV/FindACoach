package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "weeks")
@Getter
@Setter
public class ProgramWeekEntity extends BaseEntity {

    @Column(name = "number", nullable = false)
    private int number;

    @OneToMany(mappedBy = "week", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayWorkoutsEntity> days = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ProgramEntity program;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @ManyToMany
    @JoinTable(
            name = "program_weeks_completed",
            joinColumns = @JoinColumn(name = "week_id"),
            inverseJoinColumns = @JoinColumn(name = "user_Id"))
    private List<UserEntity> usersCompleted = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramWeekEntity that = (ProgramWeekEntity) o;
        return number == that.number && isCompleted == that.isCompleted && Objects.equals(days, that.days) && Objects.equals(program, that.program) && Objects.equals(usersCompleted, that.usersCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, days, program, isCompleted, usersCompleted);
    }
}
