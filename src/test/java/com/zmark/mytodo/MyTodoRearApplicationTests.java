package com.zmark.mytodo;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.service.TagService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class MyTodoRearApplicationTests {
	@Autowired
	private TagService tagService;
	@Autowired
	private TagDAO tagDAO;

	@Test
	void contextLoads() {
	}

	@Test
	void testCreateNewTagNormal() {
		tagService.createNewTag("tag1/tag2/tag3");
		tagService.createNewTag("tag1/tag2/tag4");
		tagService.createNewTag("tag1/tag2/tag5");
	}

	@Test
	void testCreateNewTagException() {
		try {
			tagService.createNewTag("tag1/tag2/tag3");
			log.info("tag1/tag2/tag3 create succeed");
			tagService.createNewTag("tag1/tag3/tag4");
			log.error("tag1/tag3/tag4 created but it shouldn't!!!");
		} catch (Exception e) {
			log.warn("创建tag失败：", e);
		}
	}

	@Test
	void testTagToDTO() {
		tagService.createNewTag("tag1/tag2/tag3");
		tagService.createNewTag("tag1/tag2/tag4");
		tagService.createNewTag("tag1/tag2/tag5");
		tagDAO.findAll().forEach(tag -> log.info(tag.toTagDTO()));
	}

}
