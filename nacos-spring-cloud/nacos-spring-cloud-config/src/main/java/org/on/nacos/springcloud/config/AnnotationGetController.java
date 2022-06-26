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

package org.on.nacos.springcloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springcloud/nacos/config")
public class AnnotationGetController {
    
    @Value(value = "${spring.datasource.name:}")
    private String name;
    
    @Value(value = "${spring.datasource.url:}")
    private String url;
    
    @Value(value = "${spring.datasource.username:}")
    private String username;
    
    @Value(value = "${spring.datasource.password:}")
    private String password;
    
    @Value(value = "${spring.datasource.driverClassName:}")
    private String driverClassName;
    
    @GetMapping(path = "annotation/get")
    private Map<String, String> getNacosDataSource2() {
        Map<String, String> result = new HashMap<>();
        result.put("name", name);
        result.put("url", url);
        result.put("username", username);
        result.put("password", password);
        result.put("driverClassName", driverClassName);
        return result;
    }
}
