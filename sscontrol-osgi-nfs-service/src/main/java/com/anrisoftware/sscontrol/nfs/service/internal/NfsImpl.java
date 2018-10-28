package com.anrisoftware.sscontrol.nfs.service.internal;

import static com.anrisoftware.sscontrol.nfs.service.internal.NfsServiceImpl.SERVICE_NAME;
import static com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.stringListStatement;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.nfs.service.external.Export;
import com.anrisoftware.sscontrol.nfs.service.external.Nfs;
import com.anrisoftware.sscontrol.nfs.service.internal.ExportImpl.ExportImplFactory;
import com.anrisoftware.sscontrol.types.host.external.HostServiceProperties;
import com.anrisoftware.sscontrol.types.host.external.HostServicePropertiesService;
import com.anrisoftware.sscontrol.types.host.external.TargetHost;
import com.anrisoftware.sscontrol.types.misc.external.StringListPropertyUtil.ListProperty;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * NFS service.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public class NfsImpl implements Nfs {

    private final NfsImplLogger log;

    private final List<TargetHost> targets;

    private final HostServiceProperties serviceProperties;

    private final List<Export> exports;

    private String version;

    @Inject
    private transient ExportImplFactory exportFactory;

    @AssistedInject
    NfsImpl(NfsImplLogger log, HostServicePropertiesService propertiesService, @Assisted Map<String, Object> args) {
        this.log = log;
        this.targets = new ArrayList<TargetHost>();
        this.serviceProperties = propertiesService.create();
        this.exports = new ArrayList<>();
        parseArgs(args);
    }

    /**
     * <pre>
     * export dir: "99-write_graphite" with {
     * }
     * </pre>
     */
    public Export export(Map<String, Object> args) {
        Export export = exportFactory.create(args);
        exports.add(export);
        log.exportAdded(this, export);
        return export;
    }

    @Override
    public String getName() {
        return format("%s-%s", SERVICE_NAME, version);
    }

    @Override
    public TargetHost getTarget() {
        return getTargets().get(0);
    }

    @Override
    public List<TargetHost> getTargets() {
        return targets;
    }

    public List<String> getProperty() {
        return stringListStatement(new ListProperty() {

            @Override
            public void add(String property) {
                serviceProperties.addProperty(property);
            }
        });
    }

    @Override
    public HostServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public List<Export> getExports() {
        return exports;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", getName()).append("hosts", targets).toString();
    }

    private void parseArgs(Map<String, Object> args) {
        parseVersion(args);
        parseTargets(args);
    }

    @SuppressWarnings("unchecked")
    private void parseTargets(Map<String, Object> args) {
        Object v = args.get("targets");
        if (v != null) {
            targets.addAll((List<TargetHost>) v);
        }
    }

    private void parseVersion(Map<String, Object> args) {
        Object v = args.get("version");
        this.version = v.toString();
    }

}
