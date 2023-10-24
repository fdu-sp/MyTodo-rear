package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.MyDayTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyDayTaskDAO extends JpaRepository<MyDayTask, Integer> {
}
