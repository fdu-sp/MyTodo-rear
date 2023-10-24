package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagDAO extends JpaRepository<Tag, Integer> {
}
