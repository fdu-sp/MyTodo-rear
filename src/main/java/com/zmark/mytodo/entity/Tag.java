package com.zmark.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zmark.mytodo.dto.tag.TagDTO;
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
    private Tag ParentTag;
    @Column(name = "parent_tag_id")
    private Long parentTagId;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "update_time")
    private Timestamp updateTime;

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

    public TagDTO toTagDTO() {
        return TagDTO.builder()
                .tagName(tagName)
                .tagPath(getTagPath())
                .build();
    }

    public TagDTO toTagDTO(List<Tag> childrenTagList) {
        return TagDTO.builder()
                .tagName(tagName)
                .tagPath(getTagPath())
                .children(childrenTagList.stream().map(Tag::toTagDTO).toList())
                .build();
    }

}
