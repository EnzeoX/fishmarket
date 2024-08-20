package technikal.task.fishmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.entity.DataFiles;

/**
 * @author Nikolay Boyko
 */

public interface FileNamesRepository extends JpaRepository<DataFiles, Integer> {

}
