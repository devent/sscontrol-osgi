package com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster

import static com.anrisoftware.sscontrol.muellerpublicde.zz_andrea_cluster.AndreaClusterResources.*
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.*

import com.anrisoftware.sscontrol.shell.external.utils.AbstractSocketsCondition

/**
 * Checks that the sockets to the masters and nodes is available for tests.
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
class AndreaClusterMastersNodesSocket22Condition extends AbstractSocketsCondition {

    AndreaClusterMastersNodesSocket22Condition() {
        super([
            socketFiles22.masters,
            socketFiles22.nodes
        ])
    }
}
