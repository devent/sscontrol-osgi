/**
 * Copyright © 2016 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package com.anrisoftware.sscontrol.muellerpublicde

class PrometheusJobs {

    final static jobs =
    [
        [
            name: "ha_edge", value:
            """
scrape_interval: 30s
scrape_timeout: 10s
static_configs:
  - targets:
    - haproxy:9101
""",
        ],
        [
            name: "interscalar_com", value:
            """
metrics_path: /probe
params:
  module: [interscalar_com]
static_configs:
  - targets:
    - https://www.interscalar.com
    - https://interscalar.com
    - https://www.interscalar.org
    - https://interscalar.org
    - https://www.interscalar.info
    - https://interscalar.info
    - https://www.interskalar.com
    - https://interskalar.com
    - https://www.interskalar.org
    - https://interskalar.org
    - https://www.interskalar.info
    - https://interskalar.info
relabel_configs:
  - source_labels: [__address__]
    target_label: __param_target
  - source_labels: [__param_target]
    target_label: instance
  - target_label: __address__
    replacement: blackbox:9115
""",
        ],
        [
            name: "piwik_andrea", value:
            """
metrics_path: /probe
params:
  module: [piwik_andrea]
static_configs:
  - targets:
    - https://piwik.andrea.muellerpublic.de
relabel_configs:
  - source_labels: [__address__]
    target_label: __param_target
  - source_labels: [__param_target]
    target_label: instance
  - target_label: __address__
    replacement: blackbox:9115
""",
        ],
        [
            name: "project_anrisoftware_com", value:
            """
metrics_path: /probe
params:
  module: [project_anrisoftware_com]
static_configs:
  - targets:
    - https://project.anrisoftware.com
relabel_configs:
  - source_labels: [__address__]
    target_label: __param_target
  - source_labels: [__param_target]
    target_label: instance
  - target_label: __address__
    replacement: blackbox:9115
""",
        ],
    ]
}
