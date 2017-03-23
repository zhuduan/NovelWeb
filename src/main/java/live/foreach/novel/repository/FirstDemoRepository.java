package live.foreach.novel.repository;

import live.foreach.novel.model.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FirstDemoRepository extends JpaRepository<Item, String> {
    @Query("SELECT b FROM Item b WHERE b.name like %?1%")
    Iterable<Item> findByTitle(String name);
}
