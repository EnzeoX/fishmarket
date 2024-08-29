package technikal.task.fishmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.entity.DataFile;

/**
 * @author Nikolay Boyko
 */

public interface DataFilesRepository extends JpaRepository<DataFile, Integer> {

}
