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

package leyramu.framework.lersosa.visual.monitor.service;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import leyramu.framework.lersosa.visual.monitor.config.properties.CustomNacosDiscoveryProperties;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Nacos 服务发现
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/12/1
 */
public class NacosServiceDiscoveryV2 extends NacosServiceDiscovery {

    /**
     * Nacos 服务管理器
     */
    private final NacosServiceManager nacosServiceManager;

    /**
     * Nacos 发现属性
     */
    private final NacosDiscoveryProperties discoveryProperties;

    /**
     * 自定义服务发现属性
     */
    private final CustomNacosDiscoveryProperties customNacosDiscoveryProperties;

    /**
     * 构造函数
     *
     * @param nacosServiceManager            Nacos 服务管理器
     * @param discoveryProperties            Nacos 发现属性
     * @param customNacosDiscoveryProperties 自定义 Nacos 服务发现属性
     */
    public NacosServiceDiscoveryV2(
        NacosServiceManager nacosServiceManager,
        NacosDiscoveryProperties discoveryProperties,
        CustomNacosDiscoveryProperties customNacosDiscoveryProperties) {

        super(discoveryProperties, nacosServiceManager);
        this.nacosServiceManager = nacosServiceManager;
        this.discoveryProperties = discoveryProperties;
        this.customNacosDiscoveryProperties = customNacosDiscoveryProperties;
    }

    /**
     * 获取服务实例列表
     *
     * @param serviceId 服务ID
     * @return 服务实例列表
     * @throws NacosException Nacos异常
     */
    @Override
    public List<ServiceInstance> getInstances(String serviceId) throws NacosException {
        String group = this.discoveryProperties.getGroup();
        List<Instance> instances = this.namingService().selectInstances(serviceId, group, true);
        if (instances.isEmpty()) {
            for (String g : customNacosDiscoveryProperties.getExtensionGroups()) {
                List<Instance> instance = this.namingService().selectInstances(serviceId, g, true);
                if (!instance.isEmpty()) {
                    instances.addAll(instance);
                }
            }
        }
        return hostToServiceInstanceList(instances, serviceId);
    }

    /**
     * 获取所有服务
     *
     * @return 所有服务
     * @throws NacosException Nacos异常
     */
    @Override
    public List<String> getServices() throws NacosException {
        List<String> group = new CopyOnWriteArrayList<>(customNacosDiscoveryProperties.getExtensionGroups());
        group.add(this.discoveryProperties.getGroup());
        ListView<String> allServices = null;
        for (String g : group) {
            ListView<String> services = this.namingService().getServicesOfServer(1, Integer.MAX_VALUE, g);
            if (allServices == null) {
                allServices = services;
            } else {
                allServices.getData().addAll(services.getData());
            }
        }
        return allServices.getData();
    }

    /**
     * 将 Nacos Instance 列表转换成 ServiceInstance 列表
     *
     * @param instances Nacos Instance 列表
     * @param serviceId 服务ID
     * @return ServiceInstance 列表
     */
    public static List<ServiceInstance> hostToServiceInstanceList(List<Instance> instances, String serviceId) {
        List<ServiceInstance> result = new ArrayList<>(instances.size());

        for (Instance instance : instances) {
            ServiceInstance serviceInstance = hostToServiceInstance(instance, serviceId);
            if (serviceInstance != null) {
                result.add(serviceInstance);
            }
        }

        return result;
    }

    /**
     * 将 Nacos Instance 转换成 ServiceInstance
     *
     * @param instance  Nacos Instance
     * @param serviceId 服务ID
     * @return ServiceInstance
     */
    public static ServiceInstance hostToServiceInstance(Instance instance, String serviceId) {
        if (instance != null && instance.isEnabled() && instance.isHealthy()) {
            NacosServiceInstance nacosServiceInstance = new NacosServiceInstance();
            nacosServiceInstance.setHost(instance.getIp());
            nacosServiceInstance.setPort(instance.getPort());
            nacosServiceInstance.setServiceId(serviceId);
            nacosServiceInstance.setInstanceId(instance.getInstanceId());
            Map<String, String> metadata = new HashMap<>();
            metadata.put("nacos.instanceId", instance.getInstanceId());
            metadata.put("nacos.weight", instance.getWeight() + "");
            metadata.put("nacos.healthy", instance.isHealthy() + "");
            metadata.put("nacos.cluster", instance.getClusterName() + "");
            if (instance.getMetadata() != null) {
                metadata.putAll(instance.getMetadata());
            }

            metadata.put("nacos.ephemeral", String.valueOf(instance.isEphemeral()));
            nacosServiceInstance.setMetadata(metadata);
            if (metadata.containsKey("secure")) {
                boolean secure = Boolean.parseBoolean(metadata.get("secure"));
                nacosServiceInstance.setSecure(secure);
            }

            return nacosServiceInstance;
        } else {
            return null;
        }
    }

    /**
     * 获取 NamingService
     *
     * @return NamingService
     */
    private NamingService namingService() {
        return this.nacosServiceManager.getNamingService();
    }
}
