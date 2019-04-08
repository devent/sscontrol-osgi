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

import static com.anrisoftware.sscontrol.shell.external.utils.UnixTestUtil.*
import static org.junit.Assume.*

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class MuellerpublicdeResources {

	static final int nodesSshPort = 22222

	static final Map socketFiles = [
		masters: [
			"/tmp/robobee@andrea-node-0.muellerpublic.de:22",
		],
		nodes: [
			"/tmp/robobee@andrea-node-1.muellerpublic.de:${nodesSshPort}",
			"/tmp/robobee@andrea-node-2.muellerpublic.de:${nodesSshPort}",
		],
		mails: [
			"/tmp/robobee@andrea-mail-0.muellerpublic.de:22",
		],
		etcd: [
			"/tmp/robobee@etcd-0.muellerpublic.de:22",
			"/tmp/robobee@etcd-1.muellerpublic.de:${nodesSshPort}",
			"/tmp/robobee@etcd-2.muellerpublic.de:${nodesSshPort}",
		],
	]

	static final Map socketFiles22 = [
		masters: [
			'/tmp/robobee@andrea-node-0.muellerpublic.de:22',
		],
		nodes: [
			'/tmp/robobee@andrea-node-1.muellerpublic.de:22',
			'/tmp/robobee@andrea-node-2.muellerpublic.de:22',
		],
	]

	static final List mastersHosts = [
		'andrea-node-0.muellerpublic.de',
	]

	static final List nodesHosts = [
		'andrea-node-1.muellerpublic.de',
		'andrea-node-2.muellerpublic.de',
	]

	static final List mailsHosts = [
		'andrea-mail-0.muellerpublic.de',
	]

	static final List etcdHosts = [
		'etcd-0.muellerpublic.de',
		'etcd-1.muellerpublic.de',
		'etcd-2.muellerpublic.de',
	]

	static final String localRobobeeSocket = "/tmp/${System.getProperty('user.name')}@localhost:22"

	static void assumeMastersExists() {
		assumeSocketsExists socketFiles.masters
	}

	static void assumeNodesExists() {
		assumeSocketsExists socketFiles.nodes
	}

	static void assumeEtcdExists() {
		assumeSocketsExists socketFiles.etcd
	}

	static void assumeMailExists() {
		assumeSocketsExists socketFiles.mails
	}

	static void assumeLocalExists() {
		assumeSocketExists localRobobeeSocket
	}

	static final URL muellerpublicKey = MuellerpublicdeResources.class.getResource('muellerpublic_de_robobee.txt')

	static final URL etcd_ca = MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_ca_cert.pem')

	static final Map etcd_vars = [
		client: [
			ca: "$etcd_ca",
			cert: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_cert.pem')}",
			key: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_key.pem')}",
		],
		nodes: [
			[
				ca: "$etcd_ca",
				cert: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_0_cert.pem')}",
				key: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_0_key.pem')}",
			],
			[
				ca: "$etcd_ca",
				cert: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_1_cert.pem')}",
				key: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_1_key.pem')}",
			],
			[
				ca: "$etcd_ca",
				cert: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_2_cert.pem')}",
				key: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_2_key.pem')}",
			],
		]
	]

	static final Map k8s_vars = [
		joinCommand: "kubeadm join --token 733471.e9d3a2f2370e0915 185.24.220.41:6443 --discovery-token-ca-cert-hash sha256:2c18995abbf9ba44f93d7b8c364c447bc3473abe7b372a11b82c0a9a9dadae70",
		nodes: [
			[
				labels: [
					"robobeerun.com/heapster=required",
					"robobeerun.com/dashboard=required"
				],
				taints: [],
			],
			[
				labels: [
					"robobeerun.com/edge-router=required",
					"muellerpublic.de/interscalar-com=required",
					"robobeerun.com/cert-manager=required",
					"robobeerun.com/ingress-nginx=required",
				],
				taints: [],
			],
			[
				labels: [
					"muellerpublic.de/anrisoftware-com=required"
				],
				taints: [],
			],
		],
		etcd: [
			certs:[
				ca: "$etcd_ca",
				cert: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_cert.pem')}",
				key: "${MuellerpublicdeResources.class.getResource('andrea_muellerpublicde_etcd_k8s_client_key.pem')}",
			]
		],
		rsync: [
			key: MuellerpublicdeResources.class.getResource('rsync_id_rsa.txt'),
			publicKey: MuellerpublicdeResources.class.getResource('rsync_id_rsa_pub.txt'),
		]
	]
}
