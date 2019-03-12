package com.anrisoftware.sscontrol.k8s.fromrepository.service.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.k8s.fromrepository.service.external.Crd;
import com.google.inject.assistedinject.Assisted;

/**
 * Custom resource definition.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class CrdImpl implements Crd {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface CrdImplFactory {

        Crd create(Map<String, Object> args);
    }

    private String kind;

    private String version;

    @Inject
    CrdImpl(@Assisted Map<String, Object> args) {
        parseArgs(args);
    }

    @Override
    public String getKind() {
        return kind;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private void parseArgs(Map<String, Object> args) {
        parseKind(args);
        parseVersion(args);
    }

    private void parseKind(Map<String, Object> args) {
        Object v = args.get("kind");
        assertThat("kind=null", v, notNullValue());
        this.kind = v.toString();
    }

    private void parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        assertThat("version=null", v, notNullValue());
        this.version = v.toString();
    }

}
