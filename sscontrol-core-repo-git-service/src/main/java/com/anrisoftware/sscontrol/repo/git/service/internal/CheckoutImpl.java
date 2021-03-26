/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
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
package com.anrisoftware.sscontrol.repo.git.service.internal;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.repo.git.service.external.Checkout;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Git</i> checkout.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CheckoutImpl implements Checkout {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
     * @version 1.0
     */
    public interface CheckoutImplFactory {

        Checkout create(Map<String, Object> args);

    }

    private String branch;

    private String tag;

    private String commit;

    @Inject
    CheckoutImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String getBranch() {
        return branch;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    @Override
    public String getCommit() {
        return commit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        Object v = args.get("branch");
        if (v != null) {
            setBranch(v.toString());
        }
        v = args.get("tag");
        if (v != null) {
            setTag(v.toString());
        }
        v = args.get("commit");
        if (v != null) {
            setCommit(v.toString());
        }
    }

}
