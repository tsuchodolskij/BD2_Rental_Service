package model.mapping;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Vehicle")
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private long id;

    @Column(name="Occupancy_status", nullable=false)
    private boolean occupancyStatus;

    @Column(name="Vehicle_status", nullable=false)
    private boolean vehicleStatus;

    @Column(name="Tech_evaluation_date", nullable=false)
    private Date techEvaluationDate;

    @OneToMany(mappedBy="vehicle")
    List<Hire> hire;

    @ManyToOne
    @JoinColumn(name="Type_Name", nullable = false)
    private Type type;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Terminal terminal;
}