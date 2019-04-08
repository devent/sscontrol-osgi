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

class PrometheusBlackboxModules {

    final static modules = [
        [
            name: "interscalar_com", value:
            """\
prober: http
timeout: 15s
http:
  valid_status_codes: []
  method: GET
  no_follow_redirects: false
  fail_if_ssl: false
  fail_if_not_ssl: true
  fail_if_not_matches_regexp:
  - '<a href="https://www.interscalar.com/"><span>Home</span></a>'
"""             ],
        [
            name: "piwik_andrea", value:
            """\
prober: http
timeout: 15s
http:
  valid_status_codes: []
  method: GET
  no_follow_redirects: false
  fail_if_ssl: false
  fail_if_not_ssl: true
  fail_if_not_matches_regexp:
  - '<a href="https://piwik.org" title="Web analytics">'
"""             ],
        [
            name: "project_anrisoftware_com", value:
            """\
prober: http
timeout: 15s
http:
  valid_status_codes: []
  method: GET
  no_follow_redirects: false
  fail_if_ssl: false
  fail_if_not_ssl: true
  fail_if_not_matches_regexp:
  - '<title>Advanced Natural Research Institute</title>'
"""             ],
    ]
}
