/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 断言单元测试案例
 *
 * @author Lion Li
 */
@DisplayName("断言单元测试案例")
public class AssertUnitTest {

    @DisplayName("测试 assertEquals 方法")
    @Test
    public void testAssertEquals() {
        Assertions.assertEquals("666", new String("666"));
        Assertions.assertNotEquals("666", new String("666"));
    }

    @DisplayName("测试 assertSame 方法")
    @Test
    public void testAssertSame() {
        Object obj = new Object();
        Object obj1 = obj;
        Assertions.assertSame(obj, obj1);
        Assertions.assertNotSame(obj, obj1);
    }

    @DisplayName("测试 assertTrue 方法")
    @Test
    public void testAssertTrue() {
        Assertions.assertTrue(true);
        Assertions.assertFalse(true);
    }

    @DisplayName("测试 assertNull 方法")
    @Test
    public void testAssertNull() {
        Assertions.assertNull(null);
        Assertions.assertNotNull(null);
    }

}