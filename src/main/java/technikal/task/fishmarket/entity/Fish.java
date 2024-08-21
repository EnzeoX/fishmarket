package technikal.task.fishmarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fish")
public class Fish {

	@Id
	@Column(name = "fish_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;

	private double price;

	@Column(name = "catch_date")
	private Date catchDate;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "fish")
	private List<DataFile> attachedFiles = new ArrayList<>();

	public void addFile(DataFile file) {
		file.setFish(this);
		this.attachedFiles.add(file);
	}

}
