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
@Table(name="Contract")
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Contract_ID", nullable = false)
    private Long contractId;

    @Column(name="Salary", nullable = false)
    private Long salary;

    @Column(name="Work_hours", nullable = false)
    private Long workHours;

    @Column(name="Employment_date", nullable = false)
    private Date employmentDate;

    @Column(name="Expiration_date", nullable = false)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name="Driver_licence_ID", nullable = false)
    private Driver driver;
}