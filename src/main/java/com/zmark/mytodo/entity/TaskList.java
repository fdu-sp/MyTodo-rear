package com.zmark.mytodo.entity;

import com.zmark.mytodo.dto.list.TaskListSimpleDTO;
import com.zmark.mytodo.utils.TimeUtils;
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
    public final static Long DEFAULT_LIST_ID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    @Builder.Default
    private String description = "";

    @Column(nullable = false, name = "group_id")
    @Builder.Default
    private Long groupId = TaskGroup.DEFAULT_GROUP_ID;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "list_id", insertable = false)
    @Builder.Default
    private List<Task> taskList = new ArrayList<>();

    @Column(nullable = false, name = "create_time")
    @Builder.Default
    private Timestamp createTime = TimeUtils.now();

    @Column(nullable = false, name = "update_time")
    @Builder.Default
    private Timestamp updateTime = TimeUtils.now();

    public TaskListSimpleDTO toSimpleDTO() {
        return TaskListSimpleDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .count((long) this.getTaskList().size())
                .description(this.getDescription())
                .groupId(this.getGroupId())
                .createTime(this.getCreateTime())
                .updateTime(this.getUpdateTime())
                .build();
    }
}
