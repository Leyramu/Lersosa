/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.resource.dubbo;

import leyramu.framework.lersosa.resource.api.RemoteSmsService;
import leyramu.framework.lersosa.resource.api.domain.RemoteSms;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 短信服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteSmsServiceImpl implements RemoteSmsService {

    /**
     * 获取特定供应商类型的 SmsBlend 实例.
     *
     * @return SmsBlend 实例，代表指定供应商类型
     */
    private SmsBlend getSmsBlend() {
        // 可自定义厂商配置获取规则 例如根据租户获取 或 负载均衡多个厂商等
        return SmsFactory.getSmsBlend("config1");
    }

    /**
     * 根据给定的 SmsResponse 对象创建并返回一个 RemoteSms 对象，封装短信发送的响应信息.
     *
     * @param smsResponse 短信发送的响应信息
     * @return 封装了短信发送结果的 RemoteSms 对象
     */
    private RemoteSms getRemoteSms(SmsResponse smsResponse) {
        // 创建一个 RemoteSms 对象，封装响应信息
        RemoteSms sms = new RemoteSms();
        sms.setSuccess(smsResponse.isSuccess());
        sms.setResponse(smsResponse.getData().toString());
        sms.setConfigId(smsResponse.getConfigId());
        return sms;
    }

    /**
     * 同步方法：发送简单文本短信.
     *
     * @param phone   目标手机号
     * @param message 短信内容
     * @return 封装了短信发送结果的 RemoteSms 对象
     */
    @Override
    public RemoteSms sendMessage(String phone, String message) {
        // 调用 getSmsBlend 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSmsBlend().sendMessage(phone, message);
        return getRemoteSms(smsResponse);
    }

    /**
     * 同步方法：发送固定消息模板多模板参数短信.
     *
     * @param phone    目标手机号
     * @param messages 短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @return 封装了短信发送结果的 RemoteSms 对象
     */
    @Override
    public RemoteSms sendMessage(String phone, LinkedHashMap<String, String> messages) {
        SmsResponse smsResponse = getSmsBlend().sendMessage(phone, messages);
        return getRemoteSms(smsResponse);
    }

    /**
     * 同步方法：发送带参数的短信.
     *
     * @param phone      目标手机号
     * @param templateId 短信模板ID
     * @param messages   短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @return 封装了短信发送结果的 RemoteSms 对象
     */
    @Override
    public RemoteSms sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        // 调用 getSmsBlend 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSmsBlend().sendMessage(phone, templateId, messages);
        return getRemoteSms(smsResponse);
    }

    /**
     * 同步方法：群发简单文本短信.
     *
     * @param phones  目标手机号列表
     * @param message 短信内容
     * @return 封装了短信发送结果的 RemoteSms 对象
     */
    @Override
    public RemoteSms messageTexting(List<String> phones, String message) {
        // 调用 getSmsBlend 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSmsBlend().massTexting(phones, message);
        return getRemoteSms(smsResponse);
    }

    /**
     * 同步方法：群发带参数的短信.
     *
     * @param phones     目标手机号列表
     * @param templateId 短信模板ID
     * @param messages   短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @return 封装了短信发送结果的 RemoteSms 对象
     */
    @Override
    public RemoteSms messageTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        // 调用 getSmsBlend 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSmsBlend().massTexting(phones, templateId, messages);
        return getRemoteSms(smsResponse);
    }

    /**
     * 异步方法：发送简单文本短信.
     *
     * @param phone   目标手机号
     * @param message 短信内容
     */
    @Override
    public void sendMessageAsync(String phone, String message) {
        getSmsBlend().sendMessageAsync(phone, message);
    }

    /**
     * 异步方法：发送带参数的短信.
     *
     * @param phone      目标手机号
     * @param templateId 短信模板ID
     * @param messages   短信模板参数，使用 LinkedHashMap 以保持参数顺序
     */
    @Override
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {
        getSmsBlend().sendMessageAsync(phone, templateId, messages);
    }

    /**
     * 延迟发送简单文本短信.
     *
     * @param phone       目标手机号
     * @param message     短信内容
     * @param delayedTime 延迟发送时间（毫秒）
     */
    @Override
    public void delayMessage(String phone, String message, Long delayedTime) {
        getSmsBlend().delayedMessage(phone, message, delayedTime);
    }

    /**
     * 延迟发送带参数的短信.
     *
     * @param phone       目标手机号
     * @param templateId  短信模板ID
     * @param messages    短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param delayedTime 延迟发送时间（毫秒）
     */
    @Override
    public void delayMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        getSmsBlend().delayedMessage(phone, templateId, messages, delayedTime);
    }

    /**
     * 延迟群发简单文本短信.
     *
     * @param phones      目标手机号列表
     * @param message     短信内容
     * @param delayedTime 延迟发送时间（毫秒）
     */
    @Override
    public void delayMessageTexting(List<String> phones, String message, Long delayedTime) {
        getSmsBlend().delayMassTexting(phones, message, delayedTime);
    }

    /**
     * 延迟批量发送带参数的短信.
     *
     * @param phones      目标手机号列表
     * @param templateId  短信模板ID
     * @param messages    短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param delayedTime 延迟发送时间（毫秒）
     */
    @Override
    public void delayMessageTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        getSmsBlend().delayMassTexting(phones, templateId, messages, delayedTime);
    }

    /**
     * 加入黑名单.
     *
     * @param phone 手机号
     */
    @Override
    public void addBlacklist(String phone) {
        getSmsBlend().joinInBlacklist(phone);
    }

    /**
     * 加入黑名单.
     *
     * @param phones 手机号列表
     */
    @Override
    public void addBlacklist(List<String> phones) {
        getSmsBlend().batchJoinBlacklist(phones);
    }

    /**
     * 移除黑名单.
     *
     * @param phone 手机号
     */
    @Override
    public void removeBlacklist(String phone) {
        getSmsBlend().removeFromBlacklist(phone);
    }

    /**
     * 移除黑名单.
     *
     * @param phones 手机号
     */
    @Override
    public void removeBlacklist(List<String> phones) {
        getSmsBlend().batchRemovalFromBlacklist(phones);
    }
}
