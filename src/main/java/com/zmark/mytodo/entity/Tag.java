package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.dto.TagDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tag")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false, name = "tag_name")
    private String tagName;
    @OneToOne
    @JoinColumn(name = "parent_tag_id", insertable = false, updatable = false)
    private Tag ParentTag;
    @Column(name = "parent_tag_id")
    private Integer parentTagId;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "update_time")
    private Timestamp updateTime;

    public String getTagPath() {
        // 从当前tag开始，依次向上查找父tag，直到根tag
        StringBuilder tagPath = new StringBuilder();
        Tag currentTag = this;
        while (currentTag != null) {
            tagPath.insert(0, currentTag.getTagName());
            currentTag = currentTag.getParentTag();
            if (currentTag != null) {
                tagPath.insert(0, "/");
            }
        }
        return tagPath.toString();
    }

    public TagDTO toTagDTO() {
        return TagDTO.builder()
                .tagName(tagName)
                .tagPath(getTagPath())
                .build();
    }
}
