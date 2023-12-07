package com.zmark.mytodo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务清单，一个任务清单有多个任务，一个任务只能属于一个任务清单
 *
 * @author ZMark
 * @date 2023/12/7 19:03
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_list")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "group_id")
    private Long taskGroupId = 1L;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "list_id", insertable = false)
    private List<Task> taskList = new ArrayList<>();

    @Column(nullable = false, name = "create_time")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Column(nullable = false, name = "update_time")
    private Timestamp updateTime = new Timestamp(System.currentTimeMillis());
}
