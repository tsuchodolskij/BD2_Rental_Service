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
@Table(name="Address")
@Entity
public class Address {

    @EmbeddedId
    private AddressPk addressPk;

    @OneToOne(mappedBy="address", fetch = FetchType.LAZY)
    Station station;

    @OneToOne(mappedBy="address", fetch = FetchType.LAZY)
    Agency agency;
}
