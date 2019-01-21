package model.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.tableview.UserHireHistory;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Hire")
@Entity
public class Hire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private long id;

    @Column(name="Rent_start_date", nullable = false)
    private Timestamp rentStartDate;

    @Column(name="Rent_finish_date")
    private Timestamp rentFinishDate;

    @Column(name="Full_hire_price")
    private Long hirePrice;

    @ManyToOne
    @JoinColumn(name="User_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="Vehicle_ID", nullable = false)
    private Vehicle vehicle;

    public UserHireHistory toUserHireHistory(){
        return UserHireHistory.builder()
                            .startDate(rentStartDate)
                            .finishDate(rentFinishDate)
                            .vehicleType(vehicle.getType().getName())
                            .hirePrice(hirePrice)
                            .build();
    }
}
