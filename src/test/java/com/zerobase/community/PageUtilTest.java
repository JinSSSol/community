package com.zerobase.community;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zerobase.community.util.PageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PageUtilTest {

	@Test
	void pagerTest() {
		// given
		PageUtil pageUtil = new PageUtil(151, 10, 3, "");

		// when
		String htmlPager = pageUtil.pager();

		// then
		assertTrue(htmlPager.split("/").length == 15);
	}


}
