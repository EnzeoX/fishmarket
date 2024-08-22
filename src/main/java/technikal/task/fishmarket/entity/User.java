package technikal.task.fishmarket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import technikal.task.fishmarket.enums.Role;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikolay Boyko
 */

@Data
@Entity
@Builder
@Table(name = "user_table", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("USER AUTHORITY: " + role.name());
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
