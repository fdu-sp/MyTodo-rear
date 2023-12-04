package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.StepInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepInfoDAO extends JpaRepository<StepInfo, Long> {
}
