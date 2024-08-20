package technikal.task.fishmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import technikal.task.fishmarket.entity.Fish;

public interface FishRepository extends JpaRepository<Fish, Integer> {}
