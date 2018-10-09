package com.anrisoftware.sscontrol.command.shell.internal.cmd;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;

import com.anrisoftware.sscontrol.command.shell.external.Cmd;
import com.anrisoftware.sscontrol.command.shell.external.ssh.SshArgs;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class SshArgsImpl implements SshArgs {

    private final Map<String, Object> args;

    @Inject
    SshArgsImpl(@Assisted Map<String, Object> args) {
        this.args = args;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean useSshMaster() {
        String master = ObjectUtils
                .toString(args.get(Cmd.SSH_CONTROL_MASTER_ARG));
        String path = ObjectUtils.toString(args.get(Cmd.SSH_CONTROL_PATH_ARG));
        return "auto".equals(master) && !isBlank(path);
    }

    @Override
    public int size() {
        return args.size();
    }

    @Override
    public boolean isEmpty() {
        return args.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return args.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return args.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return args.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return args.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return args.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        args.putAll(m);
    }

    @Override
    public void clear() {
        args.clear();
    }

    @Override
    public Set<String> keySet() {
        return args.keySet();
    }

    @Override
    public Collection<Object> values() {
        return args.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return args.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return args.equals(o);
    }

    @Override
    public int hashCode() {
        return args.hashCode();
    }

    @Override
    public String toString() {
        return args.toString();
    }

}
