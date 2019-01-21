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
@Table(name="Type")
@Entity
public class Type {

    @Id
    @Column(name="Name", nullable=false)
    private String name;

    @Column(name="Price", nullable=false)
    private Long price;

    @Column(name="Size", nullable=false)
    private Long size;

    @OneToMany(mappedBy="type")
    List<Vehicle> vehicles;

    @OneToMany(mappedBy="type")
    List<Terminal> terminal;



}