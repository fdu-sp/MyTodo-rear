package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagDAO extends JpaRepository<Tag, Integer> {

    Tag findByTagName(String tagName);

    List<Tag> findTagsByParentTagId(Integer parentTagId);
}
