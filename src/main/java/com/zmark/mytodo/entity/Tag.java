package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.utils.TimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

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
    private Long id;
    
    @Column(nullable = false, name = "tag_name")
    private String tagName;

    @OneToOne
    @JoinColumn(name = "parent_tag_id", insertable = false, updatable = false)
    @Builder.Default
    private Tag parentTag = null;

    @Column(name = "parent_tag_id")
    @Builder.Default
    private Long parentTagId = null;

    @Column(name = "create_time")
    @Builder.Default
    private Timestamp createTime = TimeUtils.now();

    @Column(name = "update_time")
    @Builder.Default
    private Timestamp updateTime = TimeUtils.now();

    public static List<Tag> from(List<String> tagNames) {
        return tagNames.stream().map(tagName -> Tag.builder().tagName(tagName).build()).toList();
    }

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
}
