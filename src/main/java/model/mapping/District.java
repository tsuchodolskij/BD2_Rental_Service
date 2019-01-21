package model.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="District")
@Entity
public class District {

    @Id
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Number_of_stations", nullable = false)
    private Long numberOfStations;

    @Column(name = "Number_of_agencies", nullable = false)
    private Long numberOfAgencies;

    @Column(name = "Number_of_drivers", nullable = false)
    private Long numberOfDrivers;

    @OneToMany(mappedBy="stationPk.district")
    List<Station> stations;
}