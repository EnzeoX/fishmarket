package technikal.task.fishmarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Nikolay Boyko
 */

@Entity
@Getter
@Setter
@Table(name = "data_file")
@NoArgsConstructor
public class DataFile {

    @Id
    @Column(name = "data_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "save_date")
    private Date saveDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fish_id")
    private Fish fish;
}
