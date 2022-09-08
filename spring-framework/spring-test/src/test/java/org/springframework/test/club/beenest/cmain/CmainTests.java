package org.springframework.test.club.beenest.cmain;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class CmainTests {

	@Test
	public void testMain() {
		DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
		System.out.println("测试哦");
	}
}
