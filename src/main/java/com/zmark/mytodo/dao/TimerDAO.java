package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.Timer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Violette
 * @date 2024/5/4 15:17
 */
public interface TimerDAO extends JpaRepository<Timer, Long> {
    Timer findTimerById(Long id);
    List<Timer> findByEndTimeIsNull();
}
