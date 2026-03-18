package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentTag;
import com.cmcu.itstudy.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Query("""
            select t
            from Tag t
            join DocumentTag dt on dt.tagId = t.id
            group by t
            order by count(dt) desc, t.name asc
            """)
    List<Tag> findTop15PopularTags(Pageable pageable);
}
