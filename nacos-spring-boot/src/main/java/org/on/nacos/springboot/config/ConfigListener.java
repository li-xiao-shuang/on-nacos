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


import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import org.springframework.stereotype.Component;

/**
 * @author lixiaoshuang
 */
@Component
public class ConfigListener {
    
    /**
     * 基于注解监听配置
     *
     * @param newContent
     * @throws Exception
     */
    @NacosConfigListener(dataId = "hello-nacos.text", timeout = 500)
    public void onChange(String newContent) {
        System.out.println("配置变更为 : \n" + newContent);
    }
    
}
