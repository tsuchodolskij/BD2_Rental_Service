package model.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AddressPk implements Serializable {

    @Column(name = "Street")
    private String street;

    @Column(name = "House_number")
    private String houseNumber;

    @Column(name = "Post_code")
    private String postCode;
}
