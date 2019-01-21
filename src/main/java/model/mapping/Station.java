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
@Table(name="Station")
@Entity
public class Station {

    @EmbeddedId
    private StationPk stationPk;

    @Column(name="Place_quantity", nullable=false)
    private Long placeQuantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "Address_House_number", nullable = false),
            @JoinColumn(name = "Address_Post_code", nullable = false),
            @JoinColumn(name = "Address_Street", nullable = false)

    })
    private Address address;

    @OneToMany(mappedBy="terminalPk.station")
    List<Terminal> terminals;
}
