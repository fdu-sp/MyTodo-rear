package com.zmark.mytodo.entity;

import com.zmark.mytodo.dto.group.TaskGroupSimpDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务组，一个任务组有多个任务清单，一个任务清单只能属于一个任务组
 *
 * @author ZMark
 * @date 2023/12/7 19:00
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_group")
public class TaskGroup {
    public final static Long DEFAULT_GROUP_ID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "group_id")
    private List<TaskList> taskLists = new ArrayList<>();

    @Column(nullable = false, name = "create_time")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Column(nullable = false, name = "update_time")
    private Timestamp updateTime = new Timestamp(System.currentTimeMillis());

    public TaskGroupSimpDTO toSimpleDTO() {
        return TaskGroupSimpDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .count((long) this.getTaskLists().size())
                .createTime(this.getCreateTime())
                .updateTime(this.getUpdateTime())
                .build();
    }
}
