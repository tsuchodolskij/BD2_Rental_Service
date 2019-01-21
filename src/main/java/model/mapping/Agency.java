package model.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.tableview.AgenciesInDistrict;
import model.tableview.UserHireHistory;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Agency")
@Entity
public class Agency {

    @EmbeddedId
    private AgencyPk agencyPk;

    @Column(name="Parking_places_capacity", nullable=false)
    private Long parkingPlacesCapacity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "Address_House_number", nullable = false),
            @JoinColumn(name = "Address_Post_code", nullable = false),
            @JoinColumn(name = "Address_Street", nullable = false)

    })
    private Address address;

    @OneToMany(mappedBy = "agency")
    private List<Bus> buses;

    @OneToMany(mappedBy = "agency")
    private List<Driver> drivers;

    public AgenciesInDistrict toAgenciesInDistrict(){
        return AgenciesInDistrict.builder()
                .parkingPlaces(parkingPlacesCapacity)
                .street(address.getAddressPk().getStreet())
                .house(address.getAddressPk().getHouseNumber())
                .code(address.getAddressPk().getPostCode())
                .build();
    }

}