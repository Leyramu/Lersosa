/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo;

import leyramu.framework.lersosa.common.core.enums.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 带参数单元测试案例.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@DisplayName("带参数单元测试案例")
public class ParamUnitTest {

    public static Stream<String> getParam() {
        List<String> list = new ArrayList<>();
        list.add("t1");
        list.add("t2");
        list.add("t3");
        return list.stream();
    }

    @DisplayName("测试 @ValueSource 注解")
    @ParameterizedTest
    @ValueSource(strings = {"t1", "t2", "t3"})
    public void testValueSource(String str) {
        System.out.println(str);
    }

    @DisplayName("测试 @NullSource 注解")
    @ParameterizedTest
    @NullSource
    public void testNullSource(String str) {
        System.out.println(str);
    }

    @DisplayName("测试 @EnumSource 注解")
    @ParameterizedTest
    @EnumSource(UserType.class)
    public void testEnumSource(UserType type) {
        System.out.println(type.getUserType());
    }

    @DisplayName("测试 @MethodSource 注解")
    @ParameterizedTest
    @MethodSource("getParam")
    public void testMethodSource(String str) {
        System.out.println(str);
    }

    @BeforeEach
    public void testBeforeEach() {
        System.out.println("@BeforeEach ==================");
    }

    @AfterEach
    public void testAfterEach() {
        System.out.println("@AfterEach ==================");
    }


}
