package com.anrisoftware.sscontrol.muellerpublicde.zz_cluster_test

import static com.anrisoftware.sscontrol.muellerpublicde.zz_cluster_test.ClusterTestResources.*
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.*

import com.anrisoftware.sscontrol.shell.external.utils.AbstractSocketsCondition

/**
 * Checks that the sockets to the masters and nodes is available for tests.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class ClusterTestMastersNodesSocketCondition extends AbstractSocketsCondition {

    ClusterTestMastersNodesSocketCondition() {
        super([
            socketFiles.masters,
            socketFiles.nodes
        ])
    }
}
