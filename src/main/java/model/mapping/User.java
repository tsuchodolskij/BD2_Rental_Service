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
@Table(name="User")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name = "Username", nullable = false)
    private String username;

    @Column(name="Phone_number", nullable=false)
    private String phoneNumber;

    @Column(name="Status", nullable=false)
    private boolean status;

    @Column(name="Password", nullable=false)
    private String password;

    @Column(name="Hire_Status", nullable=false)
    private boolean hireStatus;

    @OneToMany(mappedBy="user")
    List<Hire> hire;
}
