package model.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.tableview.AllDrivers;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Driver")
@Entity
public class Driver {

    @Id
    @Column(name="Drivers_licence_ID", nullable=false)
    private String driverLicenceId;

    @Column(name="Family_name", nullable = false)
    private String familyName;

    @Column(name="Work_status", nullable = false)
    private boolean workStatus;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "District_name", nullable = false),
            @JoinColumn(name = "Agency_ID", nullable = false)
    })
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "Bus_Registration_number", nullable = false)
    private Bus bus;

    @OneToMany(mappedBy = "driver")
    private List<Contract> contracts;

    public AllDrivers toAllDrivers(){
        return AllDrivers.builder()
                .driversLicenseID(driverLicenceId)
                .familyName(familyName)
                .busRegistrationNumber(bus.getRegistrationNumber())
                .districtName(agency.getAgencyPk().getDistrict().getName())
                .build();
    }
}