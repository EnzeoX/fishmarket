package technikal.task.fishmarket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import technikal.task.fishmarket.enums.Role;

/**
 * @author Nikolay Boyko
 */

@Data
@Entity
@Builder
@Table(name = "user_table", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
}
