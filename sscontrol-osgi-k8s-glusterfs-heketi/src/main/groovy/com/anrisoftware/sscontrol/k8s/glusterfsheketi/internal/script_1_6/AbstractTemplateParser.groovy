package com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_6

import com.anrisoftware.sscontrol.k8s.glusterfsheketi.internal.script_1_5.TemplateParser

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
abstract class AbstractTemplateParser implements TemplateParser {

    @Override
    boolean isKubeFile(String fileName) {
        String[] split = fileName.split(/\./)
        def name = split[-2]
        def m = (name =~ /(.*)-(\w*)/)
        m.find()
    }

    @Override
    String getFilename(String fileName) {
        String[] split = fileName.split(/\./)
        def name = split[-2]
        def m = (name =~ /(.*)-(\w*)/)
        m.find()
        "${m.group(1)}.${m.group(2)}"
    }

    @Override
    boolean getNeedCopyRepo() {
        false
    }
}
