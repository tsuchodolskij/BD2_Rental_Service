package model.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.tableview.AllBuses;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Bus")
@Entity
public class Bus {

    @Id
    @Column(name="Registration_number", nullable=false)
    private String registrationNumber;

    @Column(name = "Tech_evaluation_date", nullable = false)
    private Date techEvaluationDate;

    @Column(name = "Size_capacity", nullable = false)
    private Long sizeCapacity;

    @Column(name="OC_AC", nullable=false)
    private Long ocAc;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "District_name", nullable = false),
            @JoinColumn(name = "Agency_ID", nullable = false)
    })
    private Agency agency;

    @OneToMany(mappedBy = "bus")
    private List<Driver> drivers;

    public AllBuses toAllBuses(){
        return AllBuses.builder()
                .registration(registrationNumber)
                .capacity(sizeCapacity)
                .evaluationDate(techEvaluationDate)
                .district(agency.getAgencyPk().getDistrict().getName())
                .build();
    }
}