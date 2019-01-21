package model.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Terminal")
@Entity
public class Terminal {

    @EmbeddedId
    private TerminalPk terminalPk;

    @ManyToOne
    @JoinColumn(name="Type_Name", nullable = false)
    private Type type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Vehicle_ID")
    private Vehicle vehicle;


}
