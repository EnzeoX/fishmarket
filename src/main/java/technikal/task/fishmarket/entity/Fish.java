package technikal.task.fishmarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "fish")
public class Fish {

	@Id
	@Column(name = "fish_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private double price;
	private Date catchDate;
//	private String imageFileName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fish", cascade = CascadeType.ALL)
	private List<DataFile> attachedFiles;

}
