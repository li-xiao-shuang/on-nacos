/*
 * Copyright 2022 on-nacos open source organization.
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.on.nacos.springboot.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springboot/nacos/config")
public class BindingClassController {
    
    @Resource
    private NacosDataSourceConfig nacosDataSourceConfig;
    
    
    @GetMapping(path = "binding/class/get")
    private Map<String, String> getNacosDataSource() {
        Map<String, String> result = new HashMap<>();
        result.put("name", nacosDataSourceConfig.getName());
        result.put("url", nacosDataSourceConfig.getUrl());
        result.put("username", nacosDataSourceConfig.getUsername());
        result.put("password", nacosDataSourceConfig.getPassword());
        result.put("driverClassName", nacosDataSourceConfig.getDriverClassName());
        return result;
    }
}
