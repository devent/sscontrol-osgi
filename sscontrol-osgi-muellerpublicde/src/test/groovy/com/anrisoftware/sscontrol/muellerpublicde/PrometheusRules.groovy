/*
 * Copyright 2016-2017 Erwin Müller <erwin.mueller@deventm.org>
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

class PrometheusRules {

    final static rules =
    [
        [
            name: "ha_backend_down", value:
            '''\
# Alert if a HAProxy backend is down.
groups:
- name: ha_backend_down
  rules:
  - alert: BackendDown
    expr: haproxy_backend_up == 0
    for: 30m
    labels:
      severity: critical
      priority: high
    annotations:
      description: '{{ $labels.backend }} of job {{ $labels.job }} has been down for more than 30 minutes.'
      summary: Backend {{ $labels.backend }} down
'''
        ],
        [
            name: "host_down", value:
            '''\
# Alert for any host that is unreachable for >x minutes.
groups:
- name: host_down
  rules:
  - alert: HostDown
    expr: probe_success == 0
    for: 30m
    labels:
      severity: critical
      priority: high
    annotations:
      description: '{{ $labels.instance }} of job {{ $labels.job }} has been down for more than 30 minutes.'
      summary: Host {{ $labels.instance }} down
'''
        ],
        [
            name: "host_slow", value:
            '''\
# Alert for any host that is slow for >x minutes.
groups:
- name: host_slow
  rules:
  - alert: HostSlow
    expr: quantile_over_time(0.5, probe_duration_seconds[1h]) > 4
    for: 2h
    labels:
      severity: non-critical
      priority: low
    annotations:
      description: '{{ $labels.instance }} of job {{ $labels.job }} is slow for more than 2 hours.'
      summary: Host {{ $labels.instance }} slow
'''
        ],
        [
            name: "host_blocked", value:
            '''\
# Alert for any host that is blocked for >x minutes.
groups:
- name: host_blocked
  rules:
  - alert: HostBlocked
    expr: quantile_over_time(0.5, probe_duration_seconds[1h]) > 6
    for: 2h
    labels:
      severity: critical
      priority: low
    annotations:
      description: '{{ $labels.instance }} of job {{ $labels.job }} is blocked for more than 2 hours.'
      summary: Host {{ $labels.instance }} blocked
'''
        ],
    ]
}
